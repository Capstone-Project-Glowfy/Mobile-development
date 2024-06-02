package com.bangkit.glowfyapp.view.home.fragments.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.databinding.FragmentProductBinding

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ProductCategoryAdapter

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

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        setupView()
        setupAction()
    }

    private fun setupView() {
        showListProductCategory()
    }

    private fun setupAction() {
        radioButtonHandler()
    }

    private fun showListProductCategory() {
        viewModel.products.observe(viewLifecycleOwner) { skins ->
            adapter = ProductCategoryAdapter(skins)
            binding.apply {
                productCategoryRv.layoutManager = GridLayoutManager(context, 2)
                productCategoryRv.adapter = adapter
            }
        }
        // default - data
        viewModel.fetchProductByCategory("beauty")
        viewModel.error.observe(viewLifecycleOwner) { error->
            if(error.isNotEmpty()){
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar(loading)
        }
    }

    private fun radioButtonHandler() {
        binding.categoryRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val category = when (checkedId) {
                // change by the category on real API
                R.id.normalBtn -> "beauty"
                R.id.oilyBtn -> "fragrances"
                R.id.dryBtn -> "furniture"
                R.id.acneBtn -> "groceries"
                else -> "beauty "
            }
            viewModel.fetchProductByCategory(category)
        }
    }

    private fun progressBar(onLoading: Boolean){
        if(onLoading){
            binding.categoryProgressbar.visibility = View.VISIBLE
            binding.productCategoryRv.visibility = View.GONE
        }else{
            binding.categoryProgressbar.visibility = View.GONE
            binding.productCategoryRv.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}