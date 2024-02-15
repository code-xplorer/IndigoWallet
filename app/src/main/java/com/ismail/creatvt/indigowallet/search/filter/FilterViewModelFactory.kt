package com.ismail.creatvt.indigowallet.search.filter

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.db.entity.TransactionType
import com.ismail.creatvt.indigowallet.db.entity.Wallet
import java.util.*
import kotlin.collections.ArrayList

class FilterViewModelFactory(
    val application: Application,
    var initialCategories: ArrayList<Category>,
    var initialWallets: ArrayList<Wallet>,
    var initialTypes: List<TransactionType>,
    var selectedDateStart: Date,
    var selectedDateEnd: Date
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == FilterViewModel::class.java) {
            return FilterViewModel(
                application,
                initialCategories,
                initialWallets,
                initialTypes,
                selectedDateStart,
                selectedDateEnd
            ) as T
        } else if (modelClass == DateFilterViewModel::class.java) {
            return DateFilterViewModel(
                application,
                selectedDateStart,
                selectedDateEnd
            ) as T
        }
        return super.create(modelClass)
    }

}