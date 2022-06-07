package com.firstapp.tempus
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import javax.xml.transform.ErrorListener

var monthTest:Month = Month()

class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    //region OnClickListener stuff
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int){
        }
    }

    fun setOnItemClickListener(listener : onItemClickListener){
        mListener = listener
    }

    //endregion
    private var test:Event = Event("Test","Test","Test")
    private var test2:Event = Event("Test2","Test2","Test2")
    private var test3:Event = Event("Test3","Test3","Test3", "This is just a test of hidden memberVaribles ")
    private var groupOfTest = arrayListOf(test,test2,test3)
    private var test4:Event = Event("Test4","Test4","Test4")
    private var test5:Event = Event("Test5","Test5","Test5")
    private var test6:Event = Event("Test6","Test6","Test6")
    private var groupOfTest2 = arrayListOf(test4,test5,test6)


    //this initializes the view to the current month, further we can have a check to see if the month
    //has changed at all
    init{
        monthTest.addEvents(6,groupOfTest)
        monthTest.addEvents(8,groupOfTest2)

    }
    var recyclerDate = Calendar.getInstance().get(Calendar.DATE)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_layout,parent,false)

        //use this space here to initialize the starting date's size and maybe
        //mess around with the listeners properties
        return ViewHolder(v,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newPos = position + 1

        holder.eventTitle.text = monthTest.mDays[recyclerDate-1][position].mTitle
        holder.eventNumber.text = "${newPos}.)"
        holder.eventStart.text = monthTest.mDays[recyclerDate-1][position].mStart
        holder.eventLeaveBy.text = monthTest.mDays[recyclerDate-1][position].mLeave


    }

    override fun getItemCount(): Int {
        return monthTest.mDays[recyclerDate-1].size
    }

    //this function is used by the listener in MainActivity.kt to know what date we're in
    fun changeDate(date: Int){
        this.recyclerDate = date
    }

    inner class ViewHolder(itemView: View,listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        var eventNumber: TextView
        var eventTitle: TextView
        var eventStart: TextView
        var eventLeaveBy: TextView

        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }


            eventNumber = itemView.findViewById(R.id.event_number)
            eventTitle = itemView.findViewById(R.id.event_title)
            eventStart = itemView.findViewById(R.id.event_start)
            eventLeaveBy = itemView.findViewById(R.id.event_leave_by)
        }
    }

    class MarginItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = spaceSize
                }
                left = spaceSize
                right = spaceSize
                bottom = spaceSize
            }
        }
    }

}