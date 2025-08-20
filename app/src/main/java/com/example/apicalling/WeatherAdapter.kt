package com.example.apicalling

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import kotlin.math.min


class WeatherAdapter(val context: Context, val weatherList: List<Day>,val description: String) : BaseAdapter() {
    override fun getCount(): Int = weatherList.size   // 👈 return size

    override fun getItem(position: Int): Any = weatherList[position]  // 👈 return item

    override fun getItemId(position: Int): Long = position.toLong()   // 👈 return id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.card, parent, false)
        val data = weatherList[position]

        val maxtemp = view.findViewById<TextView>(R.id.tvTempMaxValue)   // 👈 careful: you were binding wrong ID
        val mintemp = view.findViewById<TextView>(R.id.tvTempMinValue)
        val humidity = view.findViewById<TextView>(R.id.tvHumidityValue)
        val advice = view.findViewById<TextView>(R.id.tvAdviceValue)
        val curr=view.findViewById<TextView>(R.id.tvCurrentTempValue)


        curr.text = String.format("%.2f°C", (data.temp - 32) * 5.0 / 9.0)


        maxtemp.text = String.format("%.2f°C", (data.tempmax - 32) * 5.0 / 9.0)
        mintemp.text = String.format("%.2f°C", (data.tempmin - 32) * 5.0 / 9.0)
        humidity.text = data.humidity.toString() + "%"
        advice.text =  description
        val imageView: ImageView = view.findViewById(R.id.tip)







        return view
    }
}
