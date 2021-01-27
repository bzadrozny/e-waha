package com.jstarczewski.ewaha

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val distanceTextView: TextView

    val priceTextView: TextView

    init {
        distanceTextView = view.findViewById(R.id.tvDistance)
        priceTextView = view.findViewById(R.id.tvPrices)
    }
}