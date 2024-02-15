package com.ismail.creatvt.indigowallet.addtransaction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.INPUT_MODE_CALENDAR
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_12H
import com.ismail.creatvt.indigowallet.BaseActivity
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.ActivityAddEditTransactionBinding
import com.ismail.creatvt.indigowallet.settings.SettingsActivity
import com.ismail.creatvt.indigowallet.transactions.TransactionViewModelFactory
import com.ismail.creatvt.indigowallet.utility.DateTimeFormatter.getDate
import com.ismail.creatvt.indigowallet.utility.DateTimeFormatter.getFormattedDate
import com.ismail.creatvt.indigowallet.utility.DateTimeFormatter.getFormattedTime
import com.ismail.creatvt.indigowallet.utility.DateTimeFormatter.getTime
import com.ismail.creatvt.indigowallet.utility.TRANSACTION_ID
import kotlinx.android.synthetic.main.activity_add_edit_transaction.*
import java.util.*

class AddEditTransactionActivity : BaseActivity() {

    private val transactionId: Int
        get() = intent.getIntExtra(TRANSACTION_ID, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_IndigoWallet_BlueHeaderActivity)
        val binding: ActivityAddEditTransactionBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_add_edit_transaction
        )

        binding.lifecycleOwner = this
        val viewModel = getViewModel()

        binding.viewModel = viewModel
        setSupportActionBar(toolbar)
        setTitle(R.string.add_transaction)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setting up the Tab layout
        val incomeFragment = IncomeFragment()
        val expenseFragment = ExpenseFragment()
        val transferFragment = TransferFragment()

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3

            override fun createFragment(position: Int) = when (position) {
                0 -> incomeFragment
                1 -> expenseFragment
                else -> transferFragment
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(
                when (position) {
                    0 -> R.string.income
                    1 -> R.string.expense
                    else -> R.string.transfer
                }
            )
        }.attach()

    }

    private fun getViewModel(): AddEditTransactionViewModel {
        return ViewModelProvider(
            this,
            TransactionViewModelFactory(application, transactionId)
        ).get(AddEditTransactionViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        getViewModel().viewContract = this
    }

    override fun onStop() {
        super.onStop()
        getViewModel().viewContract = null
    }

    override fun showDialogFragment(fragment: DialogFragment, tag: String) {
        fragment.show(supportFragmentManager, tag)
    }

}