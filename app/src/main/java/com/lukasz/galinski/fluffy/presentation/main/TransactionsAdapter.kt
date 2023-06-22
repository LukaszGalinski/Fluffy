package com.lukasz.galinski.fluffy.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.core.domain.TransactionCategories
import com.lukasz.galinski.core.domain.TransactionType
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.TransactionsSingleItemBinding

class TransactionsAdapter :
    RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>() {

    var transactionsList: List<Transaction> = listOf()
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
                binding.amount.text =
                    holder.binding.root.resources.getString(getAmountStringPattern(type), amount)
                binding.amount.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        getAmountTextColor(type)
                    )
                )
                binding.categoryIcon.setImageResource(
                    getCategoryImageResource(TransactionCategories.valueOf(category))
                )
            }
        }
    }

    inner class TransactionsViewHolder(val binding: TransactionsSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun getAmountTextColor(type: String): Int {
        return if (type == TransactionType.INCOME.label) {
            R.color.dark_green
        } else R.color.dark_red
    }

    private fun getAmountStringPattern(type: String): Int {
        return if (type == TransactionType.INCOME.label) {
            R.string.amount_income_pattern
        } else R.string.amount_outcome_pattern
    }

    private fun getCategoryImageResource(category: TransactionCategories): Int {
        return when (category) {
            TransactionCategories.Car -> R.drawable.icon_income
            TransactionCategories.Food -> R.drawable.icon_outcome
            TransactionCategories.Other -> R.drawable.icon_shop
        }
    }
}