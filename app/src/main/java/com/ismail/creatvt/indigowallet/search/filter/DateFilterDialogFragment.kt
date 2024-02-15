package com.ismail.creatvt.indigowallet.search.filter

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.DataBindingBottomSheetDialogFragment
import com.ismail.creatvt.indigowallet.DataBindingDialogFragment
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.FragmentDateFilterDialogBinding
import com.ismail.creatvt.indigowallet.utility.END_DATE
import com.ismail.creatvt.indigowallet.utility.START_DATE
import java.util.*

class DateFilterDialogFragment(
    val onDateFilterDoneListener: OnDateFilterDoneListener
) : DataBindingBottomSheetDialogFragment<FragmentDateFilterDialogBinding>(R.layout.fragment_date_filter_dialog) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startDate = requireArguments().getLong(START_DATE)
        val endDate = requireArguments().getLong(END_DATE)

        val viewModel = ViewModelProvider(
            this,
            FilterViewModelFactory(
                requireActivity().application,
                arrayListOf(),
                arrayListOf(),
                arrayListOf(),
                Date(startDate),
                Date(endDate)
            )
        ).get(DateFilterViewModel::class.java)

        binding?.viewModel = viewModel
    }

    override fun onStart() {
        super.onStart()

        binding?.viewModel?.viewContract = this
        binding?.viewModel?.onDateFilterDoneListener = onDateFilterDoneListener
    }

    override fun onStop() {
        super.onStop()

        binding?.viewModel?.viewContract = null
        binding?.viewModel?.onDateFilterDoneListener = null
    }
}