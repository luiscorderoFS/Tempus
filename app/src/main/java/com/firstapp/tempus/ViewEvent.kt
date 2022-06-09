package com.firstapp.tempus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.io.Serializable

class ViewEvent : AppCompatActivity(), Serializable {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event)

        val title = findViewById<TextView>(R.id.view_title)
        val start = findViewById<TextView>(R.id.view_start)
        val end = findViewById<TextView>(R.id.view_end)
        val desc = findViewById<TextView>(R.id.view_desc)

        val bundle : Bundle?= intent.extras
        val event:Event = bundle!!.get("event") as Event

        title.text = event.mTitle
        start.text = event.mTime
        end.text = event.mDate
        desc.text = event.mDesc

    }
}