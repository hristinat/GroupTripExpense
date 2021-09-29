package com.example.android.grouptripexpense

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.grouptripexpense.ExpenseAdapter.ExpenseViewHolder

class ExpenseAdapter : RecyclerView.Adapter<ExpenseViewHolder>() {
    private var expenses: List<Expense>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val context = parent.context
        val layoutIdForListItem = R.layout.expense
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForListItem, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses!![position]
        holder.expenseType.text = expense.type
        holder.amount.text = "%.2f".format(expense.amount)
        holder.paidBy.text = expense.memberName
    }

    override fun getItemCount(): Int {
        return if (null != expenses) expenses!!.size else 0
    }

    fun setExpenses(expenses: List<Expense>?) {
        this.expenses = expenses
        notifyDataSetChanged()
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var expenseType: TextView = itemView.findViewById(R.id.expense_type)
        var amount: TextView = itemView.findViewById(R.id.amount)
        var paidBy: TextView = itemView.findViewById(R.id.paid_by)
    }
}