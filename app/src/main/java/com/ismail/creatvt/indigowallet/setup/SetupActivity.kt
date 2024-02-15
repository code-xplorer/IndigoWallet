package com.ismail.creatvt.indigowallet.setup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.BaseActivity
import com.ismail.creatvt.indigowallet.HomeActivity
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.ViewContract
import com.ismail.creatvt.indigowallet.databinding.ActivitySetupBinding
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.db.entity.Wallet
import com.ismail.creatvt.indigowallet.utility.CategoryFactory
import kotlinx.android.synthetic.main.activity_setup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SetupActivity : BaseActivity(), ViewTreeObserver.OnGlobalFocusChangeListener {
    private var binding: ActivitySetupBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_IndigoWallet)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_setup
        )

        CoroutineScope(IO).launch {
            val categoryDao = IndigoWalletDatabase.getInstance(this@SetupActivity)
                .categoryDao()
            if (categoryDao.getCount() == 0) {
                categoryDao.insertAll(CategoryFactory.defaultCategories)
            }
        }

        val viewModel = ViewModelProvider(this).get(SetupViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel

        binding?.root?.viewTreeObserver?.addOnGlobalFocusChangeListener(this)
    }

    override fun onStart() {
        super.onStart()

        binding?.viewModel?.viewContract = this
    }

    override fun onStop() {
        super.onStop()

        binding?.viewModel?.viewContract = null
    }

    override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
        val rootHeight = setup_root.height
        val windowHeight = window.decorView.height
        if (rootHeight < windowHeight) {
            setup_scroll.smoothScrollTo(0, setup_scroll.maxScrollAmount)
        }
    }

}