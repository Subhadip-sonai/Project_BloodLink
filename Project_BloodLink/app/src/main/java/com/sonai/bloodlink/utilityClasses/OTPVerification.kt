package com.sonai.bloodlink.utilityClasses

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPVerification(var context: Context) {

    private var auth = FirebaseAuth.getInstance()

    //OTP generation
    fun sendOTP(number: String){
        val option = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(context as Activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(option)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("count","1").apply()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // Save the verification ID and resending token so we can use them later
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("verificationId",verificationId).apply()

        }

    }

    //verify OTP
    fun signInWithPhoneCredential(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful){
                    Toast.makeText(context, "Verification Successfull", Toast.LENGTH_SHORT).show()
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString("count","0").apply()
                }
                else{
                    if (task.exception is FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(context, "Invalid Code", Toast.LENGTH_SHORT).show()
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("count","2").apply()
                    }
                }
            }
    }

}