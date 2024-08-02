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
    private var countriesRepo: CountriesRepo
) : ViewModel(){


    private val _countriesState : MutableStateFlow<UiStates> = MutableStateFlow(UiStates.INITIAL)
    val countriesState : StateFlow<UiStates> get() = _countriesState


    fun getCountries() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Notify loading state on the main thread
                _countriesState.value = UiStates.LOADING

                // Fetch data in IO thread and update success state
                val countries = withContext(Dispatchers.IO) {
                    countriesRepo.fetchData()
                }
                _countriesState.value = UiStates.SUCCESS(countries)
            } catch (ex: Exception) {
                // Handle error state on the main thread
                _countriesState.value = UiStates.ERROR(ex.message ?: "Unknown error")
            }
        }
    }




    sealed class UiStates {
        data object LOADING : UiStates()
        data class SUCCESS(var countriesModel: CountriesModel) : UiStates()
        data class ERROR(var error : String) : UiStates()
        data object INITIAL : UiStates()
    }
}