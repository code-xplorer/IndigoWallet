package com.ismail.creatvt.indigowallet.search.filter

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.ViewContract
import java.text.SimpleDateFormat
import java.util.*

class DateFilterViewModel(
    application: Application,
    private var selectedDateStart: Date,
    private var selectedDateEnd: Date
) : AndroidViewModel(application) {

    var dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)

    var selectedDateStartString = MutableLiveData(dateFormat.format(selectedDateStart))
    var selectedDateEndString = MutableLiveData(dateFormat.format(selectedDateEnd))

    var onDateFilterDoneListener: OnDateFilterDoneListener? = null

    var viewContract: ViewContract? = null

    fun onSelectStartDate(view: View) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.start_date)
            .setSelection(selectedDateStart.time)
            .setCalendarConstraints(
                CalendarConstraints
                    .Builder()
                    .setValidator(DateValidatorPointBackward.before(selectedDateEnd.time))
                    .build()
            )
            .build()

        picker.addOnPositiveButtonClickListener {
            selectedDateStart.time = it
            formatStartDate()
        }
        viewContract?.showDialogFragment(picker, "SelectStartDate")
    }

    private fun formatStartDate() {
        selectedDateStartString.postValue(dateFormat.format(selectedDateStart))
    }

    private fun formatEndDate() {
        selectedDateEndString.postValue(dateFormat.format(selectedDateEnd))
    }

    fun onSelectEndDate(view: View) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.end_date)
            .setSelection(selectedDateEnd.time)
            .setCalendarConstraints(
                CalendarConstraints
                    .Builder()
                    .setValidator(DateValidatorPointForward.from(selectedDateStart.time))
                    .build()
            )
            .build()

        picker.addOnPositiveButtonClickListener {
            selectedDateEnd.time = it
            formatEndDate()
        }
        viewContract?.showDialogFragment(picker, "SelectEndDate")
    }

    fun onLastSevenDaysClick(view: View) {
        applyDateRange(Calendar.DATE, -7)
    }

    fun onLastThirtyDaysClick(view: View) {
        applyDateRange(Calendar.DATE, -30)
    }

    fun onLastThreeMonthsClick(view: View) {
        applyDateRange(Calendar.MONTH, -3)
    }

    fun onLastSixMonthsClick(view: View) {
        applyDateRange(Calendar.MONTH, -6)
    }

    fun onLastYearClick(view: View) {
        applyDateRange(Calendar.YEAR, -1)
    }

    private fun applyDateRange(property: Int, amount: Int) {
        selectedDateEnd.time = MaterialDatePicker.todayInUtcMilliseconds()
        selectedDateStart.time = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance()
        calendar.add(property, amount)
        selectedDateStart.time = calendar.timeInMillis
        formatStartDate()
        formatEndDate()
    }

    fun onDoneClick(){
        onDateFilterDoneListener?.onDateFilterDone(
            selectedDateStart.time,
            selectedDateEnd.time
        )
        viewContract?.dismiss()
    }

    fun onCancelClick() {
        viewContract?.dismiss()
    }

    fun onClearClick() {
        onDateFilterDoneListener?.onDateFilterDone(
            0L,
            MaterialDatePicker.todayInUtcMilliseconds()
        )
        viewContract?.dismiss()
    }
}