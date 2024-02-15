package com.ismail.creatvt.indigowallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ismail.creatvt.indigowallet.db.IndigoWalletDatabase
import com.ismail.creatvt.indigowallet.setup.SetupActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {
    private var isNew: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(IO).launch {
            val db = IndigoWalletDatabase.getInstance(this@SplashActivity)
            isNew = db.transactionWalletDao().getWalletCount() == 0
            withContext(Main) {
                if (isNew) {
                    startActivity(Intent(this@SplashActivity, SetupActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                }
                finish()
            }
        }
    }
}