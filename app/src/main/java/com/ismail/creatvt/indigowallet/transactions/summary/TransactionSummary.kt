package com.ismail.creatvt.indigowallet.transactions.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import java.util.*

class TransactionSummary(
    _totalIncome: LiveData<Float?>,
    _totalExpense: LiveData<Float?>,
    _totalBalance: LiveData<Float?>,
    _totalTransfer: LiveData<Float?>
) {

    val totalIncome: LiveData<Float> = _totalIncome.map {
        it ?: 0f
    }
    val totalExpense: LiveData<Float> = _totalExpense.map {
        it ?: 0f
    }
    val totalBalance: LiveData<Float> = _totalBalance.map {
        it ?: 0f
    }
    val totalTransfer: LiveData<Float> = _totalBalance.map {
        it ?: 0f
    }

    companion object{
        fun forCurrentMonth(db: IndigoWalletDatabase): TransactionSummary {
            val calendar = Calendar.getInstance()
            val currentMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
            val totalIncome = db.transactionWalletDao().getTotalIncome(currentMonth)
            val totalExpense = db.transactionWalletDao().getTotalExpense(currentMonth)
            val totalBalance = db.transactionWalletDao().getTotalBalance()
            val totalTransfer = db.transactionWalletDao().getTotalTransfer(currentMonth)

            return TransactionSummary(totalIncome, totalExpense, totalBalance, totalTransfer)
        }
    }

}