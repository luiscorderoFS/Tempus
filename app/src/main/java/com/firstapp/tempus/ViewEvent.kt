package com.firstapp.tempus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import org.w3c.dom.Text
import java.io.Serializable

var globalEvent:Event = Event()
class ViewEvent : AppCompatActivity(), Serializable {

    lateinit var event:Event
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event)
        val title = findViewById<TextView>(R.id.view_title)
        val start = findViewById<TextView>(R.id.view_start)
        val location = findViewById<TextView>(R.id.view_location)
        val desc = findViewById<TextView>(R.id.view_desc)

        val bundle : Bundle?= intent.extras
        event = bundle!!.get("event") as Event
        globalEvent = event
        title.text = event.mTitle
        start.text = event.mTime
        location.text = event.mStartLocation
        desc.text = event.mDesc

    }

    override fun onResume() {
        super.onResume()
        val title = findViewById<TextView>(R.id.view_title)
        val start = findViewById<TextView>(R.id.view_start)
        val location = findViewById<TextView>(R.id.view_location)
        val desc = findViewById<TextView>(R.id.view_desc)
        if(globalEvent != event){
            title.text = globalEvent.mTitle
            start.text = globalEvent.mTime
            location.text = globalEvent.mStartLocation
            desc.text = globalEvent.mDesc
        }

    }

    // Method made for the Edit Event Button of this Activity's layout - Gabriel
    fun goToEditEvent(view: View){
        // Get the stored event object passed through the intent provided by the Main Event Activity - Gabriel
        val bundle : Bundle?= intent.extras
        val event:Event = bundle!!.get("event") as Event
        val position = bundle.get("position") as Int

        // Create a new intent with the same event object - Gabriel
        val intent = Intent(this@ViewEvent, EditEventActivity::class.java)
        intent.putExtra("event", event)
        intent.putExtra("position", position)
        // Start the next activity, being the Edit Event Activity - Gabriel
        startActivity(intent)
    }



}