package com.sonai.bloodlink.agency.dashboard

import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.sonai.bloodlink.R

class AgencyDashboard : AppCompatActivity() {

    lateinit var drawerlayout: DrawerLayout
    lateinit var coordinatelayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var framelayout: FrameLayout
    lateinit var navigationview: NavigationView
    lateinit var logoutbtn: Button
    lateinit var progressbar: ProgressBar

    var sp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agency_dashboard_page)

        drawerlayout = findViewById(R.id.drawerlayout)
        coordinatelayout = findViewById(R.id.coordinatelayout)
        toolbar = findViewById(R.id.toolbar)
        framelayout = findViewById(R.id.framelayout)
        navigationview = findViewById(R.id.navigationview)
        logoutbtn = findViewById(R.id.logoutbtn)
        progressbar = findViewById(R.id.progressbar)

        try {
            sp = PreferenceManager.getDefaultSharedPreferences(this).getString("username", null)
                .toString()

        } catch (_: Exception) {
        }


    }
}