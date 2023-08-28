package com.lukasz.galinski.core.domain.repository

class NetworkDataRepository(private val dataSource: NetworkDataSource) {
    fun get() = dataSource.get()
}