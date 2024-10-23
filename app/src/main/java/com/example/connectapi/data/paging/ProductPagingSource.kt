package com.example.connectapi.data.paging

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import com.example.connectapi.data.model.Product
import com.example.connectapi.data.service.ProductService
import java.io.IOException

class ProductPagingSource(
    private val productService: ProductService,
    private val query: String? = null,
    private val limit: Int = 10,
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 0
        return try {
            val response = if (query.isNullOrEmpty()) {
                productService.getProducts(limit, skip = page * limit)
            } else {
                productService.searchProducts(query, limit, skip = page * limit)
            }
            val products = response.body()?.products ?: emptyList()
            val nextKey = if (products.isEmpty()) null else page + 1

            Page(
                data = products,
                prevKey = if (page == 0) null else page - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}