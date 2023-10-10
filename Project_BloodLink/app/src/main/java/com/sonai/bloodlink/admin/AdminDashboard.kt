package com.sonai.bloodlink.admin

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.sonai.bloodlink.R
import com.sonai.bloodlink.mainactivity.dashboard.DashboardPage
import com.sonai.bloodlink.admin.home.AdminHomeFragment


class AdminDashboard : AppCompatActivity() {

    lateinit var drawerlayout: DrawerLayout
    lateinit var coordinatelayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var framelayout: FrameLayout
    lateinit var navigationview: NavigationView


//    lateinit var loginregcontainer: LinearLayout

    lateinit var logoutbtn: Button
    lateinit var progressbar: ProgressBar

    var sp: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_dashboard_page)

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

        // Display the Admin Home Fragment initially
        if (savedInstanceState == null) {
            replaceFragment(AdminHomeFragment())
        }

        setUpToolbar()

        // add hamburger icon
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerlayout, R.string.open_drawer, R.string.close_drawer
        )

        //sync Hamburger icon to nav drawer
        drawerlayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // add click listener to nav items
        navigationview.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.admin_nav_home -> {
                    supportActionBar?.title = "Admin Block"
                    replaceFragment(AdminHomeFragment())
                    drawerlayout.closeDrawers()
                }

                R.id.admin_nav_profile -> {

//                    replaceFragment(ProfileFragment())
//                    supportActionBar?.title = "Admin Profile"
//                    drawerlayout.closeDrawers()
                }

                R.id.admin_nav_about -> {
                    Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show()
                }

            }

            return@setNavigationItemSelectedListener true
        }


        logoutbtn.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(this).edit().remove("username")
                .apply()
            Log.d(TAG,"line1")
//            Log.d("DashboardPage","Log Out Done")
            logoutbtn.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
            Log.d(TAG,"line2")
            Handler().postDelayed(
                {
                    Log.d(TAG,"line3")
                    progressbar.visibility = View.GONE
//                    loginregcontainer.visibility = View.VISIBLE
                    Log.d(TAG,"line4")
                    startActivity(Intent(this, DashboardPage::class.java))
                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                    finish()
                    Log.d(TAG,"line5")
                }, 2000
            )
        }
    }
    //Back button setup
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START)
        } else if (supportFragmentManager.findFragmentById(R.id.framelayout) is AdminHomeFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, fragment)
            .addToBackStack(null) // Add the fragment to the back stack
            .commit()
    }
    // add click listener on hamburger icon
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerlayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Combined toolbar with actionbar
    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Dashboard"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}