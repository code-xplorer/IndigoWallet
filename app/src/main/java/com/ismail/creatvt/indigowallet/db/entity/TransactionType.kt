package com.ismail.creatvt.indigowallet.db.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TransactionType(val id: Int, val text: String):Parcelable {
    override fun toString(): String {
        return text
    }

    companion object{
        const val TYPES = "TYPES"
    }
}
    