package com.ismail.creatvt.indigowallet.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ismail.creatvt.indigowallet.db.dao.CategoryDao
import com.ismail.creatvt.indigowallet.db.dao.CommonDao
import com.ismail.creatvt.indigowallet.db.dao.TransactionWalletDao
import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.utility.DateConverter
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction
import com.ismail.creatvt.indigowallet.db.entity.Wallet

@TypeConverters(DateConverter::class)
@Database(
    version = 6,
    entities = [MoneyTransaction::class, Wallet::class, Category::class],
    exportSchema = false
)
abstract class IndigoWalletDatabase : RoomDatabase() {

    abstract fun transactionWalletDao(): TransactionWalletDao

    abstract fun categoryDao(): CategoryDao

    abstract fun commonDao(): CommonDao

    companion object {
        private var instance: IndigoWalletDatabase? = null

        fun getInstance(context: Context): IndigoWalletDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    IndigoWalletDatabase::class.java,
                    "indigowallet.db"
                ).fallbackToDestructiveMigration().build()
            }
            return instance!!
        }
    }
}