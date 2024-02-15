package com.ismail.creatvt.indigowallet.addtransaction

import android.view.View

interface AddEditTransactionEventListener {
    fun onSaveClick(view: View)
    fun onTimeClick(view: View)
    fun onDateClick(view: View)
}