package com.ismail.creatvt.indigowallet.utility

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.ismail.creatvt.indigowallet.R

fun Activity.startActivityAnim(activityClass: Class<*>){
    startActivity(Intent(this, activityClass))
    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation)
}

fun Fragment.startActivityAnim(activityClass: Class<*>){
    requireActivity().startActivityAnim(activityClass)
}
