package com.sonai.bloodlink.donor.Login

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
import com.sonai.bloodlink.agency.AgencyLoginPage
import com.sonai.bloodlink.mainactivity.dashboard.DashboardPage
import com.sonai.bloodlink.R
import com.sonai.bloodlink.admin.AdminLoginPage
import com.sonai.bloodlink.donor.Login.forgetPass.EnterNumberForVarificationToForgotPassword
import com.sonai.bloodlink.donor.registation.DonorRegistrationPage

class DonorLoginPage : AppCompatActivity() {

    lateinit var donorloginusername: TextInputLayout
    lateinit var donorloginpassword: TextInputLayout
    lateinit var donorBtnLogin: Button
    lateinit var agencyregisterbtn: TextView
    lateinit var donorloginProgressbar: ProgressBar
    lateinit var gotoregisterpage: TextView
    lateinit var forgetpassdonaor: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donor_login_page)

        donorloginusername = findViewById(R.id.adminemail)
        donorloginpassword = findViewById(R.id.adminpassword)
        donorBtnLogin = findViewById(R.id.donorBtnLogin)
        gotoregisterpage = findViewById(R.id.gotoregisterpage)
        donorloginProgressbar = findViewById(R.id.donorloginProgressbar)
        agencyregisterbtn = findViewById(R.id.agencyregisterbtn)
        forgetpassdonaor = findViewById(R.id.forgetpassdonaor)

        donorBtnLogin.setOnClickListener {

            donorloginProgressbar.visibility = View.VISIBLE
            donorBtnLogin.visibility = View.GONE

            val usernamedatabase = donorloginusername.editText?.text.toString()
            val passworddatabase = donorloginpassword.editText?.text.toString()

            if (check(usernamedatabase, passworddatabase)) {

                val dbref = FirebaseFirestore.getInstance().collection("User_Information")
                    .document(usernamedatabase)
                dbref.get().addOnSuccessListener { task ->
                    if (task.exists()) {
                        if (task.data?.get("password") == passworddatabase) {

                            donorloginpassword.error = null

                            donorloginProgressbar.visibility = View.GONE
                            donorBtnLogin.visibility = View.VISIBLE

                            Toast.makeText(
                                this@DonorLoginPage, "Login Successfully", Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@DonorLoginPage, DashboardPage::class.java)

                            val saveData =
                                PreferenceManager.getDefaultSharedPreferences(this@DonorLoginPage)
                            saveData.edit().putString("username", usernamedatabase).apply()
                            saveData.edit().putString("phone", task.data?.get("phone").toString()).apply()

                            startActivity(intent)
                            finish()

                        } else {
                            donorloginpassword.error = "Incorrect Password"
                            donorloginProgressbar.visibility = View.GONE
                            donorBtnLogin.visibility = View.VISIBLE
                        }

                    } else {
                        donorloginusername.error = "Not Exist"
                        donorloginProgressbar.visibility = View.GONE
                        donorBtnLogin.visibility = View.VISIBLE
                    }

                }

            }


        }

        forgetpassdonaor.setOnClickListener {
            startActivity(Intent(this, EnterNumberForVarificationToForgotPassword::class.java))
        }

        gotoregisterpage.setOnClickListener {

            val intent = Intent(this@DonorLoginPage, DonorRegistrationPage::class.java)
            startActivity(intent)
        }

        agencyregisterbtn.setOnClickListener {

            val intent = Intent(this@DonorLoginPage, AgencyLoginPage::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.adminlogin).setOnClickListener {
            startActivity(Intent(this,AdminLoginPage::class.java))
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@DonorLoginPage,DashboardPage::class.java))
        finish()
    }

    private fun check(getname: String, getpassword: String): Boolean {

        if (getname.isEmpty()) {
            donorloginusername.error = "Can't be Empty"
            donorloginProgressbar.visibility = View.GONE
            donorBtnLogin.visibility = View.VISIBLE
            return false
        }
        if (getpassword.isEmpty()) {
            donorloginpassword.error = "Can't be Empty"
            donorloginProgressbar.visibility = View.GONE
            donorBtnLogin.visibility = View.VISIBLE
            return false
        }
        return true
    }


}


