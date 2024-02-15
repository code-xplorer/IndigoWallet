package com.ismail.creatvt.indigowallet.utility

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.MultiAutoCompleteTextView.CommaTokenizer
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.customview.SpacesItemDecoration
import com.ismail.creatvt.indigowallet.search.filter.Filter
import com.ismail.creatvt.indigowallet.utility.ColorFactory.textColor
import xander.elasticity.ElasticityHelper
import xander.elasticity.HorizontalElasticityBounceEffect
import xander.elasticity.ORIENTATION
import xander.elasticity.VerticalElasticityBounceEffect
import xander.elasticity.adapters.RecyclerViewElasticityAdapter


@BindingAdapter("selected")
fun setSelectedPosition(tabLayout: TabLayout, position: Int) {
    tabLayout.selectTab(tabLayout.getTabAt(position))
}

@BindingAdapter("selectedAttrChanged")
fun setSelectedAttrChanged(tabLayout: TabLayout, attrChanged: InverseBindingListener) {
    tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            attrChanged.onChange()
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }
    })
}

@InverseBindingAdapter(attribute = "selected")
fun getSelectedPosition(tabLayout: TabLayout): Int {
    return tabLayout.selectedTabPosition
}

@BindingAdapter("selectedItem")
fun setSelectedItem(autoCompleteTextView: AutoCompleteTextView, index: Int) {
    autoCompleteTextView.listSelection = index
}

@InverseBindingAdapter(attribute = "selectedItem", event = "selectedItemAttrChanged")
fun getSelectedItem(autoCompleteTextView: AutoCompleteTextView): Int {
    return autoCompleteTextView.listSelection
}

@BindingAdapter("selectedItemAttrChanged")
fun onSelectionChanged(
    autoCompleteTextView: AutoCompleteTextView,
    changeListener: InverseBindingListener
) {
    autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
        changeListener.onChange()
    }
    autoCompleteTextView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            changeListener.onChange()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            changeListener.onChange()
        }
    }
}

@BindingAdapter("data")
fun <T> setData(autoCompleteTextView: AutoCompleteTextView, values: List<T>?) {
    if (values != null) {
        autoCompleteTextView.setAdapter(
            ArrayAdapter(
                autoCompleteTextView.context,
                android.R.layout.simple_spinner_dropdown_item,
                values
            )
        )
        if (autoCompleteTextView is MultiAutoCompleteTextView) {
            autoCompleteTextView.setTokenizer(CommaTokenizer())
        }
    }
}

@BindingAdapter("tintResource")
fun ImageView.setTintResource(@ColorRes colorRes: Int) {
    ImageViewCompat.setImageTintList(
        this, ColorStateList.valueOf(
            ContextCompat.getColor(context, colorRes)
        )
    )
}

@BindingAdapter("tintColor")
fun ImageView.setTintColor(@ColorInt colorInt: Int) {
    ImageViewCompat.setImageTintList(
        this, ColorStateList.valueOf(colorInt)
    )
}

@BindingAdapter("backgroundTintColor")
fun ImageView.setBackgroundTintColor(@ColorInt colorInt: Int) {
    val bg = DrawableCompat.wrap(background)
    DrawableCompat.setTintList(
        bg,
        ColorStateList.valueOf(colorInt)
    )
    background = bg
}

@BindingAdapter("snapTo")
fun RecyclerView.setSnapTo(target: View) {
    SnapToHelper(target, this).attachToRecyclerView(this)
}

@BindingAdapter("linearSnap")
fun RecyclerView.setLinearSnap(set: Boolean) {
    LinearSnapHelper().attachToRecyclerView(this)
}

@BindingAdapter("iconImageColorSetup")
fun ImageView.setIconImageColorSetup(colorValue: Int) {
    val colorDrawable: Drawable = DrawableCompat.wrap(
        ContextCompat.getDrawable(
            context,
            R.drawable.category_icon_background
        ) ?: return
    )

    DrawableCompat.setTint(
        colorDrawable,
        colorValue
    )
    ImageViewCompat.setImageTintList(
        this,
        ColorStateList.valueOf(colorValue.textColor)
    )
    background = colorDrawable
}

@BindingAdapter("iconImageSetup")
fun ImageView.setIconImageSetup(colorRes: Int) {
    if (colorRes == 0) return

    val colorDrawable: Drawable = DrawableCompat.wrap(
        ContextCompat.getDrawable(
            context,
            R.drawable.category_icon_background
        ) ?: return
    )
    val colorInt = try {
        ContextCompat.getColor(context, colorRes)
    } catch (e: Exception) {
        Log.d("DataBindingAdapters", "Exception: ${e.message} $colorRes")
        0xff09f909.toInt()
    }

    DrawableCompat.setTint(
        colorDrawable,
        colorInt
    )
    ImageViewCompat.setImageTintList(
        this,
        ColorStateList.valueOf(colorInt.textColor)
    )
    background = colorDrawable
}

@BindingAdapter("gap")
fun RecyclerView.setGap(space: Float) {
    if (layoutManager is LinearLayoutManager) {
        addItemDecoration(SpacesItemDecoration(space.toInt()))
    }
}

@BindingAdapter("onPageChanged")
fun ViewPager2.setOnPageChangedListener(pageChangedListener: OnPageChangedListener) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            pageChangedListener.onPageChanged(position)
        }
    })
}

@BindingAdapter("filterItems", "onCloseClick", "onChipClick")
fun ChipGroup.setChipItems(
    filterItems: List<Filter>,
    listener: OnCloseClickListener,
    chipClickListener: OnChipClickListener
) {
    removeAllViews()
    filterItems.forEachIndexed { index, filter ->
        val chip = layoutInflater.inflate(R.layout.filter_chip, this, false) as Chip
        chip.text = filter.text
        chip.setOnCloseIconClickListener {
            listener.onCloseClick(chip, index, filter)
        }
        chip.setOnClickListener {
            chipClickListener.onChipClick(chip, index, filter)
        }
        addView(chip)
    }
}

interface OnChipClickListener {
    fun onChipClick(view: View, position: Int, filter: Filter)
}

interface OnCloseClickListener {
    fun onCloseClick(view: View, position: Int, filter: Filter)
}

interface OnPageChangedListener {
    fun onPageChanged(page: Int)
}