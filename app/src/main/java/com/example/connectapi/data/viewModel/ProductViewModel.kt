package com.example.connectapi.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.connectapi.data.paging.ProductPagingSource
import com.example.connectapi.data.service.ProductService

class ProductViewModel(private val productService: ProductService) : ViewModel() {
    val product = Pager(PagingConfig(pageSize = 10)) {
        ProductPagingSource(productService)
    }.flow.cachedIn(viewModelScope)
}