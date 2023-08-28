package com.lukasz.galinski.fluffy.framework.network

import retrofit2.http.GET

interface NetworkService {
    @GET("/balance")
    fun getBalance(): Long
}