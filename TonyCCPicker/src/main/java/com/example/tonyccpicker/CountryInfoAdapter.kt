package com.example.tonyccpicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tonyccpicker.databinding.ItemCountryBinding

class CountryInfoAdapter : ListAdapter<CountryInfo, CountryInfoAdapter.ViewHolder>(CountryInfoDiffUtilCallback()) {
    private var listener : OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {countryInfo ->
            holder.bind(countryInfo)
            holder.itemView.setOnClickListener {
                listener?.onItemClick(countryInfo)
            }
        }
    }

    class ViewHolder(val binding : ItemCountryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(country : CountryInfo) {
            binding.ivFlag.setImageResource(CountryInfo.getFlagMasterResID(country))
            binding.tvName.text = country.name
            binding.tvCode.text = "+" + country.phoneCode
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun setOnItemClickListener(listener : (CountryInfo) -> Unit){
        this.listener = object : OnItemClickListener {
            override fun onItemClick(country : CountryInfo) {
                listener(country)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(country : CountryInfo)
    }
}