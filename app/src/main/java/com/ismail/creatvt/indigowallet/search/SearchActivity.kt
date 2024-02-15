package com.ismail.creatvt.indigowallet.search

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.BaseActivity
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.ActivitySearchBinding
import com.ismail.creatvt.indigowallet.transactions.TransactionAdapter

class SearchActivity : BaseActivity() {

    private var viewModel: SearchViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_IndigoWallet_BlueHeaderActivity)
        val binding = DataBindingUtil.setContentView<ActivitySearchBinding>(
            this,
            R.layout.activity_search
        )
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        binding.viewModel = viewModel
        binding.adapter = TransactionAdapter(viewModel!!, this)
    }

    override fun onStart() {
        super.onStart()

        viewModel?.viewContract = this
    }

    override fun onStop() {
        super.onStop()

        viewModel?.viewContract = null
    }
}