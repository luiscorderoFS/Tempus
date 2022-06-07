package com.firstapp.tempus

import java.io.Serializable

class Event(title:String, start:String, leave:String, desc:String = "") : Serializable{
    var mTitle = title
    var mStart = start
    var mLeave = leave
    var mDesc = desc
}




