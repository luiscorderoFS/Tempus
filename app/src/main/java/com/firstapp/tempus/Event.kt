package com.firstapp.tempus

import java.io.Serializable

class Event(title:String, start:String, leave:String, location:String = "" , desc:String = "") : Serializable, Comparable<Event>{
    var mTitle = title
    var mStart = start
    var mLeave = leave
    var mLocation = location
    var mDesc = desc

    override fun compareTo(other: Event): Int = this.mStart.compareTo(other.mStart)

}




