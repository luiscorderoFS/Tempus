package com.firstapp.tempus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginOrRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_or_register)
    }

    // Method that is called upon clicking the Login Activity button, found in the relevant layout - Gabriel
    public fun gotoLogin(view: View) {
        // Simply change the activity - Gabriel
        startActivity(Intent(this, LoginActivity::class.java))
    }

    // Method that is called upon clicking the Register Activity button, found in the relevant layout - Gabriel
    public fun gotoReg(view: View) {
        // Simply change the activity - Gabriel
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}