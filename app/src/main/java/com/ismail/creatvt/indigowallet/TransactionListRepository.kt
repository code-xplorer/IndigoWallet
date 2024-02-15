package com.ismail.creatvt.indigowallet

import android.view.View
import androidx.lifecycle.LiveData
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransactionWithWallet

interface TransactionListRepository {

    val transactions: LiveData<List<MoneyTransactionWithWallet>>

    fun onTransactionClick(view: View, transaction: MoneyTransactionWithWallet)

}