package com.firstapp.tempus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // Create the authentication variable - Gabriel
    private lateinit var auth: FirebaseAuth

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    // Upon starting this screen, evaluate if the user is signed in or not - Gabriel
    override fun onStart(){
        super.onStart()
        // If not, got to the login/register screen - Gabriel
        if(auth.currentUser == null){
            startActivity(Intent(this, LoginOrRegisterActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize the authentication variable - Gabriel
        auth = Firebase.auth
        // Temporary line of code to correct any issues with authenticating after logging in once (just sign the user out before anything occurs) - Gabriel
        //auth.signOut()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val calPlaceHolder = Calendar.getInstance()
        var day = calPlaceHolder.get(Calendar.DAY_OF_MONTH)

        //init the month
        monthTest = juneTest

        //region Text view for date change

        //Initializes the textView to display the current date
        val sdf = SimpleDateFormat("M/dd/yyyy", Locale.US)
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
        recyclerEvent.adapter = adapter



        //Formatting of the recyclerView
        recyclerEvent.addItemDecoration(
            RecyclerAdapter.MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin))
        )
        //endregion



        //a listener that checks when the date changes, allows the text box to show the selected day
        calendarView.setOnDateChangeListener{view,year,month,dayOfMonth ->
            val newMonth = month + 1
            val calText = "$newMonth/$dayOfMonth/$year"
            day = dayOfMonth

            // if check to see what month we're in
            if(newMonth == 6){
                monthTest = juneTest
            }
            else if(newMonth == 7){
                monthTest = julyTest
            }

            textView.text = calText
            //allows the listener to be able to dynamically change the recycle view
            (adapter as RecyclerAdapter).changeDate(day)
            recyclerEvent.adapter = adapter


        }


        (adapter as RecyclerAdapter).setOnItemClickListener(object: RecyclerAdapter.onItemClickListener{
            override fun onItemClick(position:Int){

                //Toast.makeText(this@MainActivity, "You clicked on item no. ${position+1}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity,ViewEvent::class.java)
                intent.putExtra("event", monthTest.mDays[day-1][position])

                startActivity(intent)
            }
        })
    }

    fun goToCreate(view: View){
        startActivity(Intent(this, CreateEventActivity::class.java))
    }

    fun logOutAndGoToLogIn(view:View){
        monthTest.clear()
        auth.signOut()
        startActivity(Intent(this, LoginOrRegisterActivity::class.java))
    }
}