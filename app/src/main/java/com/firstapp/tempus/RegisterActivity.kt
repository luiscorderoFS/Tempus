package com.firstapp.tempus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    // Create the authentication variable - Gabriel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize the authentication variable - Gabriel
        auth = Firebase.auth
    }

    // Method that is called upon clicking the Register button, found in the relevant layout - Gabriel
    public fun registerUser(view: View) {
        // Save the inputted email and passwords as strings - Gabriel
        var email: String = findViewById<EditText>(R.id.email_edit_text).text.toString()
        var password: String = findViewById<EditText>(R.id.password_edit_text).text.toString()
        var rePassword: String = findViewById<EditText>(R.id.re_password_edit_text).text.toString()

        // If the password and retyped password match, then continue - Gabriel
        if(password == rePassword){
            // Use the createUserWithEmailAndPassword method and pass in the email and password variables.
            auth.createUserWithEmailAndPassword(email, password)
                // Upon a successful registration, display a Toast message and change the activity - Gabriel
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginOrRegisterActivity::class.java))
                        // Otherwise, display a Toast message that the registration failed - Gabriel
                    } else {
                        Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
        // Otherwise, display a Toast message that the passwords do not match - Gabriel
        } else {
            Toast.makeText(this, "Typed passwords do not match", Toast.LENGTH_SHORT).show()
        }
    }
}