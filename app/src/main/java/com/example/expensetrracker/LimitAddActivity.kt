package com.example.expensetrracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.expensetrracker.dialog.MonthYearPickerDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class LimitAddActivity : AppCompatActivity() {

    private var selectedDate: String = SimpleDateFormat("yyyy-MM").format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limit_add)

        val limit = findViewById<EditText>(R.id.editLimitSum)
        val yearMonth = findViewById<TextView>(R.id.editLimitDate)
        val button = findViewById<Button>(R.id.buttonLimitAdd)

        yearMonth.setOnClickListener {
            val dialog: DialogFragment = MonthYearPickerDialog { year, month ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, 1)
                selectedDate = SimpleDateFormat("yyyy-MM").format(calendar.time)
                yearMonth.text = selectedDate
            }
            dialog.show(supportFragmentManager, "MonthYearPickerDialog")
        }

        button.setOnClickListener {
            val limit = Limit(limit.text.toString().toInt(), selectedDate, null)
            val db = DbHelper(this, null)
            db.addLimit(limit)
            Toast.makeText(this, "Лимит добавлен!", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}