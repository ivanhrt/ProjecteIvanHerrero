package com.googlecalendarmoodlekt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_home.swipeRefresh
import kotlinx.android.synthetic.main.activity_moodle.*


class MoodleActivity : AppCompatActivity() {


    private val URL_BASE = "https://www.vidalibarraquer.net/moodle/calendar/view.php?view=month"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moodle)
        title = "Moodle ViB"

        //Mantener sesion activa
        val bundle :Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")
        //setup(email ?:"", provider ?: "")

        //Guardado de datos
        val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)


        swipeRefresh.setOnRefreshListener {
            web_moodle.reload()
        }

        //WebView

        web_moodle.webChromeClient = object : WebChromeClient() {

        }

        web_moodle.webViewClient = object : WebViewClient() {

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



        //Cargar web e iniciar sesión
        val settings = web_moodle.settings
        settings.javaScriptEnabled = true
        web_moodle.loadUrl(URL_BASE)


    }



    override fun onBackPressed() {
        if (web_moodle.canGoBack()){
            web_moodle.goBack()
        }
        else{
            val homeIntent :Intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("email", getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).getString("email",""))
                putExtra("provider", getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).getString("provider",""))

            }
            startActivity(homeIntent)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val homeIntent :Intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("email", getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).getString("email",""))
                    putExtra("provider", getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).getString("provider",""))

                }
                startActivity(homeIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override  fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        return true
    }

}