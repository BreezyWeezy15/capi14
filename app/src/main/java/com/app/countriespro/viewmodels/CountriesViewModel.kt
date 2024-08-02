package com.app.countriespro.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.countriespro.models.CountriesModel
import com.app.countriespro.repositories.CountriesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private var countriesRepo: CountriesRepo
) : ViewModel(){


    private val _countriesState : MutableStateFlow<UiStates> = MutableStateFlow(UiStates.INITIAL)
    val countriesState : StateFlow<UiStates> get() = _countriesState


    fun getCountries(){
        viewModelScope.launch {
            _countriesState.value = UiStates.LOADING
            try {
                countriesRepo.fetchData().collectLatest {
                    _countriesState.value = UiStates.SUCCESS(it)
                }
            } catch (ex : Exception){
                _countriesState.value = UiStates.ERROR(ex.message!!)
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