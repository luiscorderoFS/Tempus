package com.firstapp.tempus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        //region Text view for date change

        //Initializes the textView to display the current date
        val sdf = SimpleDateFormat("M/dd/yyyy", Locale.US)
        val currentDate = sdf.format(Date())
        textView.text = currentDate

        //a listener that checks when the date changes, allows the text box to show the selected day
        calendarView.setOnDateChangeListener{view,year,month,dayOfMonth ->
            val newMonth = month + 1
            val calText = "$newMonth/$dayOfMonth/$year"
            textView.text = calText
        }
        //endregion

    }
}