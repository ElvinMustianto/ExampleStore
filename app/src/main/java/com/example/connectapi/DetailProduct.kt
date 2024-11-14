package com.example.connectapi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.connectapi.adapter.ProductImageAdapter
import com.example.connectapi.databinding.ActivityDetailProductBinding
import com.example.connectapi.dto.api.ApiClient
import com.example.connectapi.dto.model.Product
import com.example.connectapi.dto.model.Reviews
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DetailProduct : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // Mengambil ID produk dari intent
        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId != -1) {
            lifecycleScope.launch {
                getProductDetail(productId)
            }
        } else {
            Toast.makeText(this, "Invalid product ID", Toast.LENGTH_SHORT).show()
        }
        // In DetailProduct
        binding.btnDetailReview.setOnClickListener {
            val intent = Intent(this, ReviewProductActivity::class.java)
            intent.putExtra("PRODUCT_ID", productId)
            startActivity(intent)
        }
    }

    private suspend fun getProductDetail(id: Int) {
        try {
            val response: Response<Product> = ApiClient.productService.getProductById(id)
            if (response.isSuccessful) {
                val product = response.body()
                product?.let {
                    updateProductUI(it)
                    loadSingleReview(it)
                } ?: showToast("Product not found")
            } else {
                showToast("Error: ${response.message()}")
            }
        } catch (error: Exception) {
            showToast("Error: ${error.localizedMessage}")
        }
    }

    private fun loadSingleReview(product: Product) {
        val singleReview = product.reviews.firstOrNull()
        singleReview?.let {
            updateReviewUI(it)
        } ?: showToast("No reviews available")
    }
    private fun updateReviewUI(reviews: Reviews) {
        binding.reviewerName.text = reviews.reviewerName
        binding.ratingBar.rating = reviews.rating.toFloat()
        binding.comment.text = reviews.comment
        binding.date.text = parseAndFormatDate(reviews.date)
    }

    @SuppressLint("SetTextI18n")
    private fun updateProductUI(product: Product) {
        binding.title.text = product.title
        binding.brand.text = product.brand
        binding.description.text = product.description
        binding.price.text = "$ ${product.price}"
        binding.rating.text = product.rating.toString()

        // Pastikan daftar gambar tidak kosong dan berisi URL yang valid
        if (product.images.isNotEmpty() && product.images[0] != "...") {
            val adapter = ProductImageAdapter(product.images)
            binding.viewPager.adapter = adapter
        } else {
            Toast.makeText(this, "No images available", Toast.LENGTH_SHORT).show()
        }

        // Menampilkan dimensi produk dengan null safety
        product.dimensions.let { dimension ->
            binding.widthProduct.text = "Width : ${dimension.width} cm"
            binding.heightProduct.text = "Height : ${dimension.height} cm"
            binding.depthProduct.text = "Depth : ${dimension.depth} cm"
        }
    }
    private fun parseAndFormatDate(dateString: String): String? {
        return try {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            originalFormat.timeZone = TimeZone.getTimeZone("UTC")
            val targetFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            originalFormat.parse(dateString)?.let { targetFormat.format(it) }
        } catch (e: Exception) {
            dateString
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
