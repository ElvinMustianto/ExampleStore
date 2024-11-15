package com.example.connectapi

import com.example.connectapi.dto.factory.ViewModelFactory
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.connectapi.adapter.ProductAdapter
import com.example.connectapi.dto.api.ApiClient
import com.example.connectapi.dto.model.Product
import com.example.connectapi.dto.viewModel.ProductViewModel
import com.example.connectapi.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var binding: ActivityMainBinding

    private val productViewModel: ProductViewModel by viewModels {
        ViewModelFactory(ApiClient.productService)
    }

    private var isSearchActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        swipeRefreshLayout = binding.main
        recyclerView = binding.recyclerView

        productAdapter = ProductAdapter { product -> onClick(product) }
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false // Membuat area pencarian terbuka ketika diklik
        }

        binding.categories.setOnClickListener {
            val categories = Intent(this, CategoryActivity::class.java)
            startActivity(categories)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()){
                    isSearchActive = true
                    search(query)
                } else {
                    observeProduct()
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    observeProduct()
                }
                return false
            }

        })
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

    private fun onClick(product: Product) {
        val goToDetail = Intent(this, DetailProduct::class.java)
        goToDetail.putExtra("PRODUCT_ID", product.id)
        startActivity(goToDetail)
    }

    private fun search(query: String) {
        productViewModel.updateQuery(query)
        lifecycleScope.launch {
            productViewModel.searchProduct(query).collectLatest { paging ->
                swipeRefreshLayout.isRefreshing = false
                productAdapter.submitData(paging)
            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (isSearchActive) {
            // Jika pencarian aktif, reset pencarian tanpa refresh
            isSearchActive = false
            binding.searchView.setQuery("", false) // Reset search query
            binding.searchView.clearFocus() // Hapus fokus dari SearchView
            binding.searchView.isIconified = true
            recyclerView.scrollToPosition(0) // Scroll ke posisi atas
        } else {
            // Jika pencarian tidak aktif, lanjutkan dengan default back button behavior
            super.onBackPressed()
        }
    }
}

