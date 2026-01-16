package com.ssba.savvy_savings_app.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssba.savvy_savings_app.R
import com.ssba.savvy_savings_app.data.AppDatabase
import com.ssba.savvy_savings_app.entities.Expense
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseCategoryTransactionHistoryAdapter(private var expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseCategoryTransactionHistoryAdapter.ExpenseCategoryTransactionViewHolder>()
{
    inner class ExpenseCategoryTransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivTransactionType)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTransactionTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvTransactionDate)
        val tvType: TextView = itemView.findViewById(R.id.tvTransactionType) // Line 43: Now works!
        val tvAmount: TextView = itemView.findViewById(R.id.tvTransactionAmount)
        val tvTransactionCategory: TextView = itemView.findViewById(R.id.tvTransactionCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseCategoryTransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ExpenseCategoryTransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseCategoryTransactionViewHolder, position: Int)
    {
        val transaction = expenses[position]
        val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val context = holder.itemView.context
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

        holder.ivIcon.setImageResource(R.drawable.ic_transaction_expense)
        holder.tvTitle.text = transaction.title
        holder.tvDate.text = dateFormatter.format(transaction.date)
        holder.tvType.text = "Expense"
        holder.tvAmount.text = currencyFormat.format(transaction.amount)

        // Use branded color
        holder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.expense_neon))

        val db: AppDatabase = AppDatabase.getInstance(context)

        CoroutineScope(Dispatchers.Main).launch {
            val category = withContext(Dispatchers.IO) {
                db.expenseCategoryDao().getExpenseCategoryById(transaction.categoryId)
            }

            holder.tvTransactionCategory.text = category?.name ?: "No Category"

            holder.itemView.setOnClickListener {
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_expense_transaction, null)
                val dialog = AlertDialog.Builder(context).setView(dialogView).create()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()

                dialogView.findViewById<TextView>(R.id.tvExpenseTitle).text = transaction.title
                dialogView.findViewById<TextView>(R.id.tvExpenseDate).text = dateFormatter.format(transaction.date)
                dialogView.findViewById<TextView>(R.id.tvExpenseAmount).apply {
                    text = currencyFormat.format(transaction.amount)
                    setTextColor(ContextCompat.getColor(context, R.color.expense_neon))
                }
                dialogView.findViewById<TextView>(R.id.tvExpenseDescription).text = transaction.description
                dialogView.findViewById<TextView>(R.id.tvExpenseCategory).text = holder.tvTransactionCategory.text

                val ivReceipt = dialogView.findViewById<ImageView>(R.id.ivReceipt)
                if (transaction.receiptPictureUrl.isNotBlank()) {
                    Glide.with(context).load(transaction.receiptPictureUrl).into(ivReceipt)
                } else {
                    ivReceipt.visibility = View.GONE
                }

                dialogView.findViewById<Button>(R.id.btnCloseExpense).setOnClickListener { dialog.dismiss() }
            }
        }
    }

    override fun getItemCount(): Int = expenses.size
}