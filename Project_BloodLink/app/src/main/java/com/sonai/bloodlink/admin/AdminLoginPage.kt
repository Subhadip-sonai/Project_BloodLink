package com.sonai.bloodlink.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.sonai.bloodlink.R

class AdminLoginPage : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_login_page)

        val adminemail = findViewById<TextInputLayout>(R.id.adminemail)
        val adminpassword = findViewById<TextInputLayout>(R.id.adminpassword)
        val loginbtn = findViewById<Button>(R.id.loginbtn)

        loginbtn.setOnClickListener {
            adminemail.error = null
            adminpassword.error = null
            if (adminemail.editText?.text.toString().isNotEmpty() &&
                adminpassword.editText?.text.toString().isNotEmpty() &&
                adminemail.editText?.text.toString() == "sgsonai008@gmail.com" &&
                adminpassword.editText?.text.toString() == "Bloodlink"
            ) {
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("username", "sgsonai008@gmail.com")
                startActivity(Intent(this, AdminDashboard::class.java))
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                adminemail.error = "Incorrect"
                adminpassword.error = "Incorrect"
            }
        }

    }

}