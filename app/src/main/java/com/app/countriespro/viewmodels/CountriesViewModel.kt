package com.app.countriespro.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.countriespro.models.CountriesModel
import com.app.countriespro.repositories.CountriesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val countriesRepo: CountriesRepo
) : ViewModel() {

    private val _countriesState = MutableStateFlow<UiStates>(UiStates.INITIAL)
    val countriesState: StateFlow<UiStates> get() = _countriesState

    private var dataFetched = false

    init {
        fetchRemoteJson()
    }

    private fun fetchRemoteJson() {
        _countriesState.value = UiStates.LOADING
        viewModelScope.launch {
            try {
                // Switch to IO dispatcher for network request
                val response = withContext(Dispatchers.IO) {
                    countriesRepo.fetchData()
                }
                // Handle the response on the main thread
                handleResponse(response)
            } catch (e: Exception) {
                // Handle any errors
                handleError(e)
            }
        }
    }

    private fun handleResponse(data: CountriesModel) {
        dataFetched = true
        _countriesState.value = UiStates.SUCCESS(data)
    }

    private fun handleError(e: Exception) {
        _countriesState.value = UiStates.ERROR(e.message ?: "Unknown error")
    }

    sealed class UiStates {
        data object LOADING : UiStates()
        data class SUCCESS(val data: CountriesModel) : UiStates()
        data class ERROR(val error: String) : UiStates()
        data object INITIAL : UiStates()
    }
}