package com.example.connectapi

import ReviewAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connectapi.databinding.ActivityReviewProductBinding
import com.example.connectapi.dto.api.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList as emptyList1

class ReviewProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewProductBinding
    private lateinit var reviewAdapter: ReviewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detailReview.layoutManager = LinearLayoutManager(this)
        reviewAdapter = ReviewAdapter(emptyList1())
        binding.detailReview.adapter = reviewAdapter

        // Retrieve product ID and load reviews
        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId != -1) {
            loadReviews(productId)
        } else {
            Toast.makeText(this, "Invalid product ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadReviews(productId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.productService.getProductById(productId)
                if (response.isSuccessful) {
                    val product = response.body()
                    val reviews = product?.reviews ?: emptyList1()
                    withContext(Dispatchers.Main) {
                        reviewAdapter.updateReviews(reviews)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ReviewProductActivity,
                            "Failed to load reviews",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ReviewProductActivity,
                        "Error: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}