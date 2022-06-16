package com.firstapp.tempus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstapp.tempus.BuildConfig.MAPS_API_KEY
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

var localMonth:Month = Month()

class MainActivity : AppCompatActivity() {

    // Create the authentication variable - Gabriel
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    private lateinit var calText:String

    // Upon starting this screen, evaluate if the user is signed in or not - Gabriel
    override fun onStart(){
        super.onStart()
        // Create notification channel on startup
        Notifications.create().createNotificationChannel(this)
        // If not, got to the login/register screen - Gabriel
        if(auth.currentUser == null){
            startActivity(Intent(this, LoginOrRegisterActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the authentication variable - Gabriel
        auth = Firebase.auth
        db = Firebase.firestore

        // Initialize Google Places API
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, MAPS_API_KEY)
        }

        // Temporary line of code to correct any issues with authenticating after logging in once (just sign the user out before anything occurs) - Gabriel
        //auth.signOut()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val calPlaceHolder = Calendar.getInstance()
        var day = calPlaceHolder.get(Calendar.DAY_OF_MONTH)

        //region Text view for date change

        //Initializes the textView to display the current date
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val currentDate = sdf.format(Date())
        textView.text = currentDate

        //endregion

        //region RecycleView

        //next few lines initialize the recyclerview
        val recyclerEvent = findViewById<RecyclerView>(R.id.recyclerEvent)
        layoutManager = LinearLayoutManager(this)
        recyclerEvent.layoutManager = layoutManager
        adapter = RecyclerAdapter()
        (adapter as RecyclerAdapter).changeDate(day)



        //Formatting of the recyclerView
        recyclerEvent.addItemDecoration(
            RecyclerAdapter.MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin))
        )
        //endregion

        //region Initializes the month via firebase

        db.collection("Users").document(auth.uid.toString()).collection(currentDate.substring(6))
            .document(currentDate.substring(0, 2)).collection("Events")
            .get()
            .addOnSuccessListener { result->
                for(document in result){
                    val eventPlaceHolder = document.toObject<Event>()
                    val datePlaceHolder = eventPlaceHolder.mDate.substring(3,5).toInt()
                    localMonth.addEvent(datePlaceHolder-1,eventPlaceHolder)
                }
            }
            .addOnFailureListener{ exception ->
                Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()

            }

        //endregion

        //region listener that checks when the date changes, allows the text box to show the selected day
        calendarView.setOnDateChangeListener{view,year,month,dayOfMonth ->
            val newMonth = month + 1
            calText = "$newMonth/$dayOfMonth/$year"
            day = dayOfMonth

            // if check to see what month we're in


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

    fun goToCreate(view: View){
        val intent = Intent(this, CreateEventActivity::class.java)
        //intent.putExtra("selectedDate",calText)
        startActivity(intent)
    }

    /*fun createNotificationChannel() {
        // if api >= 26, requires notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // build notification channel
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
            //.apply {
            // do whatever with notification

            //}
            // create system notification manager
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // create notification channel
            manager.createNotificationChannel(channel)
        }
    }*/
}