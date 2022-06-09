package com.firstapp.tempus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.content.Intent
import android.view.View
import android.widget.EditText

class LoginActivity : AppCompatActivity() {

    // Create the authentication variable - Gabriel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize the authentication variable - Gabriel
        auth = Firebase.auth
    }

    // Method that is called upon clicking the Login button, found in the relevant layout - Gabriel
    public fun loginUser(view: View) {
        // Save the inputted email and password as strings - Gabriel
        var email: String = findViewById<EditText>(R.id.login_email_edit_text).text.toString()
        var password: String = findViewById<EditText>(R.id.login_password_edit_text).text.toString()

        // Use the signInWithEmailAndPassword method and pass in the email and password variables - Gabriel
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                // Upon a successful sign in, display a Toast message and change the activity - Gabriel
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    // For the purpose of testing the Create and Edit Event Activities - Gabriel
                    //startActivity(Intent(this, CreateEventActivity::class.java))
                    //startActivity(Intent(this, EditEventActivity::class.java))
                // Otherwise, display a Toast message that the sign in failed - Gabriel
                } else {
                    Toast.makeText(this, "Unable to login. Check your input or try again later", Toast.LENGTH_SHORT).show()
                }
            }
    }
}