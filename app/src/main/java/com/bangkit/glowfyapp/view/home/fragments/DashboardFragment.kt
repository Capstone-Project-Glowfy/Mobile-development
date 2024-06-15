package com.bangkit.glowfyapp.view.home.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.response.ArticlesItem
import com.bangkit.glowfyapp.data.models.response.ProductItem
import com.bangkit.glowfyapp.data.models.response.SkinsItem
import com.bangkit.glowfyapp.databinding.FragmentDashboardBinding
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.view.adapters.ArticleAdapter
import com.bangkit.glowfyapp.view.adapters.ProductAdapter
import com.bangkit.glowfyapp.view.adapters.SkinAdapter
import com.bangkit.glowfyapp.view.adapters.shimmer.ShimmerArticleAdapter
import com.bangkit.glowfyapp.view.adapters.shimmer.ShimmerProductAdapter
import com.bangkit.glowfyapp.view.adapters.shimmer.ShimmerSkinAdapter
import com.bangkit.glowfyapp.view.detail.detailArticles.ArticlesDetailActivity
import com.bangkit.glowfyapp.view.detail.detailProducts.ProductDetailActivity
import com.bangkit.glowfyapp.view.detail.detailSkins.SkinsDetailActivity
import com.bangkit.glowfyapp.view.history.ScanHistoryActivity
import com.bangkit.glowfyapp.view.home.HomeViewModel
import com.bangkit.glowfyapp.view.welcome.WelcomeActivity
import com.bumptech.glide.Glide

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var productAdapter: ProductAdapter
    private lateinit var skinAdapter: SkinAdapter
    private lateinit var articleAdapter: ArticleAdapter

    private lateinit var shimmerProductAdapter: ShimmerProductAdapter
    private lateinit var shimmerSkinAdapter: ShimmerSkinAdapter
    private lateinit var shimmerArticleAdapter: ShimmerArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        binding.swipeRefreshLayout.setOnRefreshListener { setupData() }
        binding.historyCv.setOnClickListener { navigateToHistory() }
    }

    private fun setupData() {
        getSession()
        setupProfile()
    }

    override fun onResume() {
        super.onResume()
        setupProfile()
    }

    private fun getSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(context, WelcomeActivity::class.java))
                requireActivity().finish()
            } else {
                getAllData(user.token)
                setDashboardName(user.name)
            }
        }
    }

    private fun setDashboardName(name: String) {
        binding.userName.text = getString(R.string.userNameGreeting, name)
    }

    private fun setupProfile() {
        viewModel.dbProfile.observe(viewLifecycleOwner) { profile ->
            Log.d("ProfileFragment", "setupProfile: $profile")
            profile?.let {
                Glide.with(requireContext())
                    .load(it.profileImage)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .into(binding.profileImage)
            } ?: run {
                Log.d("ProfileFragment", "No profile found")
            }
        }
        viewModel.getProfile()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun getAllData(token: String) {
        viewModel.getArticles(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultApi.Loading -> showLoading(true)
                is ResultApi.Success -> {
                    showLoading(false)
                    setArticles(result.data.articles)
                }
                is ResultApi.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }

        viewModel.getSkins(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultApi.Loading -> showLoading(true)
                is ResultApi.Success -> {
                    showLoading(false)
                    setSkins(result.data.skins)
                }
                is ResultApi.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }

        viewModel.getProducts(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultApi.Loading -> showLoading(true)
                is ResultApi.Success -> {
                    showLoading(false)
                    setProducts(result.data.product)
                }
                is ResultApi.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }

    }

    private fun setArticles(articles: List<ArticlesItem>) {
        if (articles.isEmpty()) {
            showToast(getString(R.string.empty_article))
        } else {
            articleAdapter = ArticleAdapter(articles)
            binding.apply {
                articleViewPager.adapter = articleAdapter
                circleIndicator.setViewPager(articleViewPager)
                articleAdapter.setOnItemClickCallback(object : ArticleAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ArticlesItem) {
                        navigateToDetailArticle(data)
                    }
                })
            }
        }
    }

    private fun navigateToDetailArticle(data: ArticlesItem) {
        val intent = Intent(requireContext(), ArticlesDetailActivity::class.java)
        intent.putExtra("EXTRA_ARTICLE", data)
        startActivity(intent)
    }

    private fun setSkins(skins: List<SkinsItem>) {
        if (skins.isEmpty()) {
            showToast(getString(R.string.empty_skins))
        } else {
            skinAdapter = SkinAdapter(skins)
            binding.apply {
                skinRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                skinRv.adapter = skinAdapter
                skinAdapter.setOnItemClickCallback(object : SkinAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: SkinsItem) {
                        navigateToDetailSkin(data)
                    }
                })
            }
        }
    }

    private fun navigateToDetailSkin(data: SkinsItem) {
        val intent = Intent(requireContext(), SkinsDetailActivity::class.java)
        intent.putExtra("EXTRA_SKIN", data)
        startActivity(intent)
    }

    private fun setProducts(products: List<ProductItem>) {
        if (products.isEmpty()) {
            showToast(getString(R.string.empty_product))
        } else {
            productAdapter = ProductAdapter(products)
            binding.apply {
                productRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                productRv.adapter = productAdapter
                productAdapter.setOnItemClickCallback(object : ProductAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ProductItem) {
                        navigateToDetailProduct(data)
                    }
                })
            }
        }
    }

    private fun navigateToDetailProduct(data: ProductItem) {
        val intent = Intent(requireContext(), ProductDetailActivity::class.java)
        intent.putExtra("EXTRA_PRODUCT", data)
        startActivity(intent)
    }

    private fun navigateToHistory() {
        val intent = Intent(requireContext(), ScanHistoryActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(onLoading: Boolean){
        if(onLoading){
            showShimmer()
        } else {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showShimmer() {
        //product shimmer
        shimmerProductAdapter = ShimmerProductAdapter()
        binding.productRv.adapter = shimmerProductAdapter
        binding.productRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //skin shimmer
        shimmerSkinAdapter = ShimmerSkinAdapter()
        binding.skinRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.skinRv.adapter = shimmerSkinAdapter

        //article shimmer
        shimmerArticleAdapter = ShimmerArticleAdapter()
        binding.articleViewPager.adapter = shimmerArticleAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}