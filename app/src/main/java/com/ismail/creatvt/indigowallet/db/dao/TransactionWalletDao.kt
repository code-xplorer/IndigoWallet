package com.ismail.creatvt.indigowallet.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_EXPENSE
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_TRANSFER
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransactionWithWallet
import com.ismail.creatvt.indigowallet.db.entity.Wallet

@Dao
interface TransactionWalletDao {

    @Insert
    suspend fun insert(transaction: MoneyTransaction)

    @Update
    suspend fun update(transaction: MoneyTransaction)

    @Delete
    suspend fun delete(transaction: MoneyTransaction)

    @Query("select * from moneytransaction where type=$TYPE_INCOME")
    fun getAllIncome(): LiveData<List<MoneyTransaction>>

    @Query("select * from moneytransaction where type=$TYPE_EXPENSE")
    fun getAllExpense(): LiveData<List<MoneyTransaction>>

    @Query("select * from moneytransaction where type=$TYPE_TRANSFER")
    fun getAllTransfers(): LiveData<List<MoneyTransaction>>

    @Query("select * from moneytransaction order by timestamp desc")
    fun getAllTransactions(): LiveData<List<MoneyTransaction>>

    // Where month should be 01-12 for Jan-Dec respectively
    @Query("select sum(amount) from moneytransaction where type=$TYPE_INCOME and strftime(\"%m\")=:month")
    fun getTotalIncome(month: String): LiveData<Float?>

    @Query("select sum(amount) from moneytransaction where type=$TYPE_EXPENSE and strftime(\"%m\")=:month")
    fun getTotalExpense(month: String): LiveData<Float?>

    @Query("select sum(amount) from moneytransaction where type=$TYPE_TRANSFER and strftime(\"%m\")=:month")
    fun getTotalTransfer(month: String): LiveData<Float?>

    @Transaction
    @Query("select * from moneytransaction order by timestamp desc")
    fun getAllTransactionsWithWallet(): LiveData<List<MoneyTransactionWithWallet>>

    @Transaction
    @Query("select * from moneytransaction where id=:id")
    fun getTransactionWithWallet(id: Int): LiveData<MoneyTransactionWithWallet>

    @Query("select * from moneytransaction where id=:id")
    suspend fun getTransactionSnapshot(id: Int): MoneyTransaction

    @Query("select * from wallet")
    fun getAllWallets(): LiveData<List<Wallet>>

    @Query("select * from wallet")
    suspend fun getAllWalletsSnapshot(): List<Wallet>

    @Query("select sum(wallet_balance) from wallet")
    fun getTotalBalance(): LiveData<Float?>

    @Insert
    suspend fun insert(wallet: Wallet)

    @Update
    suspend fun update(wallet: Wallet)

    @Query("select count(wallet_id) from wallet")
    suspend fun getWalletCount(): Int

    @Query("select * from wallet where wallet_id = :walletId")
    fun getWallet(walletId: Int): LiveData<Wallet>

    @Transaction
    @Query("""select * from moneytransaction 
        where (name like :searchQuery or
               description like :searchQuery) 
        and timestamp >= :startDate 
        and timestamp <= :endDate 
        and (categoryId in (:categories) or type = $TYPE_TRANSFER) 
        and type in (:types)
        and (fromWallet in (:wallets) 
            or toWallet in (:wallets))""")
    suspend fun searchTransactionsWithTransfer(
        searchQuery: String,
        startDate:Long,
        endDate:Long,
        categories:List<Int>,
        wallets:List<Int>,
        types:ArrayList<Int>
    ):List<MoneyTransactionWithWallet>

    @Transaction
    @Query("""select * from moneytransaction 
        where (name like :searchQuery or
               description like :searchQuery) 
        and timestamp >= :startDate 
        and timestamp <= :endDate 
        and categoryId in (:categories)
        and type in (:types)
        and (fromWallet in (:wallets) 
            or toWallet in (:wallets))""")
    suspend fun searchTransactions(
        searchQuery: String,
        startDate:Long,
        endDate:Long,
        categories:List<Int>,
        wallets:List<Int>,
        types:ArrayList<Int>
    ):List<MoneyTransactionWithWallet>

}