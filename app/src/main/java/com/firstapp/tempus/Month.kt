package com.firstapp.tempus


class Month(){
    //var mDays = arrayOf(31,arrayListOf<Event>())//Array(arrayListOf<Event>())
    var mDays = Array(31,{arrayListOf<Event>()})

    fun addEvents(date:Int, events:ArrayList<Event>){
        mDays[date].addAll(events)
    }

}