package com.app.countriespro

import com.app.countriespro.models.CountriesModel
import com.app.countriespro.models.CountriesModelItem
import com.app.countriespro.models.Currency
import com.app.countriespro.models.Language
import com.app.countriespro.repositories.CountriesRepo
import com.app.countriespro.services.AuthService
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.contains
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matchers.containsInAnyOrder

class CountriesRepoTest {

    private lateinit var authService: AuthService
    private lateinit var countriesRepo: CountriesRepo

    @Before
    fun setUp() {
        authService = mock(AuthService::class.java)
        countriesRepo = CountriesRepo(authService)
    }

    @Test
    fun `fetchData should return CountriesModel from authService`(): Unit = runBlocking {
        // Given
        val values = CountriesModel().apply {
            add(
                CountriesModelItem(
                    capital = "Kabul",
                    code = "AF",
                    currency = Currency(code = "AFN", name = "Afghan afghani", symbol = "؋"),
                    demonym = "Afghan",
                    flag = "https://restcountries.eu/data/afg.svg",
                    language = Language(code = "ps", iso639_2 = "pus", name = "Pashto", nativeName = "پښتو"),
                    name = "Afghanistan",
                    region = "AS"
                )
            )
            // Add other countries here...
            add(
                CountriesModelItem(
                    capital = "Vienna",
                    code = "AT",
                    currency = Currency(code = "EUR", name = "Euro", symbol = "€"),
                    demonym = "Austrian",
                    flag = "https://restcountries.eu/data/aut.svg",
                    language = Language(code = "de", iso639_2 = "deu", name = "German", nativeName = "Deutsch"),
                    name = "Austria",
                    region = "EU"
                )
            )
        }
        `when`(authService.fetchCountries()).thenReturn(values)

        // When
        val result = countriesRepo.fetchData()

        assertThat(result, containsInAnyOrder(*values.toTypedArray()))
        assertEquals(values, result)
        verify(authService).fetchCountries()  // Ensure fetchCountries was called
    }


}