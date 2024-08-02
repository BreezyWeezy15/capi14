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

    private lateinit var binding : ActivityMainBinding
    private  val countriesViewModel: CountriesViewModel by viewModels()
    private lateinit var adapter: CountriesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding.rv.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with an empty list
        adapter = CountriesAdapter(CountriesModel())
        binding.rv.adapter = adapter

        countriesViewModel.getCountries()
        lifecycleScope.launch {
            countriesViewModel.countriesState.collectLatest {
                when(it){
                    is CountriesViewModel.UiStates.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is CountriesViewModel.UiStates.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.updateCountries(it.countriesModel)
                    }
                    is CountriesViewModel.UiStates.ERROR -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorTxt.visibility = View.GONE
                        binding.errorTxt.text = it.error
                    }
                    else -> {}
                }
            }
        }

    }
}