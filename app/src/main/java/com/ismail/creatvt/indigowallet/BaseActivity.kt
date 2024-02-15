package com.ismail.creatvt.indigowallet

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit

abstract class BaseActivity: AppCompatActivity(), ViewContract {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showDialogFragment(dialogFragment: DialogFragment, tag: String) {
        dialogFragment.show(supportFragmentManager, tag)
    }

    override fun dismiss() {
        finish()
    }

    override fun commit(body: FragmentTransaction.() -> Unit) {
        supportFragmentManager.commit(false, body)
    }

    override fun startActivity(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass))
    }

}