package com.ismail.creatvt.indigowallet.setup

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ismail.creatvt.indigowallet.HomeActivity
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.db.entity.Wallet
import com.ismail.creatvt.indigowallet.icons.IconPickerDialogFragment
import com.ismail.creatvt.indigowallet.utility.ColorFactory
import com.ismail.creatvt.indigowallet.utility.IconFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SetupViewModel(application: Application) : AndroidViewModel(application) {

    val name = MutableLiveData("")
    val initialBalance = MutableLiveData("")
    val iconString = MutableLiveData("icon_wallet_wallet")
    val colorString = MutableLiveData("color_dark_green_blue")
    var viewContract: ViewContract? = null

    val db = IndigoWalletDatabase.getInstance(application)

    fun getIconRes(icon: String): Int {
        return IconFactory.getWalletIcon(icon)
    }

    fun getColorRes(color: String): Int {
        return ColorFactory.getColorForName(color).colorRes
    }

    fun onIconClick() {
        val iconName = iconString.value!!
        val colorName = colorString.value!!
        val icon = IconFactory.getWalletIcon(iconName)
        val color = ColorFactory.getColorForName(colorName).colorRes

        val iconPicker = IconPickerDialogFragment(
            iconString.value!!,
            colorString.value!!,
            icon,
            color,
            IconFactory.walletIcons
        ) { i, c ->
            iconString.postValue(i)
            colorString.postValue(c)
        }

        viewContract?.showDialogFragment(iconPicker, "WalletIconPicker")
    }

    fun onDoneClick() {
        val name = name.value.toString()
        val initialBalance = initialBalance.value.toString().toFloat()
        val wallet = Wallet(
            name = name,
            balance = initialBalance,
            icon = iconString.value!!,
            color = colorString.value!!,
            exclude = false
        )
        CoroutineScope(Dispatchers.IO).launch {
            db.transactionWalletDao().insert(wallet)
            withContext(Dispatchers.Main) {
                viewContract?.startActivity(HomeActivity::class.java)
                viewContract?.dismiss()
            }
        }
    }
}