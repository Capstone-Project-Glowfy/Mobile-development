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
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.ResultApi
import com.bangkit.glowfyapp.data.models.items.ProductItem
import com.bangkit.glowfyapp.databinding.FragmentProductBinding
import com.bangkit.glowfyapp.utils.ViewModelFactory
import com.bangkit.glowfyapp.view.adapters.ProductCategoryAdapter
import com.bangkit.glowfyapp.view.adapters.shimmer.ShimmerProductCategoryAdapter
import com.bangkit.glowfyapp.view.auth.LoginActivity
import com.bangkit.glowfyapp.view.detail.detailProducts.ProductDetailActivity
import com.bangkit.glowfyapp.view.home.HomeViewModel

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: ProductCategoryAdapter
    private lateinit var shimmerAdapter: ShimmerProductCategoryAdapter
    private var category = "normal"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()

    }

    private fun setupData() {
        getSession()
        binding.swipeRefreshLayout.setOnRefreshListener { getSession() }
    }

    private fun getSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(context, LoginActivity::class.java))
                requireActivity().finish()
            } else {
                setCategory(user.token)
            }
        }
    }

    private fun setCategory(token: String) {
        getProductData(token, category)
        binding.categoryRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            category = when (checkedId) {
                R.id.normalBtn -> "normal"
                R.id.oilyBtn -> "oily"
                R.id.dryBtn -> "dry"
                R.id.acneBtn -> "acne"
                else -> "normal"
            }
            getProductData(token, category)
        }
    }

    private fun getProductData(token: String, category: String) {
        viewModel.getProductsByCategory(token, category).observe(viewLifecycleOwner) { result ->
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

    private fun setProducts(products: List<ProductItem>) {
        if (products.isEmpty()) {
            showToast(getString(R.string.empty_product))
        } else {
            adapter = ProductCategoryAdapter(products)
            binding.apply {
                productCategoryRv.layoutManager = GridLayoutManager(context, 2)
                productCategoryRv.adapter = adapter
                adapter.setOnItemClickCallback(object : ProductCategoryAdapter.OnItemClickCallback {
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
            showShimmer()
        }else{
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showShimmer() {
        shimmerAdapter = ShimmerProductCategoryAdapter()
        binding.productCategoryRv.adapter = shimmerAdapter
        binding.productCategoryRv.layoutManager = GridLayoutManager(context, 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}