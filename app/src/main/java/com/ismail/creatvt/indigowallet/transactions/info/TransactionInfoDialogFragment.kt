package com.ismail.creatvt.indigowallet.transactions.info

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.DataBindingBottomSheetDialogFragment
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.FragmentTransactionInfoDialogBinding
import com.ismail.creatvt.indigowallet.transactions.TransactionViewModelFactory

class TransactionInfoDialogFragment(
    val transactionId: Int,
    val onDeleteListener: TransactionInfoViewModel.OnDeleteListener? = null
) :
    DataBindingBottomSheetDialogFragment<FragmentTransactionInfoDialogBinding>(
        R.layout.fragment_transaction_info_dialog
    ) {

    private var viewModel: TransactionInfoViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this, TransactionViewModelFactory(
                requireContext().applicationContext as Application,
                transactionId
            )
        ).get(TransactionInfoViewModel::class.java)

        viewModel?.viewContract = this
        viewModel?.deleteListener = onDeleteListener
        binding?.viewModel = viewModel

    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel?.viewContract = null
    }
}