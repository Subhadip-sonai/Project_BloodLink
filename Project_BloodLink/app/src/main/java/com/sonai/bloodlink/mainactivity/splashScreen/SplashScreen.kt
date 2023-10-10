package com.sonai.bloodlink.mainactivity.splashScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sonai.bloodlink.R
import android.os.Handler
import com.sonai.bloodlink.mainactivity.dashboard.DashboardPage

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private var timer = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_layout)

        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, DashboardPage::class.java)
            startActivity(intent)
            finish()
        }, timer.toLong())
    }
}
