package com.ismail.creatvt.indigowallet.search.filter

import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.db.entity.TransactionType
import com.ismail.creatvt.indigowallet.db.entity.Wallet

interface OnFilterDoneListener {

    fun onFilterDone(
        selectedStartDate: Long,
        selectedEndDate: Long,
        categories: ArrayList<Category>,
        wallets: ArrayList<Wallet>,
        types: ArrayList<TransactionType>
    )

}