package com.app.countriespro.repositories

import com.app.countriespro.models.CountriesModel
import kotlinx.coroutines.flow.Flow

interface CountriesRepoImpl {
    suspend fun fetchData(): CountriesModel
}