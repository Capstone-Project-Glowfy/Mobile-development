package com.bangkit.glowfyapp.view.detail.detailArticles

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.glowfyapp.R
import com.bangkit.glowfyapp.data.models.response.ArticlesItem
import com.bangkit.glowfyapp.databinding.ActivityArticlesDetailBinding
import com.bumptech.glide.Glide

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
        setupData()
        binding.backButton.setOnClickListener { onBackPressed() }
    }

    private fun setupData() {
        val article = intent.getParcelableExtra<ArticlesItem>("EXTRA_ARTICLE")
        if (article != null) {
            bindArticleDetails(article)
        }
    }

    private fun bindArticleDetails(article: ArticlesItem) {
        with(binding) {
            Glide.with(this@ArticlesDetailActivity)
                .load(article.foto)
                .into(articleImage)

            articleTitle.text = article.judul
            articleAuthor.text = article.author
            articleRelease.text = article.tahun.toString()
            articleIsi.text = article.isi
        }
    }
}