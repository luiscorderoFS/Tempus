package com.firstapp.tempus
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

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

    var recyclerDate = Calendar.getInstance().get(Calendar.DATE)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_layout,parent,false)
        return ViewHolder(v,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newPos = position + 1

        holder.eventTitle.text = localMonth.mDays[recyclerDate-1][position].mTitle
        holder.eventNumber.text = "${newPos}.)"
        holder.eventTime.text = "Start: ${localMonth.mDays[recyclerDate-1][position].mTime}"
        holder.eventLocation.text = localMonth.mDays[recyclerDate-1][position].mLocation


    }

    override fun getItemCount(): Int {
        return localMonth.mDays[recyclerDate-1].size
    }

    //this function is used by the listener in MainActivity.kt to know what date we're in
    fun changeDate(date: Int){
        this.recyclerDate = date
    }

    inner class ViewHolder(itemView: View,listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        var eventNumber: TextView
        var eventTitle: TextView
        var eventTime: TextView
        var eventLocation: TextView

        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }

            eventNumber = itemView.findViewById(R.id.event_number)
            eventTitle = itemView.findViewById(R.id.event_title)
            eventTime = itemView.findViewById(R.id.event_time)
            eventLocation = itemView.findViewById(R.id.event_location)
        }
    }

    //Formatting - Gives each element in the view to have a little space
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