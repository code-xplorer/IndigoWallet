package com.ismail.creatvt.indigowallet.settings.wallets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.WalletItemLayoutBinding
import com.ismail.creatvt.indigowallet.db.entity.Wallet

class WalletsAdapter(val viewModel: WalletsViewModel, val owner: LifecycleOwner): RecyclerView.Adapter<WalletsAdapter.WalletItemViewHolder>() {

    private var wallets: List<Wallet> = arrayListOf()
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

    class WalletItemViewHolder(val itemBinding: WalletItemLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WalletItemViewHolder(
        DataBindingUtil.inflate<WalletItemLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.wallet_item_layout,
            parent,
            false
        ).apply{
            lifecycleOwner = owner
        }
    )

    override fun onBindViewHolder(holder: WalletItemViewHolder, position: Int) {
        holder.itemBinding.wallet = wallets[position]
        holder.itemBinding.walletCard.setOnClickListener {
            viewModel.onWalletClick(wallets[holder.adapterPosition].id)
        }
    }

    override fun getItemCount() = wallets.size
}