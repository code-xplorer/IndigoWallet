package com.ismail.creatvt.indigowallet.transactions

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.DataBindingFragment
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.FragmentTransactionsBinding
import com.ismail.creatvt.indigowallet.search.SearchActivity
import com.ismail.creatvt.indigowallet.settings.SettingsActivity
import com.ismail.creatvt.indigowallet.transactions.summary.WalletsListAdapter
import com.ismail.creatvt.indigowallet.utility.startActivityAnim

class TransactionsFragment :
    DataBindingFragment<FragmentTransactionsBinding>(R.layout.fragment_transactions) {

    private var viewModel: TransactionsViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(TransactionsViewModel::class.java)
        viewModel?.viewContract = this

        binding?.viewModel = viewModel
        binding?.adapter = TransactionAdapter(viewModel!!, viewLifecycleOwner)
        binding?.walletsAdapter = WalletsListAdapter(viewModel!!, viewLifecycleOwner)

        binding?.toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.option_settings -> {
                    startActivityAnim(SettingsActivity::class.java)
                    return@setOnMenuItemClickListener true
                }
                R.id.option_search -> {
                    startActivityAnim(SearchActivity::class.java)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel?.viewContract = null
    }
}