package com.example.expensetrracker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.Locale

class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 1){

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(db: SQLiteDatabase?) {
        val createExpenses = "CREATE TABLE expenses (id INTEGER PRIMARY KEY, color TEXT, name TEXT, sum INT, date TEXT)"
        val createLimitsPerMonth = "CREATE TABLE limits (id INTEGER PRIMARY KEY, max INT, yearMonth TEXT, spent INT)"

        db!!.execSQL(createExpenses)
        db.execSQL(createLimitsPerMonth)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropExpenses = "DROP TABLE IF EXISTS expenses"
        val dropLimits = "DROP TABLE IF EXISTS limits"

        db!!.execSQL(dropExpenses)
        db.execSQL(dropLimits)
        onCreate(db)
    }

    fun findLimit(yearMonth: String): Boolean {
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * FROM limits WHERE yearMonth='$yearMonth'", null)
        return result.moveToFirst()
    }

    fun addExpese(expense: Expense){
        val values = ContentValues()
        val textDate = sdf.format(expense.date)
        //values.put("id", null)
        values.put("color", expense.color)
        values.put("name", expense.name)
        values.put("sum", expense.sum)
        values.put("date", textDate)

        val db = this.writableDatabase
        db.insert("expenses", null, values)

        db.close()
    }

    fun getAllExpenses(yearMonth: String): List<Expense> {
        val expensesList = ArrayList<Expense>()
        val db = this.readableDatabase
        val date = LocalDate.parse("$yearMonth-01")
        val lastDay = date.with(TemporalAdjusters.lastDayOfMonth())
        val expensesPerMonthRequest = "SELECT * FROM expenses " +
                "WHERE date >= '$yearMonth-01' AND date <= '$lastDay' " +
                "ORDER BY date DESC"
        val cursor =  db.rawQuery(expensesPerMonthRequest, null)
        with(cursor){
            while(moveToNext()){
                val id = getInt(getColumnIndexOrThrow("id"))
                Log.d(null, id.toString())
                val color = getString(getColumnIndexOrThrow("color"))
                val name = getString(getColumnIndexOrThrow("name"))
                Log.d(null, name)
                val sum = getInt(getColumnIndexOrThrow("sum"))
                val dateString = getString(getColumnIndexOrThrow("date"))
                val date = sdf.parse(dateString)

                expensesList.add(Expense(id, color, name, sum, date))
            }
            close()
        }
        return expensesList.toList()
    }

    fun deleteExpense(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete("expenses", "id=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun addLimit(limit: Limit){
        val values = ContentValues()
        values.put("max", limit.max)
        if(limit.spent !== null) values.put("spend", limit.spent)
        values.put("yearMonth", limit.yearMonth)

        val db = this.writableDatabase

        if(findLimit(limit.yearMonth)){
            db.update("limits", values, "yearMonth=?", arrayOf(limit.yearMonth))
        }else{
        db.insert("limits", null, values)
        }

        db.close()
    }

    fun getLimit(yearMonth: String): Limit? {
        val db = this.readableDatabase
        var limit: Limit? = null
        val cursor = db.rawQuery("SELECT * FROM limits WHERE yearMonth='$yearMonth'", null)
        with(cursor){
            while (moveToNext()){
                val max = getInt(getColumnIndexOrThrow("max"))
                val date = getString(getColumnIndexOrThrow("yearMonth"))
                val spent = getInt(getColumnIndexOrThrow("spent"))
                limit = Limit(max, date, spent)
            }
        }
        return limit
    }

}