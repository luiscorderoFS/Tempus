package com.firstapp.tempus

import java.io.Serializable

class Event(title:String, time:String, location:String, date:String = "", NumID:String = "", UserID:String = "", DocID:String = "", desc:String = "") : Serializable, Comparable<Event>{
    constructor(): this("","","","","","","","")
    var mTitle = title
    var mTime = time
    var mDate = date
    var mLocation = location
    var mNumID = NumID
    var mUserID = UserID
    var mDocID = DocID
    var mDesc = desc

    override fun compareTo(other: Event): Int {
        val thisString = this.mTime
        val otherString = other.mTime
        var thisTime:Int = thisString.filter { it.isDigit() }.toInt()
        var otherTime:Int = otherString.filter { it.isDigit() }.toInt()
        //checks whether time is in AM or PM and then converts to 24H by removing
        if(thisString.contains("P")) thisTime += 1200
        if(otherString.contains("P")) otherTime += 1200


        return (thisTime.compareTo(otherTime))
    }

}




