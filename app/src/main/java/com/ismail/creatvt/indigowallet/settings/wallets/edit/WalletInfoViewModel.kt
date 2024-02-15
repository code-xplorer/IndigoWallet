package com.ismail.creatvt.indigowallet.settings.wallets.edit

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.db.entity.Wallet
import com.ismail.creatvt.indigowallet.icons.IconPickerDialogFragment
import com.ismail.creatvt.indigowallet.settings.wallets.WalletDeleteConfirmationDialog
import com.ismail.creatvt.indigowallet.utility.ColorFactory
import com.ismail.creatvt.indigowallet.utility.IconFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WalletInfoViewModel(
    application: Application,
    val walletId: Int,
    val lastWallet:Boolean,
    activity: FragmentActivity
) :
    AndroidViewModel(application), Observer<Wallet> {

    private val db = IndigoWalletDatabase.getInstance(application)
    val wallet = db.transactionWalletDao().getWallet(walletId)

    var viewContract: ViewContract? = null

    val name = MutableLiveData("")
    val balance = MutableLiveData("0.0")
    val exclude = MutableLiveData(false)
    val icon = MutableLiveData("")
    val color = MutableLiveData("")

    val iconRes = icon.map {
        IconFactory.getWalletIcon(it)
    }
    val colorRes = color.map {
        ColorFactory.getColorForName(it).colorRes
    }

    val enabled = MediatorLiveData<Boolean>()

    init {
        setUpEnabledSources()

        wallet.observe(activity, this)
    }

    private fun setUpEnabledSources() {
        enabled.addSource(name) {
            validateForm(
                it,
                balance.value?.toFloatOrNull() ?: 0f,
                icon.value,
                color.value,
                exclude.value ?: false
            )
        }

        enabled.addSource(balance) {
            validateForm(
                name.value,
                it.toFloatOrNull() ?: 0f,
                icon.value,
                color.value,
                exclude.value ?: false
            )
        }

        enabled.addSource(icon) {
            validateForm(
                name.value,
                balance.value?.toFloatOrNull() ?: 0f,
                it,
                color.value,
                exclude.value ?: false
            )
        }

        enabled.addSource(color) {
            validateForm(
                name.value,
                balance.value?.toFloatOrNull() ?: 0f,
                icon.value,
                it,
                exclude.value ?: false
            )
        }

        enabled.addSource(exclude) {
            validateForm(
                name.value,
                balance.value?.toFloatOrNull() ?: 0f,
                icon.value,
                color.value,
                it
            )
        }
    }

    private fun validateForm(
        name: String?,
        balance: Float,
        icon: String?,
        color: String?,
        exclude: Boolean
    ) {
        if (name.isNullOrEmpty() &&
            balance.isNaN() &&
            icon.isNullOrEmpty() &&
            color.isNullOrEmpty()
        ) {
            enabled.postValue(false)
            return
        }
        val walletEdit = wallet.value

        if (walletId != 0 && walletEdit != null) {
            if (walletEdit.name == name?.trim() &&
                walletEdit.icon == icon &&
                walletEdit.color == color &&
                walletEdit.balance == balance &&
                walletEdit.exclude == exclude
            ) {
                enabled.postValue(false)
                return
            }
        }
        enabled.postValue(true)
    }

    fun onIconClick() {
        val iconName = icon.value!!
        val colorName = color.value!!
        val icon = IconFactory.getWalletIcon(iconName)
        val color = ColorFactory.getColorForName(colorName).colorRes

        val iconPicker = IconPickerDialogFragment(
            iconName,
            colorName,
            icon,
            color,
            IconFactory.walletIcons
        ) { i, c ->
            this@WalletInfoViewModel.icon.postValue(i)
            this@WalletInfoViewModel.color.postValue(c)
        }

        viewContract?.showDialogFragment(iconPicker, "WalletIconPicker")
    }

    fun onDeleteClick(view: View) {
        viewContract?.dismiss()
        val walletName = wallet.value?.name?:return
        val warningTitle = LayoutInflater.from(view.context)
                .inflate(R.layout.warning_title_view, null)
        MaterialAlertDialogBuilder(view.context)
            .setCustomTitle(warningTitle)
            .setMessage(view.context.getString(R.string.wallet_confirmation_message, walletName))
            .setPositiveButton(R.string.yes) { _, _ ->
                deleteAndExit()
            }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    private fun deleteAndExit() {
        CoroutineScope(IO).launch {
            db.commonDao().deleteWalletAndTransactions(walletId)
            withContext(Main) {
                viewContract?.dismiss()
            }
        }
    }

    fun onDoneClick() {
        val wallet = Wallet(
            walletId,
            name.value!!.trim(),
            balance.value!!.toFloat(),
            icon.value!!,
            color.value!!,
            exclude.value!!
        )

        CoroutineScope(IO).launch {
            db.transactionWalletDao().apply {
                if (walletId == 0) {
                    insert(wallet)
                } else {
                    update(wallet)
                }
            }
            withContext(Main) {
                viewContract?.dismiss()
            }
        }
    }

    override fun onChanged(walletSnapshot: Wallet?) {
        if (walletSnapshot != null) {
            name.postValue(walletSnapshot.name)
            balance.postValue(walletSnapshot.balance.toString())
            exclude.postValue(walletSnapshot.exclude)
            icon.postValue(walletSnapshot.icon)
            color.postValue(walletSnapshot.color)
            wallet.removeObserver(this)
        }
    }
}