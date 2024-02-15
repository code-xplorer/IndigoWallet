package com.ismail.creatvt.indigowallet.settings.wallets.edit

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.DataBindingBottomSheetDialogFragment
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.FragmentWalletInfoDialogBinding
import com.ismail.creatvt.indigowallet.settings.wallets.WalletViewModelFactory

class WalletInfoDialogFragment(val walletId: Int, val lastWallet: Boolean) :
    DataBindingBottomSheetDialogFragment<FragmentWalletInfoDialogBinding>(
        R.layout.fragment_wallet_info_dialog
    ) {

    private var viewModel: WalletInfoViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this, WalletViewModelFactory(
                requireContext().applicationContext as Application,
                requireActivity(),
                walletId,
                lastWallet
            )
        ).get(WalletInfoViewModel::class.java)

        binding?.viewModel = viewModel
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