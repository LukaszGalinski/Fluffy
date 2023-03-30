package com.lukasz.galinski.fluffy.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.TransactionsSingleItemBinding

class TransactionsAdapter :
    RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>() {

    var transactionsList: ArrayList<Transaction> = arrayListOf()
        set(value) {
            field = value
            notifyItemRangeChanged(0, transactionsList.size)
        }

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
                binding.amount.text = holder.binding.root.resources.getString(getAmountStringPattern(type!!), amount)
                binding.amount.setTextColor(ContextCompat.getColor(itemView.context, getAmountTextColor(type!!)))
            }
        }
    }

    inner class TransactionsViewHolder(val binding: TransactionsSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun getAmountTextColor(type: String): Int {
        return if (type == TransactionType.INCOME.label){
            R.color.dark_green
        } else R.color.dark_red
    }

    private fun getAmountStringPattern(type: String): Int {
        return if (type == TransactionType.INCOME.label){
            R.string.amount_income_pattern
        } else R.string.amount_outcome_pattern
    }
}