package com.ismail.creatvt.indigowallet.transactions.info

import android.app.Application
import android.content.Intent
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.addtransaction.AddEditTransactionActivity
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.utility.TRANSACTION_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionInfoViewModel(application: Application, val transactionId: Int) :
    AndroidViewModel(application) {

    private val db = IndigoWalletDatabase.getInstance(application)
    private val dao = db.transactionWalletDao()
    val transaction = dao.getTransactionWithWallet(transactionId)

    var deleteListener: OnDeleteListener? = null

    var viewContract: ViewContract? = null

    fun onEditClick(view: View) {
        viewContract?.dismiss()

        view.context.apply {
            val intent = Intent(this, AddEditTransactionActivity::class.java)
            intent.putExtra(TRANSACTION_ID, transactionId)
            startActivity(intent)
        }
    }

    fun onDeleteClick(view: View) {
        viewContract?.dismiss()

        MaterialAlertDialogBuilder(view.context)
            .setMessage(R.string.delete_transaction_confirmation)
            .setPositiveButton(R.string.yes) { _, _ ->
                val moneyTransaction = transaction.value ?: return@setPositiveButton
                CoroutineScope(IO).launch {
                    db.commonDao().deleteTransaction(moneyTransaction)
                    withContext(Main){
                        deleteListener?.onDelete()
                    }
                }
            }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    interface OnDeleteListener {
        fun onDelete()
    }
}