package com.example.connectapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.connectapi.adapter.ProductImageAdapter
import com.example.connectapi.data.api.ApiClient
import com.example.connectapi.data.model.Product
import com.example.connectapi.databinding.ActivityDetailProductBinding
import kotlinx.coroutines.launch
import retrofit2.Response

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
    }

    private suspend fun getProductDetail(id: Int) {
        try {
            val response: Response<Product> = ApiClient.productService.getProductById(id)
            if (response.isSuccessful) {
                val product = response.body()
                if (product != null) {
                    updateUI(product)
                } else {
                    Toast.makeText(
                        this@DetailProduct,
                        "Product not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@DetailProduct,
                    "Error: ${response.message()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (error: Exception) {
            Toast.makeText(
                this@DetailProduct,
                "Error: ${error.localizedMessage}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(product: Product) {
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
    }
}
