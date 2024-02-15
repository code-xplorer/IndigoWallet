package com.ismail.creatvt.indigowallet.utility

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

val View.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun <T : ViewDataBinding> ViewGroup.inflate(layoutRes: Int) = DataBindingUtil.inflate<T>(
    layoutInflater,
    layoutRes,
    this,
    false
)

fun View.getColor(colorRes: Int): Int {
    return ResourcesCompat.getColor(resources, colorRes, context.theme)
}