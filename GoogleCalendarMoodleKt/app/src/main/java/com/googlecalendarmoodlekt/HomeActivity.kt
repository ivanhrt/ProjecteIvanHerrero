package com.googlecalendarmoodlekt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType {
    BASIC,
    GOOGLE
}


class HomeActivity : AppCompatActivity() {

    //Private

    private val URL_BASE = "https://calendar.google.com/calendar/u/0/gp?hl=es#~calendar:view=m"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Setup
        val bundle :Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")
        //setup(email ?:"", provider ?: "")

        //Guardado de datos

        val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()

        //Refresh

        swipeRefresh.setOnRefreshListener {
            web_calendar.reload()
        }

        //WebView

        web_calendar.webChromeClient = object : WebChromeClient() {

        }

        web_calendar.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                swipeRefresh.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                swipeRefresh.isRefreshing = false
            }
        }

        val settings = web_calendar.settings
        settings.javaScriptEnabled = true

        web_calendar.loadUrl(URL_BASE)


        /*web_calendar.loadUrl(URL_BASE + "javascript:{" +
                "ins=document.getElementsByTagName('input');" +
                "ins[0].value='email';" +
                "ins[1].value='provider';" +
                "ins[2].value=true;" +
                "document.getElementsByTagName('form')[0].submit();" +
                "};" );
        */


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        title = getString(R.string.calendar)
        when(item.itemId){
            //Borrado de datos
            R.id.action_logout ->{
                val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                prefs.clear()
                prefs.apply()
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,AuthActivity::class.java))
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onBackPressed() {
        if (web_calendar.canGoBack()){
            web_calendar.goBack()
        }
        else{
            finishAffinity()
            finish()
        }

    }

    /*private fun setup(email: String, provider: String) {

        title = "Inicio"
        emailTextView.text = email
        providerTextView.text = provider

        logOutButton.setOnClickListener {

            //Borrado de datos
            val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()


            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

    }*/
}