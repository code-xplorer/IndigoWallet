package com.ismail.creatvt.indigowallet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit

open class DataBindingFragment<T:ViewDataBinding>(@LayoutRes private val layoutId: Int): Fragment(),ViewContract {

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

    override fun showDialogFragment(dialogFragment: DialogFragment, tag: String) {
        dialogFragment.show(childFragmentManager, tag)
    }

    override fun dismiss() {
        parentFragmentManager.popBackStack()
    }

    override fun commit(body: FragmentTransaction.() -> Unit) {
        childFragmentManager.commit(false, body)
    }

    override fun startActivity(activityClass: Class<*>) {
        startActivity(Intent(requireContext(), activityClass))
    }
}