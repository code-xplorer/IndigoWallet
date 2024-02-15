package com.ismail.creatvt.indigowallet.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ismail.creatvt.indigowallet.utility.ColorFactory
import com.ismail.creatvt.indigowallet.utility.IconFactory
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Wallet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallet_id")
    var id: Int = 0,
    @ColumnInfo(name = "wallet_name")
    var name: String,
    @ColumnInfo(name = "wallet_balance")
    var balance: Float,
    @ColumnInfo(name = "wallet_icon")
    var icon: String,
    @ColumnInfo(name = "wallet_color")
    var color: String,
    @ColumnInfo(name = "wallet_exclude")
    var exclude: Boolean
):Parcelable {

    val iconRes: Int
        get() = IconFactory.getWalletIcon(icon)

    val colorRes: Int
        get() = ColorFactory.getColorForName(color).colorRes

    override fun toString(): String {
        return name
    }

    companion object{
        const val WALLETS = "Wallets"
    }
}