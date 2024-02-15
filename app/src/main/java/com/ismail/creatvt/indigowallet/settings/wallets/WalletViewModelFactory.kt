package com.ismail.creatvt.indigowallet.settings.wallets

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.settings.wallets.edit.WalletInfoViewModel

class WalletViewModelFactory(
    val application: Application,
    val activity: FragmentActivity,
    val walletId: Int,
    val lastWallet:Boolean
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalletInfoViewModel::class.java)) {
            return WalletInfoViewModel(application, walletId, lastWallet, activity) as T
        }
        return super.create(modelClass)
    }
}