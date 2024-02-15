package com.ismail.creatvt.indigowallet.settings.wallets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ismail.creatvt.indigowallet.BaseActivity
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.ActivityManageWalletsBinding
import kotlinx.android.synthetic.main.activity_manage_wallets.*

class ManageWalletsActivity : BaseActivity() {

    private var viewModel: WalletsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_IndigoWallet_BlueHeaderActivity)
        val binding = DataBindingUtil.setContentView<ActivityManageWalletsBinding>(
            this,
            R.layout.activity_manage_wallets
        )
        binding.lifecycleOwner = this

        setSupportActionBar(toolbar)
        setTitle(R.string.manage_wallets)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(WalletsViewModel::class.java)

        binding.adapter = WalletsAdapter(viewModel!!, this)
        binding.viewModel = viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel?.viewContract = this
    }

    override fun onStop() {
        super.onStop()
        viewModel?.viewContract = null
    }
}