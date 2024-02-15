package com.ismail.creatvt.indigowallet.transactions.util

import androidx.recyclerview.widget.DiffUtil
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransactionWithWallet
import com.ismail.creatvt.indigowallet.transactions.summary.TransactionSummary

class TransactionsDiffUtil(val oldItems: List<Any>, val newItems: List<Any>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        if (oldItem is String && newItem is String) {
            return oldItem == newItem
        }
        if (oldItem is MoneyTransaction && newItem is MoneyTransaction) {
            return oldItem.id == newItem.id
        }
        if(oldItem is MoneyTransactionWithWallet && newItem is MoneyTransactionWithWallet){
            return oldItem.transaction.id == newItem.transaction.id
        }
        return oldItem is TransactionSummary && newItem is TransactionSummary
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        if (oldItem is String && newItem is String) {
            return oldItem == newItem
        }
        if (oldItem is MoneyTransactionWithWallet && newItem is MoneyTransactionWithWallet) {
            return oldItem.transaction.id == newItem.transaction.id &&
                    oldItem.transaction.amount == newItem.transaction.amount &&
                    oldItem.transaction.name == newItem.transaction.name &&
                    oldItem.transaction.toWallet == newItem.transaction.toWallet &&
                    oldItem.transaction.fromWallet == newItem.transaction.fromWallet &&
                    oldItem.transaction.timestamp == newItem.transaction.timestamp &&
                    oldItem.transaction.description == newItem.transaction.description &&
                    oldItem.transaction.type == newItem.transaction.type &&
                    oldItem.transaction.categoryId == newItem.transaction.categoryId &&
                    oldItem.transaction.hasList == newItem.transaction.hasList
        }
        return oldItem is TransactionSummary && newItem is TransactionSummary
    }
}