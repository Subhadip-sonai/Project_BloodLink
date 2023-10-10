package com.sonai.bloodlink.donor.Login.forgetPass

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R
import com.sonai.bloodlink.donor.registation.DonorRegistrationPage
import com.sonai.bloodlink.utilityClasses.GoesToNextEditTextOTP
import com.sonai.bloodlink.utilityClasses.OTPVerification

class VerifyOTP : AppCompatActivity() {
    private lateinit var input1: EditText
    private lateinit var input2: EditText
    private lateinit var input3: EditText
    private lateinit var input4: EditText
    private lateinit var input5: EditText
    private lateinit var input6: EditText

    private lateinit var resendOTP: TextView
    private lateinit var countdownTextView: TextView
    private lateinit var validateotpbtn: Button
    private lateinit var progressbar: ProgressBar

    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftMillis: Long = 120000 // 120 seconds

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification_page)

        input1 = findViewById(R.id.input1)
        input2 = findViewById(R.id.input2)
        input3 = findViewById(R.id.input3)
        input4 = findViewById(R.id.input4)
        input5 = findViewById(R.id.input5)
        input6 = findViewById(R.id.input6)
        validateotpbtn = findViewById(R.id.validateotpbtn)
        progressbar = findViewById(R.id.progressbar)
        resendOTP = findViewById(R.id.resendOTP)
        countdownTextView = findViewById(R.id.countdownTimer)

        GoesToNextEditTextOTP().setEditTextListeners(input1,input2,input3,input4,input5,input6)


        // Create a CountDownTimer
        countDownTimer = object : CountDownTimer(timeLeftMillis, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMillis = millisUntilFinished
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
                countdownTextView.text = "Resend OTP after $timeLeftFormatted" // Update TextView
            }

            override fun onFinish() {
                // Timer finished, show your TextView
                countdownTextView.visibility = View.GONE
                resendOTP.visibility = View.VISIBLE
            }
        }

        //Starting timer
        countDownTimer.start()

        validateotpbtn.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            validateotpbtn.visibility = View.GONE
            if (input1.text.isEmpty() || input2.text.isEmpty() || input3.text.isEmpty() || input4.text.isEmpty() || input5.text.isEmpty() || input6.text.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                progressbar.visibility = View.GONE
                validateotpbtn.visibility = View.VISIBLE
            } else {
                val verificationId = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString("verificationId", null).toString()
                val y =
                    "${input1.text}${input2.text}${input3.text}${input4.text}${input5.text}${input6.text}"
                val credential = PhoneAuthProvider.getCredential(verificationId, y)

                OTPVerification(this).signInWithPhoneCredential(credential)

                val count = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString("count", null).toString()
                if (count == "2") {
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    input1.text.clear()
                    input2.text.clear()
                    input3.text.clear()
                    input4.text.clear()
                    input5.text.clear()
                    input6.text.clear()
                    progressbar.visibility = View.GONE
                    validateotpbtn.visibility = View.VISIBLE
                    PreferenceManager.getDefaultSharedPreferences(this).edit().remove("count")
                        .apply()
                } else {

                    val num = PreferenceManager.getDefaultSharedPreferences(this).getString("phone", "null")
                    FirebaseFirestore.getInstance().collection("User_Information_PhoneNo").document(num!!).get().addOnSuccessListener { task ->
                        if (task.exists()){
                            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("username", task.data?.get("username").toString()).apply()
                            Handler().postDelayed(
                                {
                                    startActivity(Intent(this, EnterNewPasswordPage::class.java))
                                    finish()
                                },2000
                            )
                        } else {
                            Handler().postDelayed(
                                {
                                    Toast.makeText(this, "Mobile no not registered", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, DonorRegistrationPage::class.java))
                                    finish()
                                }, 2500
                            )
                        }
                    }
                }
            }
        }

        resendOTP.setOnClickListener {
            val num = PreferenceManager.getDefaultSharedPreferences(this).getString("phone",null)
            OTPVerification(this).sendOTP("+91$num")
            countDownTimer.start()
            resendOTP.visibility = View.GONE
            countdownTextView.visibility = View.VISIBLE
        }
    }
}