package com.firstapp.tempus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val calPlaceHolder = Calendar.getInstance()
        var day = calPlaceHolder.get(Calendar.DAY_OF_MONTH)


        //region Text view for date change

        //Initializes the textView to display the current date
        val sdf = SimpleDateFormat("M/dd/yyyy", Locale.US)
        val currentDate = sdf.format(Date())
        textView.text = currentDate

        //a listener that checks when the date changes, allows the text box to show the selected day
        calendarView.setOnDateChangeListener{view,year,month,dayOfMonth ->
            val newMonth = month + 1
            val calText = "$newMonth/$dayOfMonth/$year"
            day = dayOfMonth

            textView.text = calText
            val recyclerEvent = findViewById<RecyclerView>(R.id.recyclerEvent)
            layoutManager = LinearLayoutManager(this)

            recyclerEvent.layoutManager = layoutManager
            //allows the listener to be able to dynamically change the recycle view
            adapter = RecyclerAdapter()
            (adapter as RecyclerAdapter).changeDate(day)
            recyclerEvent.adapter = adapter

        }
        //endregion

        //region RecycleView
        val recyclerEvent = findViewById<RecyclerView>(R.id.recyclerEvent)
        layoutManager = LinearLayoutManager(this)

        recyclerEvent.layoutManager = layoutManager

        adapter = RecyclerAdapter()
        (adapter as RecyclerAdapter).changeDate(day)
        recyclerEvent.adapter = adapter
        recyclerEvent.addItemDecoration(
            RecyclerAdapter.MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin))
        )
        //endregion
    }
}