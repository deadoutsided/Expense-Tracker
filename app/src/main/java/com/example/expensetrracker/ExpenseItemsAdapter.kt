package com.example.expensetrracker

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrracker.view.CustomCircleView

class ExpenseItemsAdapter(var expenses: List<Expense>?, var context: Context) : RecyclerView.Adapter<ExpenseItemsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val color: CustomCircleView = view.findViewById(R.id.circleView)
        val name: TextView = view.findViewById(R.id.expenseName)
        val sum: TextView = view.findViewById(R.id.expenseSum)
        val trashButton: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return expenses?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val expense = expenses?.get(position)
        if (expense != null) {
            val colorInt = try {
                // Convert hex string to int
                Color.parseColor(expense.color)
            } catch (e: IllegalArgumentException) {
                // Fallback color if parsing fails
                0xFFFF0000.toInt()
            }
            //Log.d(null, "$colorInt")
            holder.color.setCircleColor(colorInt)
            holder.name.text = expense.name
            holder.sum.text = expense.sum.toString()
        } else {
            // случай, когда бд пуста
            holder.color.setCircleColor(0xFFFF0000.toInt()) // Default color
            holder.name.text = "Название"
            holder.sum.text = "0"
        }
        holder.trashButton.setOnClickListener {
            val db = DbHelper(context, null)
            if (expense != null) {
                if(expense.id !== null)
                    db.deleteExpense(expense.id)
            }
            removeExpense(position)
        }
    }

    private fun removeExpense(position: Int) {
        val updatedExpenses = expenses?.toMutableList()
        updatedExpenses?.removeAt(position)
        expenses = updatedExpenses
        notifyItemRemoved(position)
    }

    fun updateExpenses(newExpenses: List<Expense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }

}