package com.app.countriespro

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.app.countriespro.models.CountriesModel
import com.app.countriespro.repositories.CountriesRepoImpl
import com.app.countriespro.viewmodels.CountriesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MyViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var countriesRepo: CountriesRepoImpl

    private lateinit var viewModel: MyViewModel

    @Mock
    private lateinit var uiStatesObserver: Observer<UiStates>

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MyViewModel(countriesRepo)
        viewModel.countriesState.observeForever(uiStatesObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        viewModel.countriesState.removeObserver(uiStatesObserver)
    }

    @Test
    fun `getCountries should post loading state first`() = runBlockingTest {
        viewModel.getCountries()

        Mockito.verify(uiStatesObserver).onChanged(UiStates.LOADING)
    }

    @Test
    fun `getCountries should post success state when data is fetched successfully`() = runBlockingTest {
            val countriesModel = CountriesModel() // replace with actual data
            `when`(countriesRepo.fetchData()).thenReturn(countriesModel)

            viewModel.getCountries()

            advanceUntilIdle() // Ensure all coroutines have completed

            val inOrder = Mockito.inOrder(uiStatesObserver)
            inOrder.verify(uiStatesObserver).onChanged(UiStates.LOADING)
            inOrder.verify(uiStatesObserver).onChanged(UiStates.SUCCESS(countriesModel))
        }

}

