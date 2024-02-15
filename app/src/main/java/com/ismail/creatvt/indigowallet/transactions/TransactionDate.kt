package com.ismail.creatvt.indigowallet.transactions

import java.text.SimpleDateFormat
import java.util.*

class TransactionDate(date: Date){
    private val dateFormatter = SimpleDateFormat("dd", Locale.getDefault())
    private val monthFormatter = SimpleDateFormat("MMM", Locale.getDefault())
    private val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
    val dateString: String = dateFormatter.format(date)
    val monthString: String = monthFormatter.format(date)
    val yearString: String = yearFormatter.format(date)
}