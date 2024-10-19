package com.example.connectapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.connectapi.data.api.ApiClient
import com.example.connectapi.data.model.Product
import com.example.connectapi.databinding.ActivityDetailProductBinding
import retrofit2.Call
import retrofit2.Callback
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
            getProductDetail(productId)
        } else {
            Toast.makeText(this, "Invalid product ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getProductDetail(id: Int) {
        // Memanggil API untuk mendapatkan detail produk
        val call = ApiClient.productService.getProductById(id)
        call.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        updateUI(product)
                    } else {
                        Toast.makeText(this@DetailProduct, "Product not found", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this@DetailProduct,
                        "Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(
                    this@DetailProduct,
                    "Error: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(product: Product) {
        binding.detailTitle.text = product.title
        binding.detailBrand.text = product.brand
        binding.detailDescription.text = product.description
        binding.detailPrice.text = "$ ${product.price}"

        Glide.with(this)
            .load(product.thumbnail)
            .centerCrop()
            .into(binding.detailThumbnail)
    }
}
