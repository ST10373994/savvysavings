package com.ssba.savvy_savings_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ssba.savvy_savings_app.R
import com.ssba.savvy_savings_app.entities.Expense
import com.ssba.savvy_savings_app.entities.Income
import com.ssba.savvy_savings_app.entities.Saving
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionHistoryAdapter(private val transactions: List<Any>) :
    RecyclerView.Adapter<TransactionHistoryAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // These IDs must exist in item_transaction.xml
        val ivIcon: ImageView = itemView.findViewById(R.id.ivTransactionType)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTransactionTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvTransactionDate)
        val tvAmount: TextView = itemView.findViewById(R.id.tvTransactionAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.itemView.context
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        when (transaction) {
            is Income -> {
                holder.tvTitle.text = transaction.title
                holder.tvDate.text = dateFormat.format(transaction.date)
                holder.tvAmount.text = "+ ${currencyFormat.format(transaction.amount)}"
                holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.income_neon))
                holder.ivIcon.setImageResource(R.drawable.ic_income_arrow_unfilled)
                holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.income_neon))
            }
            is Expense -> {
                holder.tvTitle.text = transaction.title
                holder.tvDate.text = dateFormat.format(transaction.date)
                holder.tvAmount.text = "- ${currencyFormat.format(transaction.amount)}"
                holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.expense_neon))
                holder.ivIcon.setImageResource(R.drawable.ic_expense_arrow_unfilled)
                holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.expense_neon))
            }
            is Saving -> {
                holder.tvTitle.text = transaction.title
                holder.tvDate.text = dateFormat.format(transaction.date)
                holder.tvAmount.text = currencyFormat.format(transaction.amount)
                holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.savings_neon))
                holder.ivIcon.setImageResource(R.drawable.ic_savings_unfilled_white)
                holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.savings_neon))
            }
        }
    }

    override fun getItemCount(): Int = transactions.size
}