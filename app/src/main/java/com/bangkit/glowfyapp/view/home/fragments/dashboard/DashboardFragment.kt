package com.bangkit.glowfyapp.view.home.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.glowfyapp.databinding.FragmentDashboardBinding
import com.bangkit.glowfyapp.view.home.fragments.dashboard.adapters.ProductAdapter
import com.bangkit.glowfyapp.view.home.fragments.dashboard.adapters.SkinAdapter
import com.bangkit.glowfyapp.view.home.fragments.dashboard.adapters.ArticleAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DashboardViewModel

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

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        setupView()
    }

    private fun setupView() {
        productItemHandler()
        skinItemHandler()
        articleHandler()
    }

    private fun articleHandler() {
        viewModel.products.observe(viewLifecycleOwner) { articles ->
            articleAdapter = ArticleAdapter(articles)
            binding.apply {
                articleViewPager.adapter = articleAdapter
                circleIndicator.setViewPager(articleViewPager)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error->
            if(error.isNotEmpty()){
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar(loading)
        }
    }

    private fun skinItemHandler() {
        viewModel.products.observe(viewLifecycleOwner) { skins ->
            skinAdapter = SkinAdapter(skins)
            binding.apply {
                skinRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                skinRv.adapter = skinAdapter
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error->
            if(error.isNotEmpty()){
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar(loading)
        }
    }

    private fun productItemHandler() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter = ProductAdapter(products)
            binding.apply {
                productRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                productRv.adapter = productAdapter
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error->
            if(error.isNotEmpty()){
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar(loading)
        }
    }

    private fun progressBar(onloading: Boolean){
        if(onloading){
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