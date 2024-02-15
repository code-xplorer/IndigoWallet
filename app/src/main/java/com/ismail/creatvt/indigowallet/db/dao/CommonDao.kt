package com.ismail.creatvt.indigowallet.db.dao

import androidx.room.*
import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransactionWithWallet
import com.ismail.creatvt.indigowallet.db.entity.Wallet

@Dao
interface CommonDao {

    @Insert
    suspend fun insert(transaction: MoneyTransaction)

    @Update
    suspend fun update(transaction: MoneyTransaction)

    @Delete
    suspend fun delete(category: Category)

    @Update
    suspend fun update(wallet: Wallet)

    @Insert
    suspend fun insert(wallet: Wallet)

    @Delete
    suspend fun delete(transaction: MoneyTransaction)

    @Query("delete from wallet where wallet_id=:walletId")
    suspend fun delete(walletId: Int)

    @Transaction
    @Query("select * from moneytransaction where categoryId = :categoryId")
    suspend fun getAllTransactionsForCategory(categoryId: Int):List<MoneyTransactionWithWallet>

    @Query("delete from category where category_id = :categoryId")
    suspend fun deleteCategory(categoryId: Int)

    @Transaction
    suspend fun deleteCategoryAndTransactions(categoryId:Int){
        val transactions = getAllTransactionsForCategory(categoryId)
        for(transaction in transactions){
            deleteTransaction(transaction)
        }
        deleteCategory(categoryId)
    }

    @Transaction
    suspend fun deleteTransaction(
        transaction: MoneyTransactionWithWallet
    ) {
        delete(transaction.transaction)
        if (transaction.transaction.type == MoneyTransaction.TYPE_TRANSFER && transaction.toWallet?.id == transaction.fromWallet?.id) return

        if (transaction.fromWallet != null) {
            transaction.fromWallet.balance += transaction.transaction.amount
            update(transaction.fromWallet)
        }
        if (transaction.toWallet != null) {
            transaction.toWallet.balance -= transaction.transaction.amount
            update(transaction.toWallet)
        }
    }

    @Transaction
    suspend fun newTransaction(
        transaction: MoneyTransaction,
        fromWallet: Wallet?,
        toWallet: Wallet?
    ) {
        // Anything inside this method runs in a single transaction.
        insert(transaction)

        when (transaction.type) {
            MoneyTransaction.TYPE_INCOME -> if (toWallet != null) {
                toWallet.balance += transaction.amount
                update(toWallet)
            }
            MoneyTransaction.TYPE_EXPENSE -> if (fromWallet != null) {
                fromWallet.balance -= transaction.amount
                update(fromWallet)
            }
            MoneyTransaction.TYPE_TRANSFER -> if (toWallet != null && fromWallet != null && fromWallet.id != toWallet.id) {
                toWallet.balance += transaction.amount
                fromWallet.balance -= transaction.amount
                update(toWallet)
                update(fromWallet)
            }
        }
    }

    @Transaction
    suspend fun updateTransaction(
        transaction: MoneyTransaction,
        initialAmount: Float,
        fromWallet: Wallet?,
        toWallet: Wallet?,
        initialFromWallet: Wallet?,
        initialToWallet: Wallet?,
        initialType: Int
    ) {
        update(transaction)

        //Revert the old transaction
        when (initialType) {
            MoneyTransaction.TYPE_INCOME -> if (initialToWallet != null) {
                initialToWallet.balance -= initialAmount
                update(initialToWallet)
            }
            MoneyTransaction.TYPE_EXPENSE -> if (initialFromWallet != null) {
                initialFromWallet.balance += initialAmount
                update(initialFromWallet)
            }
            MoneyTransaction.TYPE_TRANSFER -> if (initialToWallet != null && initialFromWallet != null && initialFromWallet.id != initialToWallet.id) {
                initialToWallet.balance -= initialAmount
                initialFromWallet.balance += initialAmount
                update(initialToWallet)
                update(initialFromWallet)
            }
        }

        //Add the new one
        when (transaction.type) {
            MoneyTransaction.TYPE_INCOME -> if (toWallet != null) {
                toWallet.balance += transaction.amount
                update(toWallet)
            }
            MoneyTransaction.TYPE_EXPENSE -> if (fromWallet != null) {
                fromWallet.balance -= transaction.amount
                update(fromWallet)
            }
            MoneyTransaction.TYPE_TRANSFER -> if (toWallet != null && fromWallet != null && fromWallet.id != toWallet.id) {
                toWallet.balance += transaction.amount
                fromWallet.balance -= transaction.amount
                update(toWallet)
                update(fromWallet)
            }
        }
    }

    @Transaction
    @Query("select * from moneytransaction where toWallet = :walletId or fromWallet = :walletId")
    suspend fun getAllTransactionsWithWalletFor(walletId: Int):List<MoneyTransactionWithWallet>

    @Transaction
    suspend fun deleteWalletAndTransactions(walletId: Int) {
        val transactions = getAllTransactionsWithWalletFor(walletId)
        for(transaction in transactions){
            deleteTransaction(transaction)
        }
        delete(walletId)
    }
}