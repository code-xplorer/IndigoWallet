package com.ismail.creatvt.indigowallet.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismail.creatvt.indigowallet.BaseActivity
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.settings.categories.CategoriesActivity
import com.ismail.creatvt.indigowallet.settings.wallets.ManageWalletsActivity
import com.ismail.creatvt.indigowallet.utility.startActivityAnim
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_IndigoWallet_BlueHeaderActivity)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        setTitle(R.string.settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        manage_categories.setOnClickListener {
            startActivityAnim(CategoriesActivity::class.java)
        }

        manage_wallets.setOnClickListener {
            startActivityAnim(ManageWalletsActivity::class.java)
        }
    }
}