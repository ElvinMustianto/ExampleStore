package com.example.connectapi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.connectapi.adapter.ProductAdapter
import com.example.connectapi.data.api.ApiClient
import com.example.connectapi.data.model.Product
import com.example.connectapi.data.model.Products
import com.example.connectapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var call: Call<Products>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        swipeRefreshLayout = binding.main
        recyclerView = binding.recyclerView

        productAdapter = ProductAdapter { product -> OnClick(product) }
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
        swipeRefreshLayout.setOnRefreshListener {
            getData()
        }
        getData()
    }

    private fun getData() {
        swipeRefreshLayout.isRefreshing = true

        call = ApiClient.productService.getAll()
        call.enqueue(object : Callback<Products>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    productAdapter.submitList(response.body()?.products)
                    productAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun OnClick(product: Product) {
        val goToDetail = Intent(this, DetailProduct::class.java)
        goToDetail.putExtra("PRODUCT_ID", product.id)
        startActivity(goToDetail)
    }
}