package com.example.expensetrracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrracker.dialog.MonthYearPickerDialog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private var selectedDate: String = SimpleDateFormat("yyyy-MM").format(Date())
    private lateinit var expenseAdapter: ExpenseItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val monthView = findViewById<TextView>(R.id.mainMonth)
        val sums = findViewById<TextView>(R.id.mainSums)
        val mainButton = findViewById<Button>(R.id.mainButton)

        val db = DbHelper(this, null)

        val actualLimit = db.getLimit(selectedDate)

        // Initialize RecyclerView and its adapter
        val expenseList: RecyclerView = findViewById(R.id.expensesList)
        expenseList.layoutManager = LinearLayoutManager(this)
        expenseAdapter = ExpenseItemsAdapter(emptyList(), this)
        expenseList.adapter = expenseAdapter

        // Load expenses for the current month
        monthView.text = selectedDate
        loadExpenses()

        monthView.setOnClickListener {
            showMonthYearPickerDialog(monthView)
        }

        mainButton.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }

        sums.setOnClickListener {
            val intent = Intent(this, LimitAddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showMonthYearPickerDialog(monthView: TextView) {
        val dialog: DialogFragment = MonthYearPickerDialog { year, month ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, 1)
            selectedDate = SimpleDateFormat("yyyy-MM").format(calendar.time)
            monthView.text = selectedDate
            loadExpenses()
        }
        dialog.show(supportFragmentManager, "MonthYearPickerDialog")
    }

    private fun loadExpenses() {
        val sums = findViewById<TextView>(R.id.mainSums)
        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val db = DbHelper(this, null)
        val expenses = db.getAllExpenses(selectedDate)
        var spentMoney = 0;
        expenses.forEach { spentMoney += it.sum }
        val newActualLimit = db.getLimit(selectedDate)?.max ?: 0
        progress.setProgress(round((spentMoney.toDouble() / newActualLimit.toDouble()) * 100).toInt(), true)
        sums.text = "$spentMoney/$newActualLimit"
        expenseAdapter.updateExpenses(expenses ?: emptyList())
    }
}
