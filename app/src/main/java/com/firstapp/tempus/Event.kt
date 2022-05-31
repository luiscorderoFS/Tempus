package com.firstapp.tempus

class Event(title:String, start:String, leave:String) {
    var mTitle = title
    var mStart = start
    var mLeave = leave

}

class Month(){
    //var mDays = arrayOf(31,arrayListOf<Event>())//Array(arrayListOf<Event>())
    var mDays = Array(31,{arrayListOf<Event>()})

    fun addEvent(date:Int, events:ArrayList<Event>){
        mDays[date].addAll(events)
    }
}


