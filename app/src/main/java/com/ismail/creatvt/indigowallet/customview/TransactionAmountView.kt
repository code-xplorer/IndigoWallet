package com.ismail.creatvt.indigowallet.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.google.android.material.card.MaterialCardView
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_EXPENSE
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME
import kotlinx.android.synthetic.main.transaction_detail_item_layout.view.*

@BindingMethods(
    BindingMethod(
        type = TransactionAmountView::class,
        attribute = "fromWallet",
        method = "setFromWallet"
    ),
    BindingMethod(
        type = TransactionAmountView::class,
        attribute = "toWallet",
        method = "setToWallet"
    ),
    BindingMethod(
        type = TransactionAmountView::class,
        attribute = "amount",
        method = "setAmount"
    ),
    BindingMethod(
        type = TransactionAmountView::class,
        attribute = "transactionType",
        method = "setTransactionType"
    ),
)
class TransactionAmountView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val a = context.obtainStyledAttributes(attrs, R.styleable.TransactionAmountView)

    var fromWallet: String?
        get() = from_wallet.text.toString()
        set(value) {
            from_wallet.text = value
        }

    var toWallet: String?
        get() = from_wallet.text.toString()
        set(value) {
            to_wallet.text = value
        }

    var amount: Float = 0.0f
        set(value) {
            field = value
            amount_text.text = String.format("₹%.2f", value)
        }

    var transactionType: Int = TYPE_INCOME
        set(value) {
            field = value
            val isTransfer = value != TYPE_INCOME && value != TYPE_EXPENSE
            from_wallet.isVisible = isTransfer
            arrow_tail.isVisible = isTransfer
            arrow_head.isVisible = isTransfer

            if(value == TYPE_INCOME){
                to_wallet.setTextColor(ContextCompat.getColor(context, R.color.green_500))
                amount_text.setTextColor(ContextCompat.getColor(context, R.color.green_500))
            } else if(value == TYPE_EXPENSE){
                to_wallet.setTextColor(ContextCompat.getColor(context, R.color.red_500))
                amount_text.setTextColor(ContextCompat.getColor(context, R.color.red_500))
            } else{
                to_wallet.setTextColor(ContextCompat.getColor(context, R.color.grey_800))
                amount_text.setTextColor(ContextCompat.getColor(context, R.color.grey_800))
                from_wallet.setTextColor(ContextCompat.getColor(context, R.color.grey_800))
            }
        }

    init {
        View.inflate(context, R.layout.transaction_detail_item_layout, this)
        cardElevation = 0f
        strokeWidth = 0
        setCardBackgroundColor(ContextCompat.getColorStateList(context, R.color.lightWhite))

        fromWallet = a.getText(R.styleable.TransactionAmountView_fromWallet)?.toString() ?: ""
        toWallet = a.getText(R.styleable.TransactionAmountView_toWallet)?.toString() ?: ""
        transactionType = a.getInt(R.styleable.TransactionAmountView_transactionType, 0)
        amount = a.getFloat(R.styleable.TransactionAmountView_amount, 0f)
    }

}