package com.example.expensetrracker.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class MonthYearPickerDialog(private val listener: (year: Int, month: Int) -> Unit) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val dialog = DatePickerDialog(requireContext(), this, year, month, 1)
        dialog.datePicker.findViewById<View>(
            requireContext().resources.getIdentifier("day", "id", "android")
        )?.visibility = View.GONE
        return dialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener(year, month)
    }
}
