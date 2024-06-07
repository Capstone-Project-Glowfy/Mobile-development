package com.bangkit.glowfyapp.view.home.fragments.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.items.ArticlesItem
import com.bangkit.glowfyapp.data.models.items.ProductItem
import com.bangkit.glowfyapp.data.models.items.SkinsItem
import com.bangkit.glowfyapp.databinding.FragmentDashboardBinding
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.view.auth.LoginActivity
import com.bangkit.glowfyapp.view.detailProduct.ProductDetailActivity
import com.bangkit.glowfyapp.view.home.HomeViewModel
import com.bangkit.glowfyapp.view.home.fragments.dashboard.adapters.ArticleAdapter
import com.bangkit.glowfyapp.view.home.fragments.dashboard.adapters.ProductAdapter
import com.bangkit.glowfyapp.view.home.fragments.dashboard.adapters.SkinAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var productAdapter: ProductAdapter
    private lateinit var skinAdapter: SkinAdapter
    private lateinit var articleAdapter: ArticleAdapter

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
    }

    private fun setupData() {
        getSession()
    }

    private fun getSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(context, LoginActivity::class.java))
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
            }
        }
    }

    private fun setSkins(skins: List<SkinsItem>) {
        if (skins.isEmpty()) {
            showToast(getString(R.string.empty_skins))
        } else {
            skinAdapter = SkinAdapter(skins)
            binding.apply {
                skinRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                skinRv.adapter = skinAdapter
            }
        }
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

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(onLoading: Boolean){
        if(onLoading){
            binding.skinProgressBar.visibility = View.VISIBLE
            binding.productProgressBar.visibility = View.VISIBLE
            binding.articleProgressBar.visibility = View.VISIBLE
        }else{
            binding.skinProgressBar.visibility = View.GONE
            binding.productProgressBar.visibility = View.GONE
            binding.articleProgressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}