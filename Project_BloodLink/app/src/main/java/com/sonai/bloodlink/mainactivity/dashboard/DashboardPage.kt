package com.sonai.bloodlink.mainactivity.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.sonai.bloodlink.R
import com.sonai.bloodlink.mainactivity.home.HomeFragment
import com.sonai.bloodlink.mainactivity.profile.ProfileFragment
import com.sonai.bloodlink.donor.Login.DonorLoginPage
import com.sonai.bloodlink.donor.registation.DonorRegistrationPage
import com.sonai.bloodlink.mainactivity.aboutus.AboutusFragment
import com.sonai.bloodlink.utilityClasses.LocationGets

class DashboardPage : AppCompatActivity() {

    lateinit var drawerlayout: DrawerLayout
    lateinit var coordinatelayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var framelayout: FrameLayout
    lateinit var navigationview: NavigationView

    lateinit var settxt: TextView
    lateinit var loginbtn: Button
    lateinit var regbtn: Button
    lateinit var loginregcontainer: LinearLayout

    lateinit var logoutbtn: Button
    lateinit var progressbar: ProgressBar

    var sp: String? = null


    @SuppressLint("MissingInflatedId", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_page)

        PreferenceManager.getDefaultSharedPreferences(this).edit().remove("count")
            .apply()
        PreferenceManager.getDefaultSharedPreferences(this).edit()
            .remove("verificationId").apply()

        val l = LocationGets(this)
        l.locationRequest()

//        val s = StoragePermission(this)
//        if (!s.isStoragePermissionGranted()){
//            s.requestStoragePermission()
//        }

        drawerlayout = findViewById(R.id.drawerlayout)
        coordinatelayout = findViewById(R.id.coordinatelayout)
        toolbar = findViewById(R.id.toolbar)
        framelayout = findViewById(R.id.framelayout)
        navigationview = findViewById(R.id.navigationview)
        logoutbtn = findViewById(R.id.logoutbtn)
        progressbar = findViewById(R.id.progressbar)

        val headerview = navigationview.getHeaderView(0)
        settxt = headerview.findViewById(R.id.settxt)
        loginbtn = headerview.findViewById(R.id.loginbtn)
        regbtn = headerview.findViewById(R.id.regbtn)
        loginregcontainer = headerview.findViewById(R.id.loginregcontainer)


        try {
            sp = PreferenceManager.getDefaultSharedPreferences(this).getString("username", null)
                .toString()

        } catch (_: Exception) {
        }

        if (sp != null) {
            settxt.visibility = View.VISIBLE
            loginregcontainer.visibility = View.GONE
            settxt.text = sp
            logoutbtn.visibility = View.VISIBLE
        } else {
            loginregcontainer.visibility = View.VISIBLE
            settxt.visibility = View.GONE
            logoutbtn.visibility = View.GONE
        }

        loginbtn.setOnClickListener {
            startActivity(Intent(this@DashboardPage, DonorLoginPage::class.java))
            finish()
        }

        regbtn.setOnClickListener {
            startActivity(Intent(this@DashboardPage, DonorRegistrationPage::class.java))
            finish()
        }

        logoutbtn.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(this).edit().remove("username")
                .apply()
            PreferenceManager.getDefaultSharedPreferences(this).edit().remove("phone")
                .apply()
//            Log.d("DashboardPage","Log Out Done")
            logoutbtn.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
            Handler().postDelayed(
                {
                    progressbar.visibility = View.GONE
                    loginregcontainer.visibility = View.VISIBLE
                    settxt.visibility = View.GONE
                    Toast.makeText(this@DashboardPage, "Logged Out", Toast.LENGTH_SHORT).show()
                }, 2000
            )
        }

        // Display the HomeFragment initially
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        setUpToolbar()

        // add hamburger icon
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@DashboardPage, drawerlayout, R.string.open_drawer, R.string.close_drawer
        )

        //sync Hamburger icon to nav drawer
        drawerlayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        // add click listener to nav items
        navigationview.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    supportActionBar?.title = "Dashboard"
                    replaceFragment(HomeFragment())
                    drawerlayout.closeDrawers()
                }

                R.id.nav_profile -> {

                    replaceFragment(ProfileFragment())
                    supportActionBar?.title = "Profile"
                    drawerlayout.closeDrawers()
                }

                R.id.nav_aboutus -> {
                    replaceFragment(AboutusFragment())
                    supportActionBar?.title = "About us"
                    drawerlayout.closeDrawers()
                }

                R.id.nav_helpandsupport -> {
                    Toast.makeText(this@DashboardPage, "Help & Support", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
         if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START)
        }else if (supportFragmentManager.findFragmentById(R.id.framelayout) is HomeFragment) {
            finish()
        }
         else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        try {
            sp = PreferenceManager.getDefaultSharedPreferences(this).getString("username", null)
                .toString()

        } catch (_: Exception) {}

        if (sp != "null") {
            settxt.visibility = View.VISIBLE
            loginregcontainer.visibility = View.GONE
            settxt.text = sp
            logoutbtn.visibility = View.VISIBLE
        } else {
            loginregcontainer.visibility = View.VISIBLE
            settxt.visibility = View.GONE
            logoutbtn.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            sp = PreferenceManager.getDefaultSharedPreferences(this).getString("username", null)
                .toString()

        } catch (_: Exception) {}

        if (sp != "null") {
            settxt.visibility = View.VISIBLE
            loginregcontainer.visibility = View.GONE
            settxt.text = sp
            logoutbtn.visibility = View.VISIBLE
        } else {
            loginregcontainer.visibility = View.VISIBLE
            settxt.visibility = View.GONE
            logoutbtn.visibility = View.GONE
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