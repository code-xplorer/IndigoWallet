package com.ismail.creatvt.indigowallet.search.filter

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.DataBindingBottomSheetDialogFragment
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.FragmentFilterBottomSheetDialogBinding
import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.db.entity.TransactionType
import com.ismail.creatvt.indigowallet.db.entity.Wallet
import com.ismail.creatvt.indigowallet.utility.END_DATE
import com.ismail.creatvt.indigowallet.utility.START_DATE
import java.util.*

class FilterBottomSheetDialogFragment(val listener: OnFilterDoneListener) :
    DataBindingBottomSheetDialogFragment<FragmentFilterBottomSheetDialogBinding>(
        R.layout.fragment_filter_bottom_sheet_dialog
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = requireArguments().getParcelableArrayList<Category>(Category.CATEGORIES)?: arrayListOf()
        val wallets = requireArguments().getParcelableArrayList<Wallet>(Wallet.WALLETS)?: arrayListOf()
        val types = requireArguments().getParcelableArrayList<TransactionType>(TransactionType.TYPES)?: arrayListOf()

        val sevenDays = 7 * 24 * 60 * 60 * 1000

        val sDateLong = requireArguments().getLong(START_DATE)
        val eDateLong = requireArguments().getLong(END_DATE)

        val selectedDateStart = Date(
            if(sDateLong == 0L) eDateLong - sevenDays else sDateLong
        )
        val selectedDateEnd = Date(eDateLong)

        val viewModel = ViewModelProvider(this, FilterViewModelFactory(
            requireActivity().application,
            categories,
            wallets,
            types,
            selectedDateStart,
            selectedDateEnd
        )).get(FilterViewModel::class.java)

        viewModel.listener = listener

        binding?.viewModel = viewModel
    }

    override fun onStart() {
        super.onStart()
        binding?.viewModel?.viewContract = this
    }

    override fun onStop() {
        super.onStop()
        binding?.viewModel?.viewContract = null
    }

}