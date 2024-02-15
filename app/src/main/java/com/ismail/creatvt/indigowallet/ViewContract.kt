package com.ismail.creatvt.indigowallet

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction

interface ViewContract {
    fun showDialogFragment(dialogFragment: DialogFragment, tag: String)
    fun dismiss()
    fun commit(body: FragmentTransaction.() -> Unit)
    fun startActivity(activityClass:Class<*>)
}