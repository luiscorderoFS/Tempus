package com.firstapp.tempus

class Event(title:String, start:String, leave:String) {
    var mTitle = title
    var mStart = start
    var mLeave = leave

}

class Month(){
    var mDays:Array<ArrayList<Event>> = Array()

    init{

    }
    fun addDayEvent(day:Int, events:ArrayList<Event>){
        mDays[day-1].addAll(events)
    }
}