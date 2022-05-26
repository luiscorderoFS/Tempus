package com.firstapp.tempus
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var test:Event = Event("Test","Test","Test")
    private var test2:Event = Event("Test2","Test2","Test2")
    private var test3:Event = Event("Test3","Test3","Test3")
    private var groupOfTest = arrayListOf(test,test2,test3)

    private var monthTest:Month = Month()

    private var date = 0
    /*private var title = arrayOf("Event 1", "Event 2", "Event 3")
    private var start = arrayOf("Start: 10 AM", "Start: 12 AM", "Start: 5 PM")
    private var leaveBy = arrayOf("Leave by: 9 AM","Leave by: 11 AM","Leave by: 4 PM")*/

    private var title1 = arrayOf("New Event", "adsasdasd 2", "asdasd 3","test")
    private var start1 = arrayOf("Start: 14 AM", "Start: 123 AM", "Start: 123123 PM","test")
    private var leaveBy1 = arrayOf("Leave by: 123 AM","Leave by: 123 AM","Leave by: 321 PM","test")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_layout,parent,false)
        /*groupOfTest.add(test)
        groupOfTest.add(test2)
        groupOfTest.add(test3)*/
        //use this space here to initialize the starting date's size and maybe
        //mess around with the listeners properties
        monthTest.addDayEvent(25,groupOfTest)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val newPos = position + 1
        if(date == 26) {
            /*holder.eventTitle.text = groupOfTest[position].mTitle
            holder.eventNumber.text = "${newPos}.)"
            holder.eventStart.text = groupOfTest[position].mStart
            holder.eventLeaveBy.text = groupOfTest[position].mLeave*/
            holder.eventTitle.text = monthTest.mDays[26][position].mTitle
            holder.eventStart.text = monthTest.mDays[26][position].mStart
            holder.eventLeaveBy.text = monthTest.mDays[26][position].mLeave

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
            return groupOfTest.size
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