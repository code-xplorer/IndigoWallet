package com.ismail.creatvt.indigowallet.search.filter

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.google.android.material.card.MaterialCardView
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.utility.getColor
import kotlinx.android.synthetic.main.filter_item_view_layout.view.*

@BindingMethods(
    BindingMethod(
        type = FilterItemView::class,
        method = "setText",
        attribute = "android:text"
    )
)
class FilterItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    var text: String? = ""
        set(value) {
            field = value
            try {
                message.text = text
            } catch (e: Exception) {

            }
        }

    init {
        inflate(context, R.layout.filter_item_view_layout, this)

        cardElevation = 0f
        radius = resources.getDimension(R.dimen.medium_radius)
        strokeColor = getColor(R.color.grey_600)
        strokeWidth = resources.getDimensionPixelSize(R.dimen.halfdp)
    }
}