package com.ismail.creatvt.indigowallet.transactions.summary

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.WalletHorizontalItemLayoutBinding
import com.ismail.creatvt.indigowallet.db.entity.Wallet
import com.ismail.creatvt.indigowallet.settings.wallets.WalletDiffCallback
import com.ismail.creatvt.indigowallet.transactions.TransactionsViewModel
import com.ismail.creatvt.indigowallet.utility.layoutInflater

class WalletsListAdapter(val viewModel: TransactionsViewModel, val owner: LifecycleOwner) :
    RecyclerView.Adapter<WalletsListAdapter.WalletViewHolder>() {

    var wallets: List<Wallet> = arrayListOf()
        set(value) {
            val oldItems = field
            field = value
            DiffUtil.calculateDiff(WalletDiffCallback(oldItems, field)).dispatchUpdatesTo(this)
        }

    init {
        viewModel.wallets.observe(owner){
            wallets = it
        }
    }

    class WalletViewHolder(val itemBinding: WalletHorizontalItemLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WalletViewHolder(
        DataBindingUtil.inflate(
            parent.layoutInflater,
            R.layout.wallet_horizontal_item_layout,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        holder.itemBinding.wallet = wallets[position]
    }

    override fun getItemCount() = wallets.size
}