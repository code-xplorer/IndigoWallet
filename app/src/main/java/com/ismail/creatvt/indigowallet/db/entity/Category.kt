package com.ismail.creatvt.indigowallet.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME
import com.ismail.creatvt.indigowallet.utility.ColorFactory
import com.ismail.creatvt.indigowallet.utility.ColorFactory.textColor
import com.ismail.creatvt.indigowallet.utility.IconFactory
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id") var id: Int = 0,
    var name: String,
    var icon: String,
    var color: String,
    var type: Int
) :Parcelable{
    override fun toString(): String {
        return name
    }

    val colorRes: Int
        get() = ColorFactory.getColorForName(color).colorRes

    val isIncome: Boolean
        get() = type == TYPE_INCOME

    val iconRes: Int
        get() = IconFactory.getCategoryIcon(icon)

    companion object{
        const val CATEGORIES = "Categories"
    }
}