package com.ismail.creatvt.indigowallet.addtransaction

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.DataBindingFragment
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.FragmentExpenseBinding

class ExpenseFragment:DataBindingFragment<FragmentExpenseBinding>(R.layout.fragment_expense) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity()).get(AddEditTransactionViewModel::class.java)

        binding?.viewModel = viewModel
    }

}