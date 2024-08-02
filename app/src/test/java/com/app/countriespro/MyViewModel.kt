package com.app.countriespro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.countriespro.models.CountriesModel
import com.app.countriespro.repositories.CountriesRepoImpl
import com.app.countriespro.viewmodels.CountriesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(private val countriesRepo: CountriesRepoImpl) : ViewModel() {
    private val _countriesState = MutableLiveData<UiStates>()
    val countriesState: LiveData<UiStates> = _countriesState

    fun getCountries() {
        viewModelScope.launch {
            try {
                _countriesState.value = UiStates.LOADING
                val countries = withContext(Dispatchers.IO) {
                    countriesRepo.fetchData()
                }
                _countriesState.value = UiStates.SUCCESS(countries)
            } catch (ex: Exception) {
                _countriesState.value = UiStates.ERROR(ex.message ?: "Unknown error")
            }
        }
    }
}

sealed class UiStates {
    data object LOADING : UiStates()
    data class SUCCESS(val data: CountriesModel) : UiStates()
    data class ERROR(val error: String) : UiStates()
}