package com.ismail.creatvt.indigowallet.search.filter

import android.app.Application
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_EXPENSE
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_TRANSFER
import com.ismail.creatvt.indigowallet.db.entity.TransactionType
import com.ismail.creatvt.indigowallet.db.entity.Wallet
import com.ismail.creatvt.indigowallet.utility.END_DATE
import com.ismail.creatvt.indigowallet.utility.START_DATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FilterViewModel(
    application: Application,
    private var initialCategories: List<Category>,
    private var initialWallets: List<Wallet>,
    private var initialTypes: List<TransactionType>,
    private var selectedDateStart: Date,
    private var selectedDateEnd: Date
) : AndroidViewModel(application), OnDateFilterDoneListener {

    var viewContract: ViewContract? = null
    var listener: OnFilterDoneListener? = null

    var dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)

    val db = IndigoWalletDatabase.getInstance(application)

    private var selectedCategories = ArrayList(initialCategories)
    private var selectedWallets = ArrayList(initialWallets)
    private var selectedTypes = ArrayList(initialTypes)

    private val selectDateString = application.getString(R.string.select_date_range)
    var selectDateMessage = MutableLiveData(selectDateString)

    private val selectTypesString = application.getString(R.string.select_types)
    var selectTypesMessage = MutableLiveData(selectTypes())

    private val selectCategoriesString = application.getString(R.string.select_categories)
    var selectCategoriesMessage = MutableLiveData(selectCategories())

    private val selectWalletsString = application.getString(R.string.select_wallets)
    var selectWalletsMessage = MutableLiveData(selectWallets())

    init {
        CoroutineScope(IO).launch {
            initialCategories = db.categoryDao().getAllCategoriesSnapshot()
            initialWallets = db.transactionWalletDao().getAllWalletsSnapshot()
            initialTypes = arrayListOf(
                TransactionType(TYPE_INCOME, app.getString(R.string.income)),
                TransactionType(TYPE_EXPENSE, app.getString(R.string.expense)),
                TransactionType(TYPE_TRANSFER, app.getString(R.string.transfer))
            )
        }
    }

    private val app: Application
        get() = getApplication()

    fun onDoneClick(view: View) {
        listener?.onFilterDone(
            selectedDateStart.time,
            selectedDateEnd.time,
            selectedCategories,
            selectedWallets,
            selectedTypes
        )
        viewContract?.dismiss()
    }

    fun onCancelClick(view: View) {
        viewContract?.dismiss()
    }

    fun onSelectDateClicked(view: View) {
        viewContract?.showDialogFragment(
            DateFilterDialogFragment(this).apply {
                arguments = bundleOf(
                    START_DATE to selectedDateStart.time,
                    END_DATE to selectedDateEnd.time
                )
            },
            "DateFilterDialog"
        )
    }

    fun onSelectTypesClicked(view: View) {
        val checkedItems = initialTypes.map {
            selectedTypes.contains(it)
        }.toBooleanArray()
        val displayTypes = initialTypes.map {
            it.text
        }.toTypedArray()
        val tempSelectedTypes = ArrayList(selectedTypes)
        MaterialAlertDialogBuilder(view.context)
            .setTitle(R.string.select_types)
            .setMultiChoiceItems(displayTypes, checkedItems) { _, position, isChecked ->
                if (isChecked) {
                    tempSelectedTypes.add(initialTypes[position])
                } else {
                    tempSelectedTypes.remove(initialTypes[position])
                }
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.done) { dialog, action ->
                selectedTypes = tempSelectedTypes
                selectTypesMessage.value = selectTypes()
            }
            .show()
    }

    fun onSelectCategoriesClicked(view: View) {
        val checkedItems = initialCategories.map {
            selectedCategories.contains(it)
        }.toBooleanArray()
        val displayCategories = initialCategories.map {
            it.name
        }.toTypedArray()
        val tempSelectedCategories = ArrayList(selectedCategories)
        MaterialAlertDialogBuilder(view.context)
            .setTitle(R.string.select_categories)
            .setMultiChoiceItems(displayCategories, checkedItems) { _, position, isChecked ->
                if (isChecked) {
                    tempSelectedCategories.add(initialCategories[position])
                } else {
                    tempSelectedCategories.remove(initialCategories[position])
                }
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.done) { dialog, action ->
                selectedCategories = tempSelectedCategories
                selectCategoriesMessage.value = selectCategories()
            }
            .show()
    }

    private fun selectCategories() = when (selectedCategories.size) {
        0 -> selectCategoriesString
        1 -> selectedCategories[0].name
        else -> app.resources.getQuantityString(
            R.plurals.selection_message,
            selectedCategories.size - 1,
            selectedCategories[0].name,
            selectedCategories.size - 1
        )
    }

    private fun selectWallets() = when (selectedWallets.size) {
        0 -> selectWalletsString
        1 -> selectedWallets[0].name
        else -> app.resources.getQuantityString(
            R.plurals.selection_message,
            selectedWallets.size - 1,
            selectedWallets[0].name,
            selectedWallets.size - 1
        )
    }

    private fun selectTypes() = when (selectedTypes.size) {
        0 -> selectTypesString
        1 -> selectedTypes[0].text
        else -> app.resources.getQuantityString(
            R.plurals.selection_message,
            selectedTypes.size - 1,
            selectedTypes[0].text,
            selectedTypes.size - 1
        )
    }

    fun onSelectWalletsClicked(view: View) {
        val checkedItems = initialWallets.map {
            selectedWallets.contains(it)
        }.toBooleanArray()
        val displayCategories = initialWallets.map {
            it.name
        }.toTypedArray()
        val tempSelectedWallets = ArrayList(selectedWallets)
        MaterialAlertDialogBuilder(view.context)
            .setTitle(R.string.select_categories)
            .setMultiChoiceItems(displayCategories, checkedItems) { _, position, isChecked ->
                if (isChecked) {
                    tempSelectedWallets.add(initialWallets[position])
                } else {
                    tempSelectedWallets.remove(initialWallets[position])
                }
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.done) { dialog, action ->
                selectedWallets = tempSelectedWallets
                selectWalletsMessage.value = selectWallets()
            }
            .show()
    }

    override fun onDateFilterDone(startDate: Long, endDate: Long) {
        selectedDateStart.time = startDate
        selectedDateEnd.time = endDate
        if (startDate == 0L && endDate == MaterialDatePicker.todayInUtcMilliseconds()) {
            selectDateMessage.value = selectDateString
        } else {
            selectDateMessage.value = app.getString(
                R.string.selected_date_range,
                dateFormat.format(selectedDateStart),
                dateFormat.format(selectedDateEnd)
            )
        }
    }

}