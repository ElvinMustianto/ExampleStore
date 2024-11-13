package com.example.connectapi.dto.service

import com.example.connectapi.dto.model.Product
import com.example.connectapi.dto.model.Products
import com.example.connectapi.dto.model.Reviews
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
    ): Response<Products>

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
    ): Response<Products>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>

}