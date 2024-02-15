package com.ismail.creatvt.indigowallet.icons

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.customview.ColorSelectorView
import com.ismail.creatvt.indigowallet.databinding.DialogIconPickerBinding
import com.ismail.creatvt.indigowallet.utility.ColorFactory.textColor
import com.ismail.creatvt.indigowallet.utility.IconFactory


class IconPickerDialog(
    context: Context,
    val fragment: Fragment,
    initialIconName: String,
    initialColorName: String,
    val initialIcon: Int,
    val initialColor: Int,
    val icons: ArrayList<IconFactory.Icon>,
    val onDone: (String, String) -> Unit
) : Dialog(context, R.style.Theme_IndigoWallet_Dialog), IconClickListener,
    IconPickerListener, ColorSelectorView.OnColorSelectedListener {

    private var binding: DialogIconPickerBinding?=null

    private var iconName:String = initialIconName
    private var colorName: String = initialColorName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_icon_picker,
            null,
            false
        )
        binding?.lifecycleOwner = fragment
        val fragmentsCount = icons.size/20
        binding?.listener = this
        binding?.nextEnabled = MutableLiveData(true)
        binding?.prevEnabled = MutableLiveData(false)
        binding?.initialColor = initialColor
        binding?.selectedIcon = MutableLiveData(initialIcon)
        if(binding != null){
            val initialColorValue = ResourcesCompat.getColor(binding!!.root.resources, initialColor, binding!!.root.context.theme)
            binding?.selectedColor = MutableLiveData(initialColorValue)
        }

        binding?.adapter = object:FragmentStateAdapter(fragment){
            override fun getItemCount() = fragmentsCount

            override fun createFragment(position: Int): Fragment {
                val start = position * 20
                val end = (start + 20).coerceAtMost(icons.size)
                return IconsFragment(icons.subList(start, end), this@IconPickerDialog)
            }

        }
        binding?.colorSelectedListener = this
        setContentView(binding!!.root)
    }

    override fun onIconClick(view: View, icon: IconFactory.Icon) {
        Log.d("IconPickerDialogTag", "Icon: ${icon.name}")
        binding?.selectedIcon?.value = icon.iconRes
        iconName = icon.name
    }

    override fun onStart() {
        super.onStart()
        val maxHeight = context.resources.getDimension(R.dimen.dialog_max_height).toInt()
        window?.setLayout(MATCH_PARENT, maxHeight)
    }

    override fun onNextClick() {
        val viewPager = binding?.viewPager2?:return
        viewPager.currentItem++
    }

    override fun onPrevClick() {
        val viewPager = binding?.viewPager2?:return
        viewPager.currentItem--
    }

    override fun onPageChanged(page: Int) {
        val viewPager = binding?.viewPager2?:return
        val size = viewPager.adapter?.itemCount?:return
        binding?.nextEnabled?.value = page != size - 1
        binding?.prevEnabled?.value = page != 0
    }

    override fun onColorSelected(
        colorSelectorView: ColorSelectorView,
        position: Int,
        colorValue: Int
    ) {
        Log.d("IconPickerDialogTag", "Color: $colorValue")
        binding?.selectedColor?.value = colorValue
        colorName = colorSelectorView.resources.getStringArray(R.array.color_selector_colour_names)[position]
    }

    override fun onOkClicked() {
        onDone(iconName, colorName)
        dismiss()
    }

    override fun onCancelClicked() {
        dismiss()
    }
}