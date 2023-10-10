package com.sonai.bloodlink.donor.Login.forgetPass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R
import com.sonai.bloodlink.donor.Login.DonorLoginPage

class EnterNewPasswordPage : AppCompatActivity() {

    private lateinit var enterPass: TextInputEditText
    private lateinit var reEnterPass: TextInputEditText
    private lateinit var resetPass: Button
    private lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_new_password_page)

        enterPass = findViewById(R.id.enterPass)
        reEnterPass = findViewById(R.id.reEnterPass)
        resetPass = findViewById(R.id.resetPass)
        progressbar = findViewById(R.id.progressbar)

        val username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", null)

        resetPass.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            resetPass.visibility = View.GONE
            val new = enterPass.text.toString()
            val conNew = reEnterPass.text.toString()
            if (new != conNew){
                Handler().postDelayed(
                    {
                        reEnterPass.error = "Not matched"
                        progressbar.visibility = View.GONE
                        resetPass.visibility = View.VISIBLE
                    }, 200
                )
            } else{
                FirebaseFirestore.getInstance().collection("User_Information").document(username!!).update(
                    mapOf("password" to new)
                )
                Handler().postDelayed(
                    {
                        startActivity(Intent(this, DonorLoginPage::class.java))
                    }, 2500
                )
            }
        }
    }
}