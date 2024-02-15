package com.ismail.creatvt.indigowallet.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.transactions.summary.TransactionSummary

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = IndigoWalletDatabase.getInstance(application)

    val transactionSummary = TransactionSummary.forCurrentMonth(db)

}