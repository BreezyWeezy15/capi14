package com.app.countriespro

import android.os.Bundle
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

        // Restore the UI state if it was saved
        savedInstanceState?.let {
            val errorText = it.getString("errorText")
            val showProgressBar = it.getBoolean("showProgressBar")
            binding.progressBar.visibility = if (showProgressBar) View.VISIBLE else View.GONE
            binding.errorTxt.text = errorText
            binding.errorTxt.visibility = if (errorText != null) View.VISIBLE else View.GONE
        }

        // Fetch data
        countriesViewModel.fetchRemoteJson()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save UI state
        outState.putBoolean("showProgressBar", binding.progressBar.visibility == View.VISIBLE)
        outState.putString("errorText", binding.errorTxt.text.toString())
    }
}