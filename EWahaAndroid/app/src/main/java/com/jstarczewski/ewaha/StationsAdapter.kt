package com.jstarczewski.ewaha

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StationsAdapter : RecyclerView.Adapter<StationViewHolder>() {

    private var items: ArrayList<Marker> = arrayListOf()

    fun updateItems(items: List<Marker>) {
        this.items.clear()
        this.items.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_station, parent, false)
        return StationViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.distanceTextView.text = "Odległość : ${items[position].distance.toString()}"
        holder.priceTextView.text = items[position].station.prices.toString()
    }
}