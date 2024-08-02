package com.app.countriespro.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.countriespro.R
import com.app.countriespro.models.CountriesModel

class CountriesAdapter(private var countriesList: CountriesModel) :
    RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countriesList[position]
        "${country.name}, ${country.region} ${country.code}".also { holder.countryInfo.text = it }
        holder.countryCapital.text = country.capital
    }

    override fun getItemCount(): Int = countriesList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateCountries(newCountriesList: CountriesModel) {
        countriesList = newCountriesList
        notifyDataSetChanged()
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryInfo: TextView = itemView.findViewById(R.id.country_info)
        val countryCapital: TextView = itemView.findViewById(R.id.country_capital)
    }
}