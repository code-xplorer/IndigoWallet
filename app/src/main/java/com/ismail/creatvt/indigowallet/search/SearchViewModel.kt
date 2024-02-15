package com.ismail.creatvt.indigowallet.search

import android.app.Application
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.datepicker.MaterialDatePicker
import com.ismail.creatvt.indigowallet.TransactionListRepository
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.db.entity.Category.Companion.CATEGORIES
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_EXPENSE
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_TRANSFER
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransactionWithWallet
import com.ismail.creatvt.indigowallet.db.entity.TransactionType
import com.ismail.creatvt.indigowallet.db.entity.TransactionType.Companion.TYPES
import com.ismail.creatvt.indigowallet.db.entity.Wallet
import com.ismail.creatvt.indigowallet.db.entity.Wallet.Companion.WALLETS
import com.ismail.creatvt.indigowallet.search.filter.*
import com.ismail.creatvt.indigowallet.transactions.info.TransactionInfoDialogFragment
import com.ismail.creatvt.indigowallet.transactions.info.TransactionInfoViewModel
import com.ismail.creatvt.indigowallet.utility.END_DATE
import com.ismail.creatvt.indigowallet.utility.START_DATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application),
    OnFilterDoneListener, TransactionListRepository, TransactionInfoViewModel.OnDeleteListener,
    OnDateFilterDoneListener {

    val db = IndigoWalletDatabase.getInstance(application)

    private var walletsSnapshot = listOf<Wallet>()
    private var categoriesSnapshot = listOf<Category>()

    var viewContract: ViewContract? = null

    val filters = MutableLiveData(arrayListOf<Filter>())

    val query = MutableLiveData("")
    private val searchResults = MutableLiveData(listOf<MoneyTransactionWithWallet>())

    override val transactions: LiveData<List<MoneyTransactionWithWallet>>
        get() = searchResults

    init {
        CoroutineScope(IO).launch {
            walletsSnapshot = db.transactionWalletDao().getAllWalletsSnapshot()
            categoriesSnapshot = db.categoryDao().getAllCategoriesSnapshot()
            onSearchClick()
        }
    }

    fun onRemoveFilter(view: View, position: Int, filter: Filter) {
        val snapshot = filters.value ?: return
        snapshot.removeAt(position)
        filters.postValue(snapshot)
        onSearchClick()
    }

    fun onChipClick(view: View, position: Int, filter: Filter){
        if(filter is DateFilter){
            viewContract?.showDialogFragment(
                DateFilterDialogFragment(this).apply {
                    arguments = bundleOf(
                        START_DATE to filter.startDate,
                        END_DATE to filter.endDate
                    )
                },
                "DateFilterDialog"
            )
        }
    }

    override fun onTransactionClick(view: View, transaction: MoneyTransactionWithWallet) {
        viewContract?.showDialogFragment(
            TransactionInfoDialogFragment(transaction.transaction.id, this),
            "TransactionInfo:${transaction.transaction.name}"
        )
    }

    fun onSearchClick() {
        Log.d("SearchViewModel", "onSearchClicked")
        val filtersSnapshot = filters.value ?: arrayListOf()
        val (startDate, endDate) = if (filtersSnapshot.isEmpty() || filtersSnapshot[0] !is DateFilter) {
            Pair(0L, MaterialDatePicker.todayInUtcMilliseconds())
        } else {
            (filtersSnapshot[0] as DateFilter).datePair
        }

        val categoriesFilter = filtersSnapshot
            .filterIsInstance<CategoryFilter>()
        val categories = ArrayList(
            if(categoriesFilter.isEmpty()){
                categoriesSnapshot.map {
                    it.id
                }
            } else{
                categoriesFilter.map {
                    it.category.id
                }
            }
        )
        val walletsFilter = filtersSnapshot
            .filterIsInstance<WalletFilter>()

        val typesFilter = filtersSnapshot
            .filterIsInstance<TypeFilter>()

        val wallets = ArrayList(
            if(walletsFilter.isEmpty()){
                walletsSnapshot.map {
                    it.id
                }
            } else{
                walletsFilter.map {
                    it.wallet.id
                }
            }
        )

        val types = ArrayList(
            if(typesFilter.isEmpty()){
                listOf(
                    TYPE_INCOME,
                    TYPE_EXPENSE,
                    TYPE_TRANSFER
                )
            } else{
                typesFilter.map {
                    it.transactionType.id
                }
            }
        )

        CoroutineScope(IO).launch {
            val searchResult = if(types.contains(TYPE_TRANSFER)){
                db.transactionWalletDao().searchTransactionsWithTransfer(
                    "%${query.value?:""}%",
                    startDate,
                    endDate,
                    categories,
                    wallets,
                    types
                )
            } else{
                db.transactionWalletDao().searchTransactions(
                    "%${query.value?:""}%",
                    startDate,
                    endDate,
                    categories,
                    wallets,
                    types
                )
            }

            searchResults.postValue(searchResult)
        }
    }

    fun onFilterClick() {
        val fragment = FilterBottomSheetDialogFragment(this)

        val filtersSnapshot = filters.value ?: arrayListOf()
        val (startDate, endDate) = if (filtersSnapshot.isEmpty() || filtersSnapshot[0] !is DateFilter) {
            Pair(0L, MaterialDatePicker.todayInUtcMilliseconds())
        } else {
            (filtersSnapshot[0] as DateFilter).datePair
        }
        val categories = ArrayList(filtersSnapshot
            .filterIsInstance<CategoryFilter>()
            .map {
                it.category
            })
        val wallets = ArrayList(filtersSnapshot
            .filterIsInstance<WalletFilter>()
            .map {
                it.wallet
            })
        val types = ArrayList(filtersSnapshot
            .filterIsInstance<TypeFilter>()
            .map {
                it.transactionType
            })
        fragment.arguments = bundleOf(
            START_DATE to startDate,
            END_DATE to endDate,
            CATEGORIES to categories,
            WALLETS to wallets,
            TYPES to types
        )
        viewContract?.showDialogFragment(fragment, "FilterOptions")
    }

    override fun onFilterDone(
        selectedStartDate: Long,
        selectedEndDate: Long,
        categories: ArrayList<Category>,
        wallets: ArrayList<Wallet>,
        types: ArrayList<TransactionType>
    ) {
        val filterItems = arrayListOf<Filter>()
        filterItems.add(DateFilter(selectedStartDate, selectedEndDate))
        types.forEach {
            filterItems.add(TypeFilter(it))
        }
        categories.forEach {
            filterItems.add(CategoryFilter(it))
        }
        wallets.forEach {
            filterItems.add(WalletFilter(it))
        }
        filters.value = filterItems
        onSearchClick()
    }

    override fun onDelete() {
        onSearchClick()
    }

    override fun onDateFilterDone(startDate: Long, endDate: Long) {
        val dateFilter = DateFilter(startDate, endDate)
        val filtersSnapshot = filters.value?: arrayListOf()
        filtersSnapshot[0] = dateFilter
        filters.value = filtersSnapshot
        onSearchClick()
    }
}