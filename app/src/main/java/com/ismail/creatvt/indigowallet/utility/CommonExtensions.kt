package com.ismail.creatvt.indigowallet.utility

fun IntArray.indexOfOrNull(element: Int): Int? {
    val index = indexOf(element)
    return if (index == -1) null else index
}