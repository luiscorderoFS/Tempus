package com.firstapp.tempus
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var title = arrayOf("Event 1", "Event 2", "Event 3")
    private var number = arrayOf("1.)", "2.)","3.)")
    private var start = arrayOf("Start: 10 AM", "Start: 12 AM", "Start: 5 PM")
    private var leaveBy = arrayOf("Leave by: 9 AM","Leave by: 11 AM","Leave by: 4 PM")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.eventTitle.text = title[position]
        holder.eventNumber.text = number[position]
        holder.eventStart.text = start[position]
        holder.eventLeaveBy.text = leaveBy[position]
    }

    override fun getItemCount(): Int {
        return title.size

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