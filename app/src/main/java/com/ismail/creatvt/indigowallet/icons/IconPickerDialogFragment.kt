package com.ismail.creatvt.indigowallet.icons

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.utility.IconFactory

class IconPickerDialogFragment(
    var initialIconName: String,
    var initialColorName: String,
    val initialIcon: Int,
    val initialColor: Int,
    val icons: ArrayList<IconFactory.Icon>,
    val onDone: (String, String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return IconPickerDialog(
            requireContext(),
            this,
            initialIconName,
            initialColorName,
            initialIcon,
            initialColor,
            icons,
            onDone
        )
    }

}