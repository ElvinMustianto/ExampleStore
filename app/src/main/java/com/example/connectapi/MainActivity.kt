package com.example.connectapi

import ViewModelFactory
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.connectapi.adapter.ProductAdapter
import com.example.connectapi.data.api.ApiClient
import com.example.connectapi.data.model.Product
import com.example.connectapi.data.viewModel.ProductViewModel
import com.example.connectapi.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var binding: ActivityMainBinding

    private val productViewModel: ProductViewModel by viewModels {
        ViewModelFactory(ApiClient.productService)
    }

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
            refreshData()
        }
        observeProduct()
    }

    private fun observeProduct() {
        lifecycleScope.launch {
            productViewModel.product.collectLatest { paging ->
                swipeRefreshLayout.isRefreshing = false
                productAdapter.submitData(paging)
            }
        }
    }

    private fun refreshData() {
        swipeRefreshLayout.isRefreshing = true
        productAdapter.refresh()
    }

    private fun OnClick(product: Product) {
        val goToDetail = Intent(this, DetailProduct::class.java)
        goToDetail.putExtra("PRODUCT_ID", product.id)
        startActivity(goToDetail)
    }
}

