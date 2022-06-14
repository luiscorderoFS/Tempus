package com.firstapp.tempus

import java.io.Serializable

class Event(title:String, time:String, location:String, date:String = "", NumID:String = "", UserID:String = "", DocID:String = "", desc:String = "") : Serializable, Comparable<Event>{
    var mTitle = title
    var mTime = time
    var mDate = date
    var mLocation = location
    var mNumID = NumID
    var mUserID = UserID
    var mDocID = DocID
    var mDesc = desc

    override fun compareTo(other: Event): Int = this.mTime.compareTo(other.mTime)

}




