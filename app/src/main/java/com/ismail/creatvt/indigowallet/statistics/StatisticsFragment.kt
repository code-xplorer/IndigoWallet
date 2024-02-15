package com.ismail.creatvt.indigowallet.statistics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.DataBindingFragment
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.FragmentStatisticsBinding

class StatisticsFragment:DataBindingFragment<FragmentStatisticsBinding>(R.layout.fragment_statistics) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
        binding?.viewModel = viewModel
    }

}