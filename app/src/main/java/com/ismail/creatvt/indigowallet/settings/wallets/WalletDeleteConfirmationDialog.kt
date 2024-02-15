package com.ismail.creatvt.indigowallet.settings.wallets

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.WalletDeleteConfirmationDialogBinding

class WalletDeleteConfirmationDialog(
    context: Context,
    walletName: String,
    val onDeleteConfirmation: () -> Unit
) : Dialog(context),
    DeleteConfirmationListener {

    val binding = DataBindingUtil.inflate<WalletDeleteConfirmationDialogBinding>(
        layoutInflater,
        R.layout.wallet_delete_confirmation_dialog,
        null,
        false
    )

    init {
        setContentView(binding.root)
//        window?.setBackgroundDrawableResource(R.drawable.dialog_background_drawable)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        binding.listener = this
        binding.walletName = walletName
    }

    override fun onYesClick() {
        onDeleteConfirmation()
        dismiss()
    }

    override fun onNoClick() {
        dismiss()
    }

}