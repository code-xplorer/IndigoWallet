package com.ismail.creatvt.indigowallet.settings.categories

import android.view.View
import com.ismail.creatvt.indigowallet.db.entity.Category

interface CategoryClickListener {

    fun onCategoryClick(view: View, category: Category)
}