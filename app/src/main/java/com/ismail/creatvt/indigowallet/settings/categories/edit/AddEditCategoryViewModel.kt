package com.ismail.creatvt.indigowallet.settings.categories.edit

import android.app.Application
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.db.entity.Category
import com.ismail.creatvt.indigowallet.icons.IconPickerDialogFragment
import com.ismail.creatvt.indigowallet.utility.CATEGORY
import com.ismail.creatvt.indigowallet.utility.IconFactory
import com.ismail.creatvt.indigowallet.utility.getColor
import com.ismail.creatvt.indigowallet.utility.layoutInflater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditCategoryViewModel(
    application: Application,
    val categoryId: Int,
    val lastCategory: Boolean,
    type: Int,
    activity: FragmentActivity
) :
    AndroidViewModel(application), CategoryIconsDialogFragment.IconSelectedListener {

    private val db = IndigoWalletDatabase.getInstance(application)
    private val categoryDao = db.categoryDao()
    private val category: LiveData<Category> = if (categoryId > 0) {
        try {
            categoryDao.getCategoryForId(categoryId)
        } catch (e: Exception) {
            Log.d("AddEditCategoryVM", "Exception : ${e.message}")
            MutableLiveData()
        }
    } else {
        MutableLiveData(
            Category(
                0,
                "",
                "icon_category_question",
                "md_grey_700",
                type
            )
        )
    }

    var viewContract: ViewContract? = null

    val categoryEdit = MutableLiveData<Category>()

    val name = MutableLiveData("")

    init {
        category.observe(activity) {
            if(it != null){
                name.postValue(it.name)
                categoryEdit.postValue(it)
            }
        }
    }

    fun onIconClick(view: View) {
        val category = categoryEdit.value ?: return
        val data = bundleOf(CATEGORY to category)
        val fragment = IconPickerDialogFragment(
            category.icon,
            category.color,
            category.iconRes,
            category.colorRes,
            IconFactory.categoryIcons
        ) { icon: String, color: String ->
            category.color = color
            category.icon = icon
            categoryEdit.value = category
        }
        fragment.arguments = data
        viewContract?.showDialogFragment(fragment, "CategoryIcons")
    }

    fun onDoneClick(view: View) {
        val cat = categoryEdit.value ?: return
        cat.name = name.value ?: cat.name
        CoroutineScope(IO).launch {
            if (cat.id == 0) {
                db.categoryDao().insert(cat)
            } else {
                db.categoryDao().update(cat)
            }
            withContext(Main) {
                viewContract?.dismiss()
            }
        }
    }

    fun onDeleteClick(view: View) {
        val categoryToDelete = category.value ?: return
        viewContract?.dismiss()

        MaterialAlertDialogBuilder(view.context)
            .setCustomTitle(view.layoutInflater.inflate(R.layout.warning_title_view, null))
            .setMessage(
                view.resources.getString(
                    R.string.category_confirmation_message,
                    categoryToDelete.name
                )
            )
            .setPositiveButton(R.string.yes) { dialog, _ ->
                CoroutineScope(IO).launch {
                    db.commonDao().deleteCategoryAndTransactions(categoryToDelete.id)
                }
            }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    override fun onIconSelected(icon: IconFactory.Icon) {
        val cat = categoryEdit.value ?: return
        cat.icon = icon.name
        categoryEdit.postValue(cat)
    }

}
