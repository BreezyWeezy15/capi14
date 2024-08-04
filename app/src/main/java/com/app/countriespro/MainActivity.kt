package com.app.countriespro

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.countriespro.adapters.CountriesAdapter
import com.app.countriespro.databinding.ActivityMainBinding
import com.app.countriespro.models.CountriesModel
import com.app.countriespro.viewmodels.CountriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val countriesViewModel: CountriesViewModel by viewModels()
    private lateinit var adapter: CountriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the RecyclerView
        binding.rv.layoutManager = LinearLayoutManager(this)
        adapter = CountriesAdapter(CountriesModel())
        binding.rv.adapter = adapter

        // Observe ViewModel state
        lifecycleScope.launch {
            countriesViewModel.countriesState.collectLatest { state ->
                when (state) {
                    is CountriesViewModel.UiStates.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is CountriesViewModel.UiStates.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.updateCountries(state.data)
                    }
                    is CountriesViewModel.UiStates.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorTxt.visibility = View.VISIBLE
                        binding.errorTxt.text = state.error
                    }
                    else -> {}
                }
            }
        }
    }

    
}