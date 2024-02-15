package com.ismail.creatvt.indigowallet.settings.wallets

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.settings.wallets.edit.WalletInfoDialogFragment

class WalletsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = IndigoWalletDatabase.getInstance(application)

    val wallets = db.transactionWalletDao().getAllWallets()

    var viewContract: ViewContract? = null

    fun onWalletClick(walletId: Int) {
        val lastWallet = wallets.value?.size?:0 <= 1
        viewContract?.showDialogFragment(
            WalletInfoDialogFragment(walletId, lastWallet),
            "WalletInfoDialog"
        )
    }

    fun onAddWalletClick() {
        viewContract?.showDialogFragment(
            WalletInfoDialogFragment(0, false),
            "AddWalletDialog"
        )
    }
}