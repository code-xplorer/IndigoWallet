package com.ismail.creatvt.indigowallet.addtransaction

import android.app.Application
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_EXPENSE
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_TRANSFER
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransactionWithWallet
import com.ismail.creatvt.indigowallet.db.entity.Wallet
import com.ismail.creatvt.indigowallet.utility.ColorFactory
import com.ismail.creatvt.indigowallet.utility.DateTimeFormatter
import com.ismail.creatvt.indigowallet.utility.mergeAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class AddEditTransactionViewModel(application: Application, transactionId: Int) :
    AndroidViewModel(application),
    AddEditTransactionEventListener {
    private val db = IndigoWalletDatabase.getInstance(application)

    lateinit var expenseCategories: List<Category>
    lateinit var incomeCategories: List<Category>

    lateinit var wallets: List<Wallet>

    val name = MutableLiveData("")
    val amount = MutableLiveData("")
    val incomeCategory = MutableLiveData("")
    val expenseCategory = MutableLiveData("")
    val date = MutableLiveData("")
    val time = MutableLiveData("")
    val description = MutableLiveData("")
    val fromWallet = MutableLiveData("")
    val toWallet = MutableLiveData("")

    val type = MutableLiveData(1)

    lateinit var transaction: MoneyTransaction

    private var initialToWallet: String = ""
    private var initialFromWallet: String = ""
    private var initialIncomeCategory: String = ""
    private var initialExpenseCategory: String = ""

    init {
        CoroutineScope(IO).launch {
            try {
                wallets = db.transactionWalletDao().getAllWalletsSnapshot()
                expenseCategories = db.categoryDao().getExpenseCategoriesSnapshot()
                incomeCategories = db.categoryDao().getIncomeCategoriesSnapshot()

                transaction = if (transactionId == -1) {
                    MoneyTransaction(
                        0,
                        "",
                        Date(),
                        0f,
                        -1,
                        1,
                        -1,
                        -1,
                        false,
                        ""
                    )
                } else {
                    db.transactionWalletDao().getTransactionSnapshot(transactionId)
                }
                val toWalletIndex = wallets.indexOfFirst {
                    it.id == transaction.toWallet
                }
                val fromWalletIndex = wallets.indexOfFirst {
                    it.id == transaction.fromWallet
                }
                when (transaction.type) {
                    TYPE_INCOME -> {
                        val cat = incomeCategories.firstOrNull {
                            it.id == transaction.categoryId
                        }
                        initialIncomeCategory = cat?.name ?: ""
                        incomeCategory.postValue(initialIncomeCategory)
                    }
                    TYPE_EXPENSE -> {
                        val cat = expenseCategories.firstOrNull {
                            it.id == transaction.categoryId
                        }
                        initialExpenseCategory = cat?.name ?: ""
                        expenseCategory.postValue(initialExpenseCategory)
                    }
                }

                name.postValue(transaction.name)
                amount.postValue(String.format("%.2f", transaction.amount))
                date.postValue(DateTimeFormatter.getFormattedDate(transaction.timestamp))
                time.postValue(DateTimeFormatter.getFormattedTime(transaction.timestamp))
                description.postValue(transaction.description)
                type.postValue(transaction.type)

                initialToWallet = if (toWalletIndex >= 0) wallets[toWalletIndex].name else ""
                toWallet.postValue(initialToWallet)
                initialFromWallet = if (fromWalletIndex >= 0) wallets[fromWalletIndex].name else ""
                fromWallet.postValue(initialFromWallet)
            } catch (e: Exception) {
                Log.d("AddEditTransactionVM", "Exception: ${e.message}")
            }
        }
    }

    val typeString = type.map {
        it.toString()
    }

    var viewContract: ViewContract? = null

    val isEnabled: LiveData<Boolean> = mergeAll(
        name,
        amount,
        incomeCategory,
        expenseCategory,
        date,
        time,
        fromWallet,
        toWallet,
        typeString
    ) {
        var index = -1
        var enabled = true

        val checkNullFor: (String?, Int?) -> Boolean = check@{ value, checkType ->
            if (type.value == checkType && value.isNullOrEmpty()) {
                enabled = false
                return@check false
            }
            return@check true
        }

        loop@ for (value in it) {
            index++
            when (index) {
                1 -> {
                    // if Amount is null or 0 then disable the save button
                    if ((value?.toFloatOrNull() ?: 0f) <= 0f) {
                        enabled = false
                        break@loop
                    }
                    continue@loop
                }
                //if selected type is income
                //and income category is null or empty then disable the save button
                2 -> if (checkNullFor(value, TYPE_INCOME)) continue@loop else break@loop
                //if selected type is expense
                //and expense category is null or empty then disable the save button
                3 -> if (checkNullFor(value, TYPE_EXPENSE)) continue@loop else break@loop
                //if selected type is expense
                //and from wallet is null or empty then disable the save button
                6 -> if (checkNullFor(value, TYPE_EXPENSE) && checkNullFor(
                        value,
                        TYPE_TRANSFER
                    )
                ) continue@loop else break@loop
                //if selected type is transfer or income
                //and to wallet is null or empty then disable the save button
                7 -> if (checkNullFor(value, TYPE_INCOME) && checkNullFor(
                        value,
                        TYPE_TRANSFER
                    )
                ) continue@loop else break@loop
                // if any other field is null then disable the save button
                else -> if (checkNullFor(value, type.value)) continue@loop else break@loop
            }
        }

        index = -1
        if (enabled && transactionId != -1) {
            //if we are editing an already existing field
            //then don't enable the save button unless there's some change
            var isDifferent = false
            loop@ for (value in it) {
                index++
                when (index) {
                    0 -> {
                        //name
                        if (transaction.name != value) {
                            Log.d("AddEditTransactionVM", "name is different : ${transaction.name} -> $value")
                            isDifferent = true
                            break@loop
                        }
                    }
                    1 -> {
                        //amount
                        if (transaction.amount != value?.toFloat()) {
                            Log.d("AddEditTransactionVM", "amount is different : ${transaction.amount} -> $value")
                            isDifferent = true
                            break@loop
                        }
                    }
                    2 -> {
                        //incomeCategory
                        if (initialIncomeCategory != value && type.value == TYPE_INCOME) {
                            Log.d("AddEditTransactionVM", "incomeCategory is different : $initialIncomeCategory -> $value")
                            isDifferent = true
                            break@loop
                        }
                    }
                    3 -> {
                        //expenseCategory
                        if (initialExpenseCategory != value && type.value == TYPE_EXPENSE) {
                            Log.d("AddEditTransactionVM", "expenseCategory is different : $expenseCategory -> $value")
                            isDifferent = true
                            break@loop
                        }
                    }
                    6 -> {
                        //fromWallet
                        if (initialFromWallet != value && type.value != TYPE_EXPENSE) {
                            Log.d("AddEditTransactionVM", "initialFromWallet is different : $initialFromWallet -> $value")
                            isDifferent = true
                            break@loop
                        }
                    }
                    7 -> {
                        //toWallet
                        if (initialToWallet != value && type.value != TYPE_INCOME) {
                            Log.d("AddEditTransactionVM", "toWallet is different : $toWallet -> $value")
                            isDifferent = true
                            break@loop
                        }
                    }
                    8 -> {
                        //typeString
                        if (transaction.type.toString() != value) {
                            Log.d("AddEditTransactionVM", "typeString is different : ${transaction.type} -> $value")
                            isDifferent = true
                            break@loop
                        }
                    }
                }
            }

            if (!isDifferent) {
                //if other fields are not different then check if the date is different
                val dateValue = it[4]
                val timeValue = it[5]
                if (dateValue != null && timeValue != null) {
                    val dateObject = DateTimeFormatter.getDate(dateValue, timeValue)
                    if (dateObject != transaction.timestamp) {
                        Log.d("AddEditTransactionVM", "date is different : ${DateTimeFormatter.getFormattedDate(transaction.timestamp)} ${DateTimeFormatter.getFormattedTime(transaction.timestamp)} -> $dateValue $timeValue")
                        isDifferent = true
                    }
                }
            }
            enabled = isDifferent
        }
        enabled
    }

    override fun onSaveClick(view: View) {
        val dateObject = DateTimeFormatter.getDate(date.value ?: "", time.value ?: "")
        val categoryString = when (type.value) {
            0 -> incomeCategory.value?.toString()
            1 -> expenseCategory.value?.toString()
            else -> null
        }

        val categoryId = if (categoryString == null) {
            -1
        } else {
            when (type.value) {
                TYPE_INCOME -> incomeCategories
                TYPE_EXPENSE -> expenseCategories
                else -> arrayListOf()
            }.firstOrNull { it.name == categoryString }?.id
        }

        val toWalletObj: Wallet? =
            toWallet.value?.let { if (it.isNotEmpty()) wallets.find { w -> w.name == it } else null }
        val fromWalletObj: Wallet? =
            fromWallet.value?.let { if (it.isNotEmpty()) wallets.find { w -> w.name == it } else null }

        val initialToWallet = wallets.find { w -> w.id == transaction.toWallet }
        val initialFromWallet = wallets.find { w -> w.id == transaction.fromWallet }

        val initialType = transaction.type
        val initialAmount = transaction.amount

        val transactionAmount = amount.value?.toFloatOrNull() ?: 0f

        transaction.name = name.value?:""
        transaction.timestamp = dateObject
        transaction.amount = transactionAmount
        transaction.type = type.value?:0
        transaction.categoryId = categoryId?:-1
        transaction.fromWallet = fromWalletObj?.id ?: -1
        transaction.toWallet = toWalletObj?.id ?: -1
        transaction.hasList = false
        transaction.description = description.value ?: ""

        CoroutineScope(IO).launch {
            if(transaction.id == 0){
                IndigoWalletDatabase.getInstance(view.context).commonDao()
                    .newTransaction(transaction, fromWalletObj, toWalletObj)
            } else{
                IndigoWalletDatabase.getInstance(view.context).commonDao()
                    .updateTransaction(transaction, initialAmount, fromWalletObj, toWalletObj, initialFromWallet, initialToWallet, initialType)
            }
            viewContract?.dismiss()
        }
    }

    override fun onTimeClick(view: View) {
        val calendar = Calendar.getInstance()
        calendar.time = DateTimeFormatter.getTime(time.value ?: "")
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTitleText(R.string.transaction_time)
            .build()
        timePicker.addOnPositiveButtonClickListener {
            calendar.set(Calendar.HOUR, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)
            time.postValue(DateTimeFormatter.getFormattedTime(calendar.time))
        }
        viewContract?.showDialogFragment(timePicker, "TimePickerDialog")
    }

    override fun onDateClick(view: View) {
        val calendar = Calendar.getInstance()
        calendar.time = DateTimeFormatter.getDate(date.value ?: "")
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.transaction_date)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setSelection(calendar.timeInMillis)
            .build()
        datePicker.addOnPositiveButtonClickListener {
            calendar.timeInMillis = it
            date.postValue(DateTimeFormatter.getFormattedDate(calendar.time))
        }
        viewContract?.showDialogFragment(datePicker, "DatePickerDialog")
    }

}