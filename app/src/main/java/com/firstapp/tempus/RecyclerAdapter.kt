package com.firstapp.tempus
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    private var date = 0
    private var title = arrayOf("Event 1", "Event 2", "Event 3")
    private var start = arrayOf("Start: 10 AM", "Start: 12 AM", "Start: 5 PM")
    private var leaveBy = arrayOf("Leave by: 9 AM","Leave by: 11 AM","Leave by: 4 PM")

    private var title1 = arrayOf("New Event", "adsasdasd 2", "asdasd 3","test")
    private var start1 = arrayOf("Start: 14 AM", "Start: 123 AM", "Start: 123123 PM","test")
    private var leaveBy1 = arrayOf("Leave by: 123 AM","Leave by: 123 AM","Leave by: 321 PM","test")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        var newPos = position + 1
        if(date == 26) {
            holder.eventTitle.text = title[position]
            holder.eventNumber.text = "${newPos}.)"
            holder.eventStart.text = start[position]
            holder.eventLeaveBy.text = leaveBy[position]
        }
        if(date == 27){
            holder.eventTitle.text = title1[position]
            holder.eventNumber.text = "${newPos}.)"
            holder.eventStart.text = start1[position]
            holder.eventLeaveBy.text = leaveBy1[position]
        }
    }

    override fun getItemCount(): Int {

        if(date == 26){
            return title.size
        }
        else if(date == 27){
            return title1.size
        }
        return 0


    }

    fun changeDate(date: Int){
        this.date = date
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var eventNumber: TextView
        var eventTitle: TextView
        var eventStart: TextView
        var eventLeaveBy: TextView

        init{
            eventNumber = itemView.findViewById(R.id.event_number)
            eventTitle = itemView.findViewById(R.id.event_title)
            eventStart = itemView.findViewById(R.id.event_start)
            eventLeaveBy = itemView.findViewById(R.id.event_leave_by)
        }
    }


}