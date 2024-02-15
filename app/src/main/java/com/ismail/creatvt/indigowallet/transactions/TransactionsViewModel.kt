package com.ismail.creatvt.indigowallet.transactions

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ismail.creatvt.indigowallet.TransactionListRepository
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransactionWithWallet
import com.ismail.creatvt.indigowallet.transactions.info.TransactionInfoDialogFragment
import com.ismail.creatvt.indigowallet.transactions.summary.TransactionSummary

class TransactionsViewModel(application: Application) : AndroidViewModel(application), TransactionListRepository {

    private val db = IndigoWalletDatabase.getInstance(application)

    val transactionSummary = TransactionSummary.forCurrentMonth(db)
    val wallets = db.transactionWalletDao().getAllWallets()

    var viewContract: ViewContract? = null

    private var _isFullSummaryVisible = MutableLiveData(false)
    val isFullSummaryVisible: LiveData<Boolean>
        get() = _isFullSummaryVisible

    override val transactions = db.transactionWalletDao().getAllTransactionsWithWallet()

    override fun onTransactionClick(view: View, transaction: MoneyTransactionWithWallet) {
        viewContract?.showDialogFragment(
            TransactionInfoDialogFragment(transaction.transaction.id),
            "TransactionInfo:${transaction.transaction.name}"
        )
    }

    fun onSummaryClick(){
        _isFullSummaryVisible.value = _isFullSummaryVisible.value != true
    }

}