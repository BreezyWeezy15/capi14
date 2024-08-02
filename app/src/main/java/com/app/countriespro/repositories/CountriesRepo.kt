package com.app.countriespro.repositories

import com.app.countriespro.models.CountriesModel
import com.app.countriespro.services.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountriesRepo @Inject constructor(private var authService: AuthService)
    : CountriesRepoImpl {


    override suspend fun fetchData(): Flow<CountriesModel> {
       return flow {
           emit(authService.fetchCountries())
       }
    }
}