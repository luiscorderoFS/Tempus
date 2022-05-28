package com.firstapp.tempus

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class CreateEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        /*val languages = resources.getStringArray(R.array.alert_options)
        val arrayAdapter = ArrayAdapter(this, R.layout.list_item, languages)
        val autoCompleteTV = findViewById<AutoCompleteTextView>(R.id.alert)
        autoCompleteTV.setAdapter(arrayAdapter)*/

        /*val listView = findViewById<ListView>(R.id.time_list)
        val arrayList = arrayOf("Start Time", "End Time")
        val arrayAdapter1: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList)
        listView.adapter = arrayAdapter1*/

        pickDate()

        findViewById<Button>(R.id.create).setOnClickListener() {
            //val tempEvent = Event(findViewById<TextInputEditText>(R.id.location_text).text(), )
        }
    }

    private fun getCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {

        findViewById<Button>(R.id.start_time).setOnClickListener {
            getCalendar()
            //TimePickerDialog(this, this, hour, minute, true).show()
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(hour)
                    .setMinute(minute)
                    .setTitleText("Start Time")
                    .build()
            MaterialTimePicker.Builder().setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
            picker.show(supportFragmentManager, "tag")
        }

        findViewById<Button>(R.id.date).setOnClickListener {
            getCalendar()
            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, day: Int, month: Int, year: Int) {
        savedDay = day
        savedMonth = month
        savedYear = year
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        savedHour = hour
        savedMinute = minute
    }

    class Event(val title: String, val location: String, val date: String, val startTime: String, val endTime: String, val arriveByTime: String, val alertOption: String){

    }
}