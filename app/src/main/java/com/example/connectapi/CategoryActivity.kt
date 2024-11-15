package com.example.connectapi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.connectapi.adapter.CategoryAdapter
import com.example.connectapi.databinding.ActivityCategoryBinding
import com.example.connectapi.dto.api.ApiClient
import com.example.connectapi.dto.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        categoryAdapter = CategoryAdapter { category ->
            onClick(category)
        }
        binding.categories.apply {
            layoutManager = LinearLayoutManager(this@CategoryActivity)
            adapter = categoryAdapter
        }

        // Fetch categories
        fetchCategories()
    }

    private fun fetchCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.productService.getCategories()
                if (response.isSuccessful) {
                    val categories = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        categoryAdapter.submitList(categories)
                    }
                } else {
                    Log.e("CategoryActivity", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("CategoryActivity", "Exception: ${e.message}")
            }
        }
    }

    private fun onClick(category: Category) {
        Toast.makeText(this, "Clicked on category: ${category.name}", Toast.LENGTH_SHORT).show()
    }
}