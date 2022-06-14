package com.firstapp.tempus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class CreateEventActivity : AppCompatActivity() {

    // Create the authentication and database variables - Gabriel
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Initialize the authentication and database variables - Gabriel
        auth = Firebase.auth
        db = Firebase.firestore


        // set pointers to date and time buttons/textviews
        val createButton: Button = findViewById(R.id.create)
        val dateButton: Button = findViewById(R.id.date)
        val timeButton: Button = findViewById(R.id.start_time)
        val dateText: TextView = findViewById(R.id.date_text)
        val timeText: TextView = findViewById(R.id.time_text)

        var date = MaterialDatePicker.todayInUtcMilliseconds()
        // Initialize the dateDummy variable with the value of date, to smooth things over when being used by others - Gabriel
        var dateDummy = date
        var hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        var minute = Calendar.getInstance().get(Calendar.MINUTE)
        // set date and time views to current time
        setText()

        dateButton.setOnClickListener{

            // create and show datepicker
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Date")
                    .setSelection(date)
                    .build()
            datePicker.show(supportFragmentManager, "tag");

            // when user clicks "OK"
            datePicker.addOnPositiveButtonClickListener {
                // set text to selected date
                val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy")
                date = datePicker.selection!!
                dateDummy = date + 86400000
                val format = simpleDateFormat.format(dateDummy)
                dateText.text = String.format(format)
            }
        }

        timeButton.setOnClickListener{
            // create and show timepicker
            val timePicker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(hour)
                    .setMinute(minute)
                    .setTitleText("Start Time")
                    .build()
            timePicker.show(supportFragmentManager, "tag")

            // when user clicks "OK"
            timePicker.addOnPositiveButtonClickListener {
                // set text to selected date
                val simpleDateFormat = SimpleDateFormat("h:mm a")
                hour = timePicker.hour
                minute = timePicker.minute
                val calendar = Calendar.getInstance()
                calendar.set(0, 0, 0, hour, minute)
                val format = simpleDateFormat.format(calendar.timeInMillis)
                timeText.text = format
            }
        }

        createButton.setOnClickListener{
            // Variables that represent the event title and location edit text fields.
            // Note: These variables must be declared here to ensure they grab the proper string values - Gabriel
            var eventTitle: String = findViewById<EditText>(R.id.title_text).text.toString()
            var eventLocation: String = findViewById<EditText>(R.id.location_text).text.toString()

            // Rather than create the path upon document creation, throw the proper path into a reference variable for the sake of accessing its unique document ID - Gabriel
            var databasePath = db.collection("Users").document(auth.uid.toString()).collection(dateText.text.toString().substring(6))
                .document(dateText.text.toString().substring(0, 2)).collection("Events").document()

            // Initialize the while loop count and string ID variables - Gabriel
            var x = 0
            var databasePathID: String = ""

            // Loop through the databasePath document ID - Gabriel
            while(x < databasePath.id.length){
                // If the current character at X is a letter of some kind, concatenate its ASCII value to the databasePathID variable - Gabriel
                if(databasePath.id.toString()[x] in 'A'..'Z' || databasePath.id.toString()[x] in 'a'..'z'){
                    databasePathID = (databasePathID + databasePath.id.toString()[x].code)
                }
                // Otherwise, if the character at X is a number, just concatenate it to the databasePathID variable - Gabriel
                else{
                    databasePathID = (databasePathID + databasePath.id.toString()[x])
                }
                // Increment the count variable after each operation - Gabriel
                x += 1
            }
            // After converting the document ID to a numeric ID, set the databasePathID variable to the first 9 digits of the same value.
            // Note: this is for the purpose of fitting this numeric ID within a variable of type integer - Gabriel
            databasePathID = databasePathID.substring(0,9)

            // Create the event object, which will take the place of the hash map - Gabriel
            val eventObj = Event(eventTitle, timeText.text.toString(), eventLocation, dateText.text.toString(), databasePathID, auth.uid.toString())

            // Set the data in the relevant database path using the event object - Gabriel
            databasePath.set(eventObj)
                .addOnCompleteListener{ task ->
                    // Upon a successful document path creation, display a Toast message and change the activity - Gabriel
                    if(task.isSuccessful){
                        Toast.makeText(this, "Database path creation successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    // Otherwise, display a Toast message that the creation failed - Gabriel
                    } else {
                        Toast.makeText(this, "Unable to create database path. Check your inputs or try again later.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun setText() {
        // set pointers to date and time textviews
        val dateText: TextView = findViewById(R.id.date_text)
        val timeText: TextView = findViewById(R.id.time_text)

        // set text to show current date
        var simpleDateFormat = SimpleDateFormat("MM/dd/yyyy")
        var format = simpleDateFormat.format(Date())
        dateText.text = format

        // set text to show current time
        simpleDateFormat = SimpleDateFormat("HH:mm a")
        format = simpleDateFormat.format(Date())
        timeText.text = format
    }
}

// Old createButton.setOnClickListener code:
// Upon clicking the button, create a hashmap with the inputted data - Gabriel
//            val data = hashMapOf<Any?, Any?>(
//                "Event Title" to eventTitle,
//                "Event Location" to eventLocation,
//                "Event Date" to dateText.text.toString(),
//                "Event Time" to timeText.text.toString()
//            )

// Coll -> Doc -> Coll ->Doc -> Coll -> Doc
// Then, create a document which contains the data of the hash map in the following path: Users->UserID->Year->Month->Events->Event Title - Gabriel
/* db.collection("Users").document(auth.uid.toString()).collection(dateText.text.toString().substring(6))
                                                                                              // Coll -> Doc -> Coll ->Doc -> Coll -> Doc
 // Then, create a document which contains the data of the hash map in the following path logic: Users->UserID->Year->Month->Events->Event Title - Gabriel
 db.collection("Users").document(auth.uid.toString()).collection(dateText.text.toString().substring(6))
     .document(dateText.text.toString().substring(0, 2)).collection("Events").document(eventTitle).set(data)
     .addOnCompleteListener{ task ->
         // Upon a successful document path creation, display a Toast message and change the activity - Gabriel
         if(task.isSuccessful){
             Toast.makeText(this, "Database path creation successful!", Toast.LENGTH_SHORT).show()


             startActivity(Intent(this, MainActivity::class.java))
         // Otherwise, display a Toast message that the creation failed - Gabriel
         } else {
             Toast.makeText(this, "Unable to create database path. Check your inputs or try again later.", Toast.LENGTH_SHORT).show()
         }
     }*/