package com.ismail.creatvt.indigowallet.search.filter

import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.db.entity.TransactionType
import com.ismail.creatvt.indigowallet.db.entity.Wallet
import java.text.SimpleDateFormat
import java.util.*

interface Filter{
    val text: String
}

class CategoryFilter(val category: Category):Filter{
    override val text: String = category.name
}

class WalletFilter(val wallet: Wallet):Filter{
    override val text: String = wallet.name
}

class TypeFilter(val transactionType: TransactionType): Filter{
    override val text: String = transactionType.text
}

class DateFilter(val startDate:Long, val endDate: Long): Filter{
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)

    val datePair = Pair(startDate, endDate)

    override val text: String =
        "${dateFormat.format(Date(startDate))} - ${dateFormat.format(Date(endDate))}"
}