package com.lukasz.galinski.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface NetworkDataSource {
    fun get(): Flow<Long>
}