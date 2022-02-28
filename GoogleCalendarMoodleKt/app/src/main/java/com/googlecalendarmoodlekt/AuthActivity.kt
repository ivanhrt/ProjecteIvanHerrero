package com.googlecalendarmoodlekt

import res.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics

import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        //Splash
        Thread.sleep(2000)
        //setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


        //Analytics Event
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Proyecto con Firebase")
        analytics.logEvent("InitScreen", bundle)

        //Setup
        setup()


    }

    private fun setup() {
        title = "Autenticación"

        /*signUpButton.setOnClickListener{
            if (emailEditText.text.isNotEmpty())
        }*/
    }


}