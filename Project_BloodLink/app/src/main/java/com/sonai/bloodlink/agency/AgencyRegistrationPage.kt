package com.sonai.bloodlink.agency

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R
import com.sonai.bloodlink.donor.Login.DonorLoginPage
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AgencyRegistrationPage : AppCompatActivity() {

    private lateinit var agencyname: TextInputLayout
    private lateinit var agencypanno: TextInputLayout
    private lateinit var agencyphonenumber: TextInputLayout
    private lateinit var agencypassword: TextInputLayout
    private lateinit var agencyemail: TextInputLayout
    private lateinit var agencyregistationnumber: TextInputLayout
    private lateinit var agencyregistationdate: TextInputLayout
    private lateinit var datepicker: TextView
    private lateinit var agencyaddresshouse: TextInputLayout
    private lateinit var agencydistrict: TextInputLayout
    private lateinit var agencystate: TextInputLayout
    private lateinit var agencycountry: TextInputLayout
    private lateinit var agencypincode: TextInputLayout
    private lateinit var donorhaveaccount: TextView
    private lateinit var agencybtnregister: Button
    private lateinit var progressbar: ProgressBar

    private lateinit var name: String
    private lateinit var  pan: String
    private lateinit var  phone: String
    private lateinit var  pass: String
    private lateinit var  email: String
    private lateinit var  regno: String
    private lateinit var  date: String
    private lateinit var  house: String
    private lateinit var  dist: String
    private lateinit var  state: String
    private lateinit var  pin: String
    private lateinit var  country: String

    private lateinit var addresses: List<Address>
    private var lat: Double? = 0.0
    private var long: Double? = 0.0


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agency_registation_page)

        agencyname = findViewById(R.id.agencyname)
        agencypanno = findViewById(R.id.agencypanno)
        agencyphonenumber = findViewById(R.id.agencyphonenumber)
        agencypassword = findViewById(R.id.agencypassword)
        agencyemail = findViewById(R.id.agencyemail)
        agencyregistationnumber = findViewById(R.id.agencyregistationnumber)
        agencyregistationdate = findViewById(R.id.agencyregistationdate)
        datepicker = findViewById(R.id.datePicker)
        agencyaddresshouse = findViewById(R.id.agencyaddresshouse)
        agencydistrict = findViewById(R.id.agencydistrict)
        agencystate = findViewById(R.id.agencystate)
        agencycountry = findViewById(R.id.agencycountry)
        agencypincode = findViewById(R.id.agencypincode)
        donorhaveaccount = findViewById(R.id.donorhaveaccount)
        agencybtnregister = findViewById(R.id.agencybtnregister)
        progressbar = findViewById(R.id.progressbar)


        datepicker.setOnClickListener {
            //Pick date using Calender
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Update the EditText with the selected date
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)

                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    datepicker.text = dateFormat.format(selectedDate.time)
                    date = dateFormat.format(selectedDate.time)
                },
                year, month, day
            )

            // Show the DatePicker dialog
            datePickerDialog.show()
        }

        agencybtnregister.setOnClickListener {
            agencybtnregister.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
            //check entries
            name = agencyname.editText?.text.toString()
            pan = agencypanno.editText?.text.toString()
            pass = agencypassword.editText?.text.toString()
            email = agencyemail.editText?.text.toString()
            phone = agencyphonenumber.editText?.text.toString()
            regno = agencyregistationnumber.editText?.text.toString()
            house = agencyaddresshouse.editText?.text.toString()
            dist = agencydistrict.editText?.text.toString()
            state = agencystate.editText?.text.toString()
            pin = agencypincode.editText?.text.toString()
            country = agencycountry.editText?.text.toString()

            if(validate()) {
                getLocation("$house,$dist,$state")
                if (lat == 0.0 || long == 0.0) {
                    agencyaddresshouse.error = "Give correct address"
                    agencybtnregister.visibility = View.VISIBLE
                    progressbar.visibility = View.GONE
                } else {
                    val data = hashMapOf(
                        "name" to name,
                        "pan_no" to pan,
                        "password" to pass,
                        "email" to email,
                        "phone_no" to phone,
                        "reg_no" to regno,
                        "reg_date" to date,
                        "house" to house,
                        "district" to dist,
                        "state" to state,
                        "pincode" to pin,
                        "country" to country,
                        "latitude" to lat,
                        "longitude" to long,
                        "verify" to "not_verified"
                    )
                    val databaseReference = FirebaseFirestore.getInstance()
                    val collection = databaseReference.collection("Agency_List")
                    collection.document("$name-$pan").set(data)
                    startActivity(Intent(this, AgencyLoginPage::class.java))
                    Toast.makeText(this, "Registation Successful", Toast.LENGTH_SHORT).show()
                }
            }
        }

        donorhaveaccount.setOnClickListener {
            val intent = Intent(this@AgencyRegistrationPage, DonorLoginPage::class.java)
            startActivity(intent)
        }

    }

    private fun getLocation(locationName: String){

        val geocoder = Geocoder(this)
        addresses = emptyList()

        try {
            addresses = geocoder.getFromLocationName(locationName, 1) as List<Address>
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (addresses.isNotEmpty()) {
            val address = addresses[0]
            lat = address.latitude
            long = address.longitude
        }
    }

    private fun validate(): Boolean{

        if (name.isEmpty()){
            agencyname.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        if (pan.isEmpty()){
            agencypanno.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        if (pass.isEmpty()){
            agencypassword.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        if (phone.isEmpty()){
            agencyphonenumber.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        if (email.isEmpty()){
            agencyemail.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        if (!email.isValidEmail()){
            agencyemail.error = "Invalid Email"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }

        if (regno.isEmpty()){
            agencyregistationnumber.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        if (date.isEmpty()){
            agencyregistationdate.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        if (house.isEmpty()){
            agencyaddresshouse.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        if (dist.isEmpty()){
            agencydistrict.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        if (state.isEmpty()){
            agencystate.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        if (country.isEmpty()){
            agencycountry.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        if (pin.isEmpty()){
            agencypincode.error = "Can't be empty"
            progressbar.visibility = View.GONE
            agencybtnregister.visibility = View.VISIBLE
            return false
        }
        return true
    }
    private fun String.isValidEmail(): Boolean {
        val emailPattern = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        return emailPattern.matches(this)
    }
}