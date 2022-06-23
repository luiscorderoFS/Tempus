package com.firstapp.tempus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

var localMonth: Month = Month()
class MainActivity : AppCompatActivity() {

    // Create the authentication variable - Gabriel
    var auth: FirebaseAuth = Firebase.auth
    var db: FirebaseFirestore = Firebase.firestore

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
    private lateinit var recyclerEvent : RecyclerView
    val calPlaceHolder = Calendar.getInstance()

    private lateinit var calText:String
    var day = calPlaceHolder.get(Calendar.DAY_OF_MONTH)
    var monthCheck = calPlaceHolder.get(Calendar.MONTH)+1
    // Upon starting this screen, evaluate if the user is signed in or not - Gabriel
    override fun onStart(){
        super.onStart()
        // If not, got to the login/register screen - Gabriel
        if(auth.currentUser == null){
            startActivity(Intent(this, LoginOrRegisterActivity::class.java))
        }

        //next few lines initialize the recyclerview
        recyclerEvent = findViewById<RecyclerView>(R.id.recyclerEvent)
        layoutManager = LinearLayoutManager(this)
        recyclerEvent.layoutManager = layoutManager
        (adapter as RecyclerAdapter).changeDate(day)
        recyclerEvent.adapter = adapter



        //Formatting of the recyclerView
        recyclerEvent.addItemDecoration(
            RecyclerAdapter.MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin))
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the authentication variable - Gabriel
        //auth = Firebase.auth
        //db = Firebase.firestore

        // Temporary line of code to correct any issues with authenticating after logging in once (just sign the user out before anything occurs) - Gabriel
        //auth.signOut()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        //region Text view for date change

        //Initializes the textView to display the current date
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val currentDate = sdf.format(Date())
        textView.text = currentDate

        //endregion


        //region Initializes the month via firebase

        initializeDatabase(currentDate)

        //endregion


        //region RecycleView

        adapter = RecyclerAdapter()

//        //next few lines initialize the recyclerview
//        val recyclerEvent = findViewById<RecyclerView>(R.id.recyclerEvent)
//        layoutManager = LinearLayoutManager(this)
//        recyclerEvent.layoutManager = layoutManager
//        adapter = RecyclerAdapter()
//        (adapter as RecyclerAdapter).changeDate(day)
//        recyclerEvent.adapter = adapter
//
//
//
//        //Formatting of the recyclerView
//        recyclerEvent.addItemDecoration(
//            RecyclerAdapter.MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin))
//        )

        //endregion



        //region listener that checks when the date changes, allows the text box to show the selected day
        calendarView.setOnDateChangeListener{view,year,month,dayOfMonth ->
            val newMonth = month + 1
            calText = "0$newMonth/$dayOfMonth/$year"
            day = dayOfMonth

            // if check to see what month we're in
            if(monthCheck != newMonth){
                monthCheck = newMonth
                localMonth.clear()
                initializeDatabase("0$newMonth/$dayOfMonth/$year")

            }

            textView.text = calText
            //allows the listener to be able to dynamically change the recycle view
            (adapter as RecyclerAdapter).changeDate(day)
            recyclerEvent.adapter = adapter


        }
        //endregion

        //region A listener that allows the user to click on an Event from the view, and
        //brings up it's full view
        (adapter as RecyclerAdapter).setOnItemClickListener(object: RecyclerAdapter.onItemClickListener{
            override fun onItemClick(position:Int){

                //Toast.makeText(this@MainActivity, "You clicked on item no. ${position+1}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity,ViewEvent::class.java)
                intent.putExtra("event", localMonth.mDays[day-1][position])

                startActivity(intent)
            }
        })
        //endregion
    }

    private fun initializeDatabase(currentDate: String){
        db.collection("Users").document(auth.uid.toString()).collection(currentDate.substring(6))
            .document(currentDate.substring(0, 2)).collection("Events")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val eventPlaceHolder = document.toObject<Event>()
                    val datePlaceHolder = eventPlaceHolder.mDate.substring(3, 5).toInt()
                    localMonth.addEvent(datePlaceHolder - 1, eventPlaceHolder)
                }
            }
        TimeUnit.SECONDS.sleep(1L)

    }

    fun goToCreate() {
        val intent = Intent(this@MainActivity, CreateEventActivity::class.java)
        intent.putExtra("selectedMonth","0$monthCheck")
        startActivity(intent)

    }

    fun logOutAndGoToLogIn() {
        localMonth.clear()
        auth.signOut()
        startActivity(Intent(this, LoginOrRegisterActivity::class.java))
    }
}