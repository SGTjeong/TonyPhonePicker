package com.example.tonyccpicker

import androidx.recyclerview.widget.DiffUtil

class CountryInfoDiffUtilCallback : DiffUtil.ItemCallback<CountryInfo>() {
    override fun areItemsTheSame(oldItem: CountryInfo, newItem: CountryInfo): Boolean {
        return oldItem.nameCode == newItem.nameCode
    }

    override fun areContentsTheSame(oldItem: CountryInfo, newItem: CountryInfo): Boolean {
        return oldItem.nameCode == newItem.nameCode
    }
}