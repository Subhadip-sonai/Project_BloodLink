package com.sonai.bloodlink.donor.Login.forgetPass

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.google.android.material.textfield.TextInputEditText
import com.sonai.bloodlink.R
import com.sonai.bloodlink.utilityClasses.OTPVerification

class EnterNumberForVarificationToForgotPassword : AppCompatActivity() {

    private lateinit var enterNumber: TextInputEditText
    private lateinit var generateOTP: Button
    private lateinit var progressbar: ProgressBar
    val get = OTPVerification(this)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_number_to_forgot_password)

        enterNumber = findViewById(R.id.enterNumber)
        generateOTP = findViewById(R.id.generateOTP)
        progressbar = findViewById(R.id.progressbar)

        generateOTP.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            generateOTP.visibility = View.GONE
            if (enterNumber.text.toString().length != 10) {
                 enterNumber.error = "Should be 10 digit"
                progressbar.visibility = View.GONE
                generateOTP.visibility = View.VISIBLE
            } else {
                val num = "+91${enterNumber.text.toString()}"
                get.sendOTP(num)
                val count = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString("count", null).toString()
                if (count == "1") {
                    enterNumber.error = "Invalid number"
                    progressbar.visibility = View.GONE
                    generateOTP.visibility = View.VISIBLE
                    PreferenceManager.getDefaultSharedPreferences(this).edit().remove("count").apply()
                } else{
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString("phone", enterNumber.text.toString()).apply()
                    Handler().postDelayed(
                        {
                            startActivity(Intent(this, VerifyOTP::class.java))
                        },2500
                    )
                }
            }
        }
    }
}