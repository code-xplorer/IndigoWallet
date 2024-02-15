package com.ismail.creatvt.indigowallet.settings.wallets

import androidx.recyclerview.widget.DiffUtil
import com.ismail.creatvt.indigowallet.db.entity.Wallet

class WalletDiffCallback(val oldItems: List<Wallet>, val newItems: List<Wallet>): DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, newItem) = getItemPair(oldItemPosition, newItemPosition)
        return oldItem.id == newItem.id
    }

    private fun getItemPair(oldItemPosition: Int, newItemPosition: Int): Pair<Wallet, Wallet> {
        return Pair(oldItems[oldItemPosition], newItems[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, newItem) = getItemPair(oldItemPosition, newItemPosition)
        return oldItem.name == newItem.name &&
                oldItem.balance == newItem.balance &&
                oldItem.color == newItem.color &&
                oldItem.icon == newItem.icon
    }
}