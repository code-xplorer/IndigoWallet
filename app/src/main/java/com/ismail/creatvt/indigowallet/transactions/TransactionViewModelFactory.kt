package com.ismail.creatvt.indigowallet.transactions

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.addtransaction.AddEditTransactionViewModel
import com.ismail.creatvt.indigowallet.transactions.info.TransactionInfoViewModel

class TransactionViewModelFactory(val application: Application, val transactionId: Int): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TransactionInfoViewModel::class.java)){
            return TransactionInfoViewModel(application, transactionId) as T
        } else if(modelClass.isAssignableFrom(AddEditTransactionViewModel::class.java)){
            return AddEditTransactionViewModel(application, transactionId) as T
        }
        return super.create(modelClass)
    }
}