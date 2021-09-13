package com.mobiquity.assesment.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiquity.assesment.R
import com.mobiquity.assesment.service.database.entity.City
import com.mobiquity.assesment.utils.setTextOrHide
import kotlinx.android.synthetic.main.item_city.view.*

class CityAdapter(
    private val clickCallback: (Boolean, City) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private val items = ArrayList<City>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(city: City) {
            with(itemView) {
                tvCityName.setTextOrHide(city.cityName)
                setOnClickListener {
                    clickCallback(false, city)
                }
                ivDelete.setOnClickListener {
                    clickCallback(true, city)
                }
            }
        }
    }

    fun updateData(data: MutableList<City>) {
        items.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }
}