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

class EditEventActivity : AppCompatActivity() {

    // Create the authentication and database variables - Gabriel
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Variables containing the relevant fields found in the chosen document before editing.
    // Set as static values for testing. Otherwise, plug in proper values based on Main Activity recycler view - Gabriel
//    var oldEventTitle: String = "Example"
//    var oldEventLocation: String = "Example"
//    var oldEventDate: String = "06/22/2022"
//    var oldEventTime: String = "06:00 PM"
//    var oldNumID: String = "731011209"
//    var oldDocID: String = "IexZdClBs1tDBZKcJWWN"

    // Initialize the calendar variable, used to store the date and time variables as integers, rather than strings - Gabriel
    val calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        // Initialize the authentication and database variables - Gabriel
        auth = Firebase.auth
        db = Firebase.firestore

        // Get the stored event object passed through the intent provided by the View Event Activity - Gabriel
        val bundle : Bundle?= intent.extras
        val oldEvent:Event = bundle!!.get("event") as Event

        // Assign the event object's variables to specific variables - Gabriel
        var oldEventTitle = oldEvent.mTitle
        var oldEventLocation = oldEvent.mLocation
        var oldEventDate = oldEvent.mDate
        var oldEventTime = oldEvent.mTime
        var oldNumID = oldEvent.mNumID
        var oldDocID = oldEvent.mDocID

        // Create a temporary string that will contain the particular 12 hour period the original time is within - Gabriel
        var tempString = oldEventTime.substring(6)
        // If the temporary string is the PM time period, then initialize the calendar, adding 12 additional hours to the hourOfDay value - Gabriel
        if(tempString == "PM"){
            // Set the calendar values, which are set in the following order: year, month, day (date), hourOfDay, minute - Gabriel
            calendar.set(oldEventDate.substring(6).toInt(),
                oldEventDate.substring(0, 2).toInt()-1,
                oldEventDate.substring(3, 5).toInt(),
                oldEventTime.substring(0, 2).toInt() + 12,
                oldEventTime.substring(3, 5).toInt())
        // Otherwise, if it is AM, set the calendar with no offsets - Gabriel
        } else {
            // Set the calendar values, which are set in the following order: year, month, day (date), hourOfDay, minute - Gabriel
            calendar.set(oldEventDate.substring(6).toInt(),
                oldEventDate.substring(0, 2).toInt()-1,
                oldEventDate.substring(3, 5).toInt(),
                oldEventTime.substring(0, 2).toInt(),
                oldEventTime.substring(3, 5).toInt())
        }

        // Set the text of the Title and Location Edit Text boxes to their relevant values, found in the old info variables - Gabriel
        findViewById<EditText>(R.id.title_text).setText(oldEventTitle)
        findViewById<EditText>(R.id.location_text).setText(oldEventLocation)

        // set pointers to date and time buttons/textviews
        val editButton: Button = findViewById(R.id.edit)
        val dateButton: Button = findViewById(R.id.date)
        val timeButton: Button = findViewById(R.id.start_time)
        val dateText: TextView = findViewById(R.id.date_text)
        val timeText: TextView = findViewById(R.id.time_text)
        val deleteButton: Button = findViewById(R.id.delete)

        // Rather than initialize the date in the date picker fragment to the current date, set it to the original date, found in the document to be "edited" - Gabriel
        var date = calendar.timeInMillis - 86400000
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
            var timePicker =
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
                val simpleDateFormat = SimpleDateFormat("hh:mm a")
                hour = timePicker.hour
                minute = timePicker.minute
                val calendar = Calendar.getInstance()
                calendar.set(0, 0, 0, hour, minute)
                val format = simpleDateFormat.format(calendar.timeInMillis)
                timeText.text = format
            }
        }

        editButton.setOnClickListener{
            // Variables that represent the event title and location edit text fields.
            // Note: These variables must be declared here to ensure they grab the proper string values - Gabriel
            var eventTitle: String = findViewById<EditText>(R.id.title_text).text.toString()
            var eventLocation: String = findViewById<EditText>(R.id.location_text).text.toString()

            // Delete the old document that is being "edited" using the old info variables for pathing - Gabriel
            db.collection("Users").document(auth.uid.toString()).collection(oldEventDate.substring(6))
                  .document(oldEventDate.substring(0, 2)).collection("Events").document(oldDocID).delete()

            // Rather than create the path upon document creation, throw the proper path into a reference variable for the sake of clarity - Gabriel
            var databasePath = db.collection("Users").document(auth.uid.toString()).collection(dateText.text.toString().substring(6))
                .document(dateText.text.toString().substring(0, 2)).collection("Events").document(oldDocID)

            // Create the event object, which will take the place of the hash map - Gabriel
            val eventObj = Event(eventTitle, timeText.text.toString(), eventLocation, dateText.text.toString(), oldNumID, auth.uid.toString(), oldDocID)

            // Set the data in the relevant database path using the event object - Gabriel
            databasePath.set(eventObj)
                .addOnCompleteListener{ task ->
                    // Upon a successful document path creation, display a Toast message and change the activity - Gabriel
                    if(task.isSuccessful){
                        Toast.makeText(this, "Database path edit successful!", Toast.LENGTH_SHORT).show()
                        //startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    // Otherwise, display a Toast message that the creation failed - Gabriel
                    } else {
                        Toast.makeText(this, "Unable to edit database path. Check your inputs or try again later.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        deleteButton.setOnClickListener{
            // Delete the document using the old data fields.
            // Note: there is an unusual bug right now where the program will crash upon attempting to delete
            //       a document that was made during the same run time. OldDocID ends up empty, for some reason - Gabriel
            db.collection("Users").document(auth.uid.toString()).collection(oldEventDate.substring(6))
                .document(oldEventDate.substring(0, 2)).collection("Events").document(oldDocID).delete()
                .addOnCompleteListener{ task ->
                    // Upon a successful document path deletion, display a Toast message and change the activity - Gabriel
                    if(task.isSuccessful){
                        Toast.makeText(this, "Database path deletion successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    // Otherwise, display a Toast message that the deletion failed - Gabriel
                    } else {
                        Toast.makeText(this, "Unable to delete database path. Please try again later.", Toast.LENGTH_SHORT).show()
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
        // Instead of using the current date, use the date found in the calendar variable's fields.
        // Note: the calendar value being inputted is then formatted as mm/dd/yyyy.
        // Then, the displayed text for the date picker fragment is set to this value upon creation - Gabriel
        var format = simpleDateFormat.format(calendar.timeInMillis - 86400000) // Offset by one day, in millis, since it is displaying the day off by one - Gabriel
        dateText.text = format

        // set text to show current time
        simpleDateFormat = SimpleDateFormat("hh:mm a")
        // Instead of using the current time, use the time found in the calendar variable's fields.
        // Note: the calendar value being inputted is then formatted as HH:MM (H = hour, M = minute).
        // Then, the displayed text for the time picker fragment is set to this value upon creation - Gabriel
        format = simpleDateFormat.format(calendar.timeInMillis)
        timeText.text = format
    }
}

// Old editButton.setOnClickListener code:
// Upon clicking the button, create a hashmap with the inputted data - Gabriel
//            val data = hashMapOf<Any?, Any?>(
//                "Event Title" to eventTitle,
//                "Event Location" to eventLocation,
//                "Event Date" to dateText.text.toString(),
//                "Event Time" to timeText.text.toString()
//            )

//      (Delete the old document)

// Create a new document based on the new user inputs, thus "editing" the old document, using the same pathing logic during the creation process.
//            // Note: While documents can actually be updated, if the user changes the date and time, then the document location has to change too.
//            //       As a result, rather than updating the document in question, it is better to delete the old document and create a new one based on the info - Gabriel
//            db.collection("Users").document(auth.uid.toString()).collection(dateText.text.toString().substring(6))
//                .document(dateText.text.toString().substring(0, 2)).collection("Events").document(eventTitle).set(data)
//                .addOnCompleteListener{ task ->
//                    // Upon a successful document path creation, display a Toast message and change the activity - Gabriel
//                    if(task.isSuccessful){
//                        Toast.makeText(this, "Database path edit successful!", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this, MainActivity::class.java))
//                    // Otherwise, display a Toast message that the creation failed - Gabriel
//                    } else {
//                        Toast.makeText(this, "Unable to edit database path. Check your inputs or try again later.", Toast.LENGTH_SHORT).show()
//                    }
//                }