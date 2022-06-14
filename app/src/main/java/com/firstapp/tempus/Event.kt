package com.firstapp.tempus

import java.io.Serializable

class Event(title:String, time:String, location:String, date:String = "", desc:String = "") : Serializable, Comparable<Event>{
    var mTitle = title
    var mTime = time
    var mDate = date
    var mLocation = location
    var mDesc = desc

    override fun compareTo(other: Event): Int = this.mTime.compareTo(other.mTime)

}




