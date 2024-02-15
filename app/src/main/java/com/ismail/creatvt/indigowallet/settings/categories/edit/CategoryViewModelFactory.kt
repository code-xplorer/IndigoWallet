package com.ismail.creatvt.indigowallet.settings.categories.edit

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CategoryViewModelFactory(val application: Application, val categoryId: Int, val lastCategory:Boolean, val type: Int, val activity: FragmentActivity):
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(AddEditCategoryViewModel::class.java.isAssignableFrom(modelClass)){
            return AddEditCategoryViewModel(application, categoryId, lastCategory, type, activity) as T
        }
        return super.create(modelClass)
    }
}