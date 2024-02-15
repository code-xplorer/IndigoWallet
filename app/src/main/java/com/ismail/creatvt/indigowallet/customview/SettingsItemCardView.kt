package com.ismail.creatvt.indigowallet.customview

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import com.google.android.material.card.MaterialCardView
import com.ismail.creatvt.indigowallet.R
import kotlinx.android.synthetic.main.settings_item_layout.view.*

class SettingsItemCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val a = context.obtainStyledAttributes(attrs, R.styleable.SettingsItemCardView)

    var title: String
        get() = title_view.text.toString()
        set(value) {
            title_view.text = value
        }

    var subtitle: String
        get() = description_view.text.toString()
        set(value) {
            description_view.text = value
        }

    init {
        View.inflate(context, R.layout.settings_item_layout, this)
        isClickable = true
        cardElevation = 0f
        radius = resources.getDimension(R.dimen.medium_radius)
        strokeWidth = resources.getDimensionPixelSize(R.dimen.halfdp)
        strokeColor = ResourcesCompat.getColor(resources, R.color.grey_600, context.theme)

        val titleText = a.getText(R.styleable.SettingsItemCardView_android_title)?.toString()?:""
        title_view.text = titleText

        val descriptionText = a.getText(R.styleable.SettingsItemCardView_android_description)?.toString()?:""
        description_view.text = descriptionText

        val iconDrawable = a.getDrawable(R.styleable.SettingsItemCardView_android_icon)
        icon_view.setImageDrawable(iconDrawable)
        ImageViewCompat.setImageTintList(icon_view, null)
    }
}