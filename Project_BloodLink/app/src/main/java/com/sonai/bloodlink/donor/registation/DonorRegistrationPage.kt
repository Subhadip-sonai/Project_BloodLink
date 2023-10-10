package com.sonai.bloodlink.donor.registation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R
import com.sonai.bloodlink.mainactivity.dashboard.DashboardPage
import com.sonai.bloodlink.utilityClasses.LocationGets
import com.sonai.bloodlink.utilityClasses.OTPVerification


class DonorRegistrationPage : AppCompatActivity() {
    lateinit var donorname: TextInputLayout
    lateinit var donorusername: TextInputLayout
    lateinit var donorphonenumber: TextInputLayout
    lateinit var donorpassword: TextInputLayout
    lateinit var donoremail: TextInputLayout
    lateinit var donorbloodgroup: Spinner
    lateinit var donorgender: Spinner
    lateinit var donorgovtidname: Spinner
    lateinit var donorgovtidnum: TextInputLayout
    lateinit var donoraddresshouse: TextInputLayout
    lateinit var donordistrict: TextInputLayout
    lateinit var donorstate: TextInputLayout
    lateinit var donorcity: TextInputLayout
    lateinit var donorpincode: TextInputLayout
    lateinit var donorbtnregister: Button
    lateinit var progressbar: ProgressBar

    lateinit var currentlocation: LinearLayout
    private var lat: Double? = 0.0
    private var long: Double? = 0.0

    private lateinit var locationGets: LocationGets
    private var dbRef = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donor_registation_page)

        locationGets = LocationGets(this)
        locationGets.locationRequest()

        donorname = findViewById(R.id.donorname)
        donorusername = findViewById(R.id.donorusername)
        donorphonenumber = findViewById(R.id.donorphonenumber)
        donorpassword = findViewById(R.id.donorpassword)
        donoremail = findViewById(R.id.donoremail)
        donorbloodgroup = findViewById(R.id.donorbloodgroup)
        donorgender = findViewById(R.id.donorgender)
        donorgovtidname = findViewById(R.id.donorgovtidname)
        donorgovtidnum = findViewById(R.id.donorgovtidnum)
        donoraddresshouse = findViewById(R.id.donoraddresshouse)
        donordistrict = findViewById(R.id.donordistrict)
        donorstate = findViewById(R.id.donorstate)
        donorcity = findViewById(R.id.donorcity)
        donorpincode = findViewById(R.id.donorpincode)
        donorbtnregister = findViewById(R.id.donorBtnLogin)
        progressbar = findViewById(R.id.donorregisterProgressbar)

        currentlocation = findViewById(R.id.currentlocation)

        currentlocation.setOnClickListener {
            locationGets.locationRequest()
            locationGets.getCurrentLocation { address ->
                if (address != null) {
                    donoraddresshouse.editText?.setText(address.thoroughfare)
                    donorcity.editText?.setText(address.locality)
                    donorpincode.editText?.setText(address.postalCode)
                    donorstate.editText?.setText(address.adminArea)
                }
            }
        }

        donorbtnregister.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(this).edit().remove("count")
                .apply()
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                .remove("verificationId").apply()
            progressbar.visibility = View.VISIBLE
            donorbtnregister.visibility = View.GONE

            donorname.error = null
            donorusername.error = null
            donorphonenumber.error = null
            donoremail.error = null
            donorgovtidnum.error = null
            donoraddresshouse.error = null
            donordistrict.error = null
            donorstate.error = null
            donorcity.error = null
            donorpincode.error = null


            if (validateInput()) {
                val n = donorphonenumber.editText?.text.toString()
                val u = donorusername.editText?.text.toString()
                dbRef.collection("User_Information_PhoneNo").document(n).get()
                    .addOnSuccessListener { task ->
                        if (task.exists()) {
                            donorphonenumber.error = "Already Exist"
                            Toast.makeText(this, "Already Exist", Toast.LENGTH_SHORT).show()
                            progressbar.visibility = View.GONE
                            donorbtnregister.visibility = View.VISIBLE
                        } else {
                            dbRef.collection("User_Information").document(u).get()
                                .addOnSuccessListener { taskk ->
                                    if (taskk.exists()) {
                                        donorusername.error = "Already Exist"
                                        Toast.makeText(this, "Already Exist", Toast.LENGTH_SHORT)
                                            .show()
                                        progressbar.visibility = View.GONE
                                        donorbtnregister.visibility = View.VISIBLE
                                    } else {
                                        otpGenerate()
                                    }
                                }
                        }
                    }

            }

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove("count")
            .apply()
        PreferenceManager.getDefaultSharedPreferences(this).edit()
            .remove("verificationId").apply()
        startActivity(Intent(this, DashboardPage::class.java))
        finish()
    }

    //send to otp page
    private fun otpGenerate() {
        val get = OTPVerification(this)
        val phone_OTP = donorphonenumber.editText?.text.toString()
        get.sendOTP("+91$phone_OTP")
        val count = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("count", null).toString()
        if (count == "1") {
            donorphonenumber.error = "Invalid number"
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            PreferenceManager.getDefaultSharedPreferences(this).edit().remove("count").apply()
        } else {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("phone", phone_OTP).apply()
            transferUserData()
        }
    }

    private fun transferUserData() {

        //initialization
        val name_database = donorname.editText?.text.toString()
        val username_database = donorusername.editText?.text.toString()
        val phone_database = donorphonenumber.editText?.text.toString()
        val password_database = donorpassword.editText?.text.toString()
        val email_database = donoremail.editText?.text.toString()
        val govtidnum_database = donorgovtidnum.editText?.text.toString()
        val addresshouse_database = donoraddresshouse.editText?.text.toString()
        val district_database = donordistrict.editText?.text.toString()
        val state_database = donorstate.editText?.text.toString()
        val city_database = donorcity.editText?.text.toString()
        val pincode_database = donorpincode.editText?.text.toString()
        val gender_database = donorgender.selectedItem?.toString()
        val bloodgroup_database = donorbloodgroup.selectedItem?.toString()
        val govtidname_database = donorgovtidname.selectedItem?.toString()


        //Maping INFO into username
        val data_username = mapOf(
            "name" to name_database,
            "username" to username_database,
            "phone" to phone_database,
            "password" to password_database,
            "email" to email_database,
            "govtId" to govtidnum_database,
            "govtName" to govtidname_database,
            "house" to addresshouse_database,
            "district" to district_database,
            "state" to state_database,
            "city" to city_database,
            "pinCode" to pincode_database,
            "gender" to gender_database,
            "bloodGroup" to bloodgroup_database,
            "lat" to lat.toString(),
            "long" to long.toString()
        )

        val dataBundle = Bundle()

        data_username.forEach { (key, value) ->
            dataBundle.putString(key, value)
        }
        Handler().postDelayed(
            {
                val intent = Intent(this@DonorRegistrationPage, EnterOTPPage::class.java)
                intent.putExtra("data", dataBundle)
                startActivity(intent)
                finish()
            },2500
        )
    }

    private fun validateInput(): Boolean {
        val nameInput = donorname.editText?.text.toString()
        val usernameInput = donorusername.editText?.text.toString()
        val phoneInput = donorphonenumber.editText?.text.toString()
        val emailInput = donoremail.editText?.text.toString()
        val govtidnumInput = donorgovtidnum.editText?.text.toString()
        val addresshouseInput = donoraddresshouse.editText?.text.toString()
        val cityInput = donorcity.editText?.text.toString()
        val districtInput = donordistrict.editText?.text.toString()
        val stateInput = donorstate.editText?.text.toString()
        val countryInput = donorcity.editText?.text.toString()
        val pincodeInput = donorpincode.editText?.text.toString()

        if (nameInput.isEmpty()) {
            donorname.error = "Please enter your name"
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }

        if (usernameInput.isEmpty()) {
            donorusername.error = "Please enter username"
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }

        if (phoneInput.isEmpty()) {
            donorphonenumber.error = "Please enter phone number"
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }


        if (!emailInput.isValidEmail()) {
            donoremail.error = "Invalid email"
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }

        if (donorbloodgroup.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select a blood group", Toast.LENGTH_SHORT).show()
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }

        if (donorgender.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }

        if (donorgovtidname.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select a government ID type", Toast.LENGTH_SHORT).show()
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }

        if (govtidnumInput.isEmpty()) {
            donorgovtidnum.error = "Please enter your government ID number"
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }

        if (addresshouseInput.isEmpty()) {
            donoraddresshouse.error = "Please enter your address"
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }

        if (districtInput.isEmpty()) {
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            donordistrict.error = "Please enter your district"
            return false
        }

        if (cityInput.isEmpty()) {
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            donorcity.error = "Please enter your city"
            return false
        }

        if (stateInput.isEmpty()) {
            donorstate.error = "Please enter your state"
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }

        if (countryInput.isEmpty()) {
            donorcity.error = "Please enter your country"
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }

        if (pincodeInput.isEmpty()) {
            donorpincode.error = "Please enter your pincode"
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }

        val add = locationGets.getLocationLatLong("$addresshouseInput,$cityInput,$districtInput")
        lat = add?.latitude
        long = add?.longitude

        Log.d("", lat.toString())
        Log.d("", long.toString())

        if (lat == null || long == null) {
            donoraddresshouse.error = "Please enter valid address"
            Toast.makeText(this, "Invalid Address", Toast.LENGTH_SHORT).show()
            progressbar.visibility = View.GONE
            donorbtnregister.visibility = View.VISIBLE
            return false
        }
        return true
    }


    private fun String.isValidEmail(): Boolean {
        val emailPattern = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        return emailPattern.matches(this)
    }
}
