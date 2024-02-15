package com.ismail.creatvt.indigowallet

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class DataBindingDialogFragment<T:ViewDataBinding>(@LayoutRes private val layoutId: Int): DialogFragment(), ViewContract{

    protected var binding: T?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding?.lifecycleOwner = viewLifecycleOwner
        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(null)
        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun showDialogFragment(dialogFragment: DialogFragment, tag: String) {
        dialogFragment.show(childFragmentManager, tag)
    }

    override fun commit(body: FragmentTransaction.() -> Unit) {
        childFragmentManager.commit(false, body)
    }

    override fun startActivity(activityClass: Class<*>) {
        startActivity(Intent(requireContext(), activityClass))
    }

}