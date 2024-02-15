package com.ismail.creatvt.indigowallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ismail.creatvt.indigowallet.addtransaction.AddEditTransactionActivity
import com.ismail.creatvt.indigowallet.utility.startActivityAnim
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_IndigoWallet_HomeActivity)
        setContentView(R.layout.activity_home)

        bottomNavigationView.background = null

        val navController = fragment.findNavController()
        bottomNavigationView.setupWithNavController(navController)

        add_button.setOnClickListener {
            startActivity(Intent(this, AddEditTransactionActivity::class.java))
        }
    }
}