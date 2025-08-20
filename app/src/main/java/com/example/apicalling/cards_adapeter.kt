package com.example.apicalling

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.text.util.LocalePreferences

class cards_adapeter(val context: Context,val list: List<Day>) : BaseAdapter() {
    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any? {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        TODO("Not yet implemented")

        val pos= list[position]
    }


}