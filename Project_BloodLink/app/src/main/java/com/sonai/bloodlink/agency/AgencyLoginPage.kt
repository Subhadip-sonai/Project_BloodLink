package com.sonai.bloodlink.agency

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R
import com.sonai.bloodlink.agency.dashboard.AgencyDashboard

class AgencyLoginPage : AppCompatActivity() {

    private lateinit var agencyloginname: TextInputLayout
    private lateinit var agencyloginpassword: TextInputLayout
    private lateinit var gotoregisterpage: TextView
    private lateinit var agencyLoginbtn: Button
    private lateinit var progressbar: ProgressBar


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agency_login_page)

        gotoregisterpage = findViewById(R.id.gotoregisterpage)
        agencyloginname = findViewById(R.id.agencyloginname)
        agencyloginpassword = findViewById(R.id.agencyloginpassword)
        agencyLoginbtn = findViewById(R.id.agencyLoginbtn)
        progressbar = findViewById(R.id.progressbar)

        agencyLoginbtn.setOnClickListener {
            agencyloginname.error = null
            agencyloginpassword.error = null

            progressbar.visibility = View.VISIBLE
            agencyLoginbtn.visibility = View.GONE

            if (check()) {
                val name = agencyloginname.editText?.text.toString()
                val pass = agencyloginpassword.editText?.text.toString()

                val ref = FirebaseFirestore.getInstance().collection("Agency_List").document(name)
                ref.get().addOnSuccessListener { task ->
                    if (task.exists()) {
                        if (task.data?.get("password") != pass) {
                            agencyloginpassword.error = "Incorrect Password"
                            progressbar.visibility = View.GONE
                            agencyLoginbtn.visibility = View.VISIBLE
                        } else {
                            if (task.data?.get("password") == pass && task.data?.get("verify") == "verified") {
                                PreferenceManager.getDefaultSharedPreferences(this).edit()
                                    .putString("username", name).apply()
                                startActivity(Intent(this, AgencyDashboard::class.java))
                                Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "You are not varified", Toast.LENGTH_SHORT)
                                    .show()
                                progressbar.visibility = View.GONE
                                agencyLoginbtn.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        agencyloginname.error = "Incorrect Name"
                        progressbar.visibility = View.GONE
                        agencyLoginbtn.visibility = View.VISIBLE
                    }

                }
            }
        }
        progressbar.visibility = View.GONE
        agencyLoginbtn.visibility = View.VISIBLE

        gotoregisterpage.setOnClickListener {
            val intent = Intent(this@AgencyLoginPage, AgencyRegistrationPage::class.java)
            startActivity(intent)
        }

    }

    private fun check(): Boolean {
        val name = agencyloginname.editText?.text.toString()
        val pass = agencyloginpassword.editText?.text.toString()

        if (name.isEmpty()) {
            agencyloginname.error = "Can't be empty"
            return false
        }
        if (pass.isEmpty()) {
            agencyloginpassword.error = "Can't be empty"
            return false
        }

        return true
    }
}