package com.lukasz.galinski.fluffy.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.TransactionsSingleItemBinding
import com.lukasz.galinski.fluffy.model.TransactionModel

class TransactionsAdapter(private val transactionsList: ArrayList<TransactionModel>) :
    RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val binding = TransactionsSingleItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionsViewHolder(binding)
    }

    override fun getItemCount() = transactionsList.size

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        with(holder) {
            with(transactionsList[position]) {
                binding.transactionName.text = name
                binding.transactionDescription.text = description
                binding.amount.text = holder.binding.root.resources.getString(R.string.amount, amount)
            }
        }
    }

    inner class TransactionsViewHolder(val binding: TransactionsSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}