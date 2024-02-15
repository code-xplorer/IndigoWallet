package com.ismail.creatvt.indigowallet.transactions

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.map
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.TransactionListRepository
import com.ismail.creatvt.indigowallet.databinding.TransactionDateHeaderLayoutBinding
import com.ismail.creatvt.indigowallet.databinding.TransactionItemLayoutBinding
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransactionWithWallet
import com.ismail.creatvt.indigowallet.transactions.util.TransactionsDiffUtil
import com.ismail.creatvt.indigowallet.utility.DateTimeFormatter
import com.ismail.creatvt.indigowallet.utility.inflate

class TransactionAdapter(
    val transactionViewModel: TransactionListRepository,
    val owner: LifecycleOwner
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var transactions: List<Any> = arrayListOf()

    init {
        transactionViewModel.transactions.map {
            val list = arrayListOf<Any>()
            var currentDate: String? = null
            it.forEach { transaction ->
                val dateString = DateTimeFormatter.getFormattedDate(transaction.transaction.timestamp)
                if (dateString != currentDate) {
                    list.add(TransactionDate(transaction.transaction.timestamp))
                    currentDate = dateString
                }
                list.add(transaction)
            }
            list
        }.observe(owner) {
            val oldItems = transactions
            transactions = it
            DiffUtil.calculateDiff(TransactionsDiffUtil(oldItems, transactions))
                .dispatchUpdatesTo(this)
        }
    }

    class DateHeaderViewHolder(val itemBinding: TransactionDateHeaderLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    class TransactionItemViewHolder(val itemBinding: TransactionItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DATE_HEADER -> DateHeaderViewHolder(
                parent.inflate<TransactionDateHeaderLayoutBinding>(
                    R.layout.transaction_date_header_layout
                ).apply {
                    lifecycleOwner = owner
                }
            )
            else -> TransactionItemViewHolder(
                parent.inflate<TransactionItemLayoutBinding>(
                    R.layout.transaction_item_layout
                ).apply {
                    lifecycleOwner = owner
                }
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = transactions[position]
        holder.apply {
            when (this) {
                is DateHeaderViewHolder -> itemBinding.date = data as TransactionDate
                is TransactionItemViewHolder -> {
                    itemBinding.transaction = data as MoneyTransactionWithWallet
                    itemBinding.viewModel = transactionViewModel
                }
            }
        }
    }

    companion object {
        const val DATE_HEADER = 2
    }

    override fun getItemViewType(position: Int) = when (transactions[position]) {
        is TransactionDate -> DATE_HEADER
        else -> 0
    }

    override fun getItemCount() = transactions.size
}