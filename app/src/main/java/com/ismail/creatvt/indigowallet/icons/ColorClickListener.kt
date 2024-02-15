package com.ismail.creatvt.indigowallet.icons

import android.view.View
import com.ismail.creatvt.indigowallet.utility.ColorFactory

interface ColorClickListener {
    fun onColorClick(view: View, color: ColorFactory.Color)
}