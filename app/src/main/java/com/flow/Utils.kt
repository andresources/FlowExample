package com.flow

import android.view.View
import android.widget.TextView

fun TextView.visible(fg: Boolean){
    if(fg) visibility = View.VISIBLE else visibility = View.GONE
}