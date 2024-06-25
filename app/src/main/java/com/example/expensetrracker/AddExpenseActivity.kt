package com.example.expensetrracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import java.util.Date

class AddExpenseActivity : AppCompatActivity() {

    private var selectedDate: Long = Date().time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val name = findViewById<TextView>(R.id.editExpenseName)
        val color = findViewById<TextView>(R.id.editExpenseColor)
        val date = findViewById<CalendarView>(R.id.editLimitDate)
        val price = findViewById<TextView>(R.id.editExpensePrice)
        val button = findViewById<Button>(R.id.addExpenseButton)

        //date.setDate(Date().getTime())
        date.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = java.util.Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis
        }

        button.setOnClickListener {
            val expense = Expense(null, color.text.toString().trim(), name.text.toString().trim(), price.text.toString().toInt(), Date(selectedDate))

            val db = DbHelper(this, null)
            db.addExpese(expense)
            Toast.makeText(this, "Расходы добавлены в истоию!", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}