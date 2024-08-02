package com.app.countriespro.services

import com.app.countriespro.models.CountriesModel
import retrofit2.http.GET

interface AuthService {


    @GET("countries.json")
    suspend fun fetchCountries() : CountriesModel
}