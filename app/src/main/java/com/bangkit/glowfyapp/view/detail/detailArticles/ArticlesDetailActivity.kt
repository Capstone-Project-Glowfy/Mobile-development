package com.bangkit.glowfyapp.view.detail.detailArticles

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.response.ArticlesItem
import com.bangkit.glowfyapp.databinding.ActivityArticlesDetailBinding

class ArticlesDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticlesDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticlesDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupData() {
        val article = intent.getParcelableExtra<ArticlesItem>("EXTRA_ARTICLE")
        if (article != null) {
            bindArticleDetails(article)
        }
    }

    private fun bindArticleDetails(article: ArticlesItem) {
        TODO("Not yet implemented")
    }
}