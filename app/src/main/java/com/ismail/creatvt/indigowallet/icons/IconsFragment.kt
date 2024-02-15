package com.ismail.creatvt.indigowallet.icons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.FragmentIconsBinding
import com.ismail.creatvt.indigowallet.utility.IconFactory

class IconsFragment(val icons: List<IconFactory.Icon>, val listener: IconClickListener) : Fragment() {

    private var binding: FragmentIconsBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_icons,
            container,
            false
        )
        binding?.lifecycleOwner = viewLifecycleOwner
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.adapter = IconAdapter(icons, listener)
    }

}