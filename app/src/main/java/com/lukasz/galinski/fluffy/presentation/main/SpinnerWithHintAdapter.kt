package com.lukasz.galinski.fluffy.presentation.main

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerWithHintAdapter(context: Context, items: Array<*>) :
    ArrayAdapter<Any>(context, android.R.layout.simple_spinner_item, items) {

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        return super.getDropDownView(position, convertView, parent) as TextView
    }
}

