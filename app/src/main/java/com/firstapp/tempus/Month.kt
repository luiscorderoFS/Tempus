package com.firstapp.tempus

import java.util.*
import kotlin.collections.ArrayList


class Month() {
    //var mDays = arrayOf(31,arrayListOf<Event>())//Array(arrayListOf<Event>())
    var mDays = Array(31,{arrayListOf<Event>()})

    fun addEvents(date:Int, events:ArrayList<Event>){
        mDays[date].addAll(events)
        mDays[date].sort()

    }

    fun addEvent(date:Int , event : Event){
        mDays[date].add(event)
        mDays[date].sort()
    }

    /*fun randomize(){
        var randy = Random()
        for (i in 0..30){
            this.addEvent(i,Event("Test 1",randy.nextInt(24).toString(),randy.nextInt().toString()))
            this.addEvent(i,Event("Test 2",randy.nextInt(24).toString(),randy.nextInt().toString()))
            this.addEvent(i,Event("Test 3",randy.nextInt(24).toString(),randy.nextInt().toString()))

        }
    }*/
    fun isEmpty():Boolean{
        for(i in 0..30){
            if(mDays[i].isNotEmpty()){
                return false
            }
        }
        return true
    }
    // Clear each index in mDays - Gabriel
    fun clear(){
        for(i in 0..30){
            if(this.mDays[i] != null){
                this.mDays[i].clear()
            }
        }
    }
}