package com.sonai.bloodlink.mainactivity.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R
import com.sonai.bloodlink.utilityClasses.LocationGets


class ProfileUpdateFragment : Fragment() {

    lateinit var donorfullnametext: EditText
    lateinit var donorusernametext: EditText
    lateinit var donorphonenumbertext: EditText
    lateinit var donoremailtext: EditText
    lateinit var donorgovtidnumtext: EditText
    lateinit var donorbloodgrouptext: EditText
    lateinit var donorgendertext: EditText
    lateinit var donorhousetextupdate: EditText
    lateinit var donorcitytextupdate: EditText
    lateinit var donordistricttextupdate: EditText
    lateinit var donorstatetextupdate: EditText
    lateinit var donorpincodetextupdate: EditText
    lateinit var donorupdatebtnupdate: Button
    lateinit var progressbar: ProgressBar

    val dbRef = FirebaseFirestore.getInstance().collection("User_Information")

    lateinit var donor_username: String

    //store the database value
    var fullname: String =""
    var username: String = ""
    var phone: String = ""
    var email: String = ""
    var govtId: String = ""
    var bloodGroup: String = ""
    var gender: String = ""
    var house: String = ""
    var city: String = ""
    var district: String = ""
    var state: String = ""
    var pinCode: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_profile_update, container, false)

        val toolbar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = "Profile Update"

        donor_username = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getString("username", null).toString()

        donorfullnametext = view.findViewById(R.id.donorfullnametext)
        donorusernametext = view.findViewById(R.id.donorusernametext)
        donorphonenumbertext = view.findViewById(R.id.donorphonenumbertext)
        donoremailtext = view.findViewById(R.id.donoremailtext)
        donorgovtidnumtext = view.findViewById(R.id.donorgovtidnumtext)
        donorbloodgrouptext = view.findViewById(R.id.donorbloodgrouptext)
        donorgendertext = view.findViewById(R.id.donorgendertext)
        donorhousetextupdate = view.findViewById(R.id.donorhousetextupdate)
        donorcitytextupdate = view.findViewById(R.id.donorcitytextupdate)
        donordistricttextupdate = view.findViewById(R.id.donordistricttextupdate)
        donorstatetextupdate = view.findViewById(R.id.donorstatetextupdate)
        donorpincodetextupdate = view.findViewById(R.id.donorpincodetextupdate)
        donorupdatebtnupdate = view.findViewById(R.id.donorupdatebtnupdate)
        progressbar = view.findViewById(R.id.progressbar)

        dbRef.document(donor_username).get().addOnSuccessListener { task ->
            fullname = task.data?.get("name").toString()
            username = donor_username
            phone = task.data?.get("phone").toString()
            email = task.data?.get("email").toString()
            govtId = task.data?.get("govtId").toString()
            bloodGroup = task.data?.get("bloodGroup").toString()
            gender = task.data?.get("gender").toString()
            house = task.data?.get("house").toString()
            city = task.data?.get("city").toString()
            district = task.data?.get("district").toString()
            state = task.data?.get("state").toString()
            pinCode = task.data?.get("pinCode").toString()


            //Set up the data
            donorfullnametext.setText(fullname)
            donorusernametext.setText(username)
            donorphonenumbertext.setText(phone)
            donoremailtext.setText(email)
            donorgovtidnumtext.setText(govtId)
            donorbloodgrouptext.setText(bloodGroup)
            donorgendertext.setText(gender)
            donorhousetextupdate.setText(house)
            donorcitytextupdate.setText(city)
            donordistricttextupdate.setText(district)
            donorstatetextupdate.setText(state)
            donorpincodetextupdate.setText(pinCode)

        }


        donorupdatebtnupdate.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            donorupdatebtnupdate.visibility = View.GONE
            //Get the data enter by user
            val name_update = donorfullnametext.text.toString()
            val username_update = donorusernametext.text.toString()
            val phone_update = donorphonenumbertext.text.toString()
            val email_update = donoremailtext.text.toString()
            val govtidno_update = donorgovtidnumtext.text.toString()
            val gender_update = donorgendertext.text.toString()
            val bloodgroup_update = donorbloodgrouptext.text.toString()
            val house_update = donorhousetextupdate.text.toString()
            val city_update = donorcitytextupdate.text.toString()
            val district_update = donordistricttextupdate.text.toString()
            val state_update = donorstatetextupdate.text.toString()
            val pinCode_update = donorpincodetextupdate.text.toString()

            if (phone_update != phone) {
                Handler().postDelayed(
                    {
                        donorphonenumbertext.error = "Can't be changed"
                        progressbar.visibility = View.GONE
                        donorupdatebtnupdate.visibility = View.VISIBLE
                    },2000
                )
            } else if (username_update == username){
                //update data to database
                val docRef = dbRef.document(username)
                if (name_update != fullname) {
                    docRef.update(mapOf("name" to name_update))
                }
                if (email_update != email) {
                    docRef.update(mapOf("email" to email_update))
                }
                if (govtidno_update != govtId) {
                    docRef.update(mapOf("govtId" to govtidno_update))
                }
                if (bloodgroup_update != bloodGroup) {
                    docRef.update(mapOf("bloodGroup" to bloodgroup_update))
                }
                if (gender_update != gender) {
                    docRef.update(mapOf("name" to name_update))
                }
                if (house_update != house) {
                    docRef.update(mapOf("house" to house_update))
                }
                if (city_update != city) {
                    docRef.update(mapOf("city" to city_update))
                }
                if (district_update != district) {
                    docRef.update(mapOf("district" to district_update))
                }
                if (state_update != state) {
                    docRef.update(mapOf("state" to state_update))
                }
                if (pinCode_update != pinCode) {
                    docRef.update(mapOf("pinCode" to pinCode_update))
                }
                val address = LocationGets(requireContext()).getLocationLatLong("$house_update,$city_update,$district_update")
                if (address == null){
                    donorhousetextupdate.error = "Invalid address"
                    progressbar.visibility = View.GONE
                    donorupdatebtnupdate.visibility = View.VISIBLE
                } else{
                    docRef.update(mapOf("lat" to address.latitude))
                    docRef.update(mapOf("long" to address.longitude))
                    Handler().postDelayed(
                        {
                            Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
                            activity?.onBackPressed()
                        }, 2000
                    )
                }
            } else {
                //update data to database
                val docRef = dbRef.document(username)
                if (name_update != fullname) {
                    docRef.update(mapOf("name" to name_update))
                }
                if (email_update != email) {
                    docRef.update(mapOf("email" to email_update))
                }
                if (govtidno_update != govtId) {
                    docRef.update(mapOf("govtId" to govtidno_update))
                }
                if (bloodgroup_update != bloodGroup) {
                    docRef.update(mapOf("bloodGroup" to bloodgroup_update))
                }
                if (gender_update != gender) {
                    docRef.update(mapOf("name" to name_update))
                }
                if (house_update != house) {
                    docRef.update(mapOf("house" to house_update))
                }
                if (city_update != city) {
                    docRef.update(mapOf("city" to city_update))
                }
                if (district_update != district) {
                    docRef.update(mapOf("district" to district_update))
                }
                if (state_update != state) {
                    docRef.update(mapOf("state" to state_update))
                }
                if (pinCode_update != pinCode) {
                    docRef.update(mapOf("pinCode" to pinCode_update))
                }
                val address = LocationGets(requireContext()).getLocationLatLong("$house_update,$city_update,$district_update")
                if (address == null){
                    donorhousetextupdate.error = "Invalid address"
                    progressbar.visibility = View.GONE
                    donorupdatebtnupdate.visibility = View.VISIBLE
                } else {
                    docRef.update(mapOf("lat" to address.latitude))
                    docRef.update(mapOf("long" to address.longitude))

                    //Now change the database
                    dbRef.document(username_update).get().addOnSuccessListener { task ->
                        if (task.exists()) {
                            Handler().postDelayed(
                                {
                                    donorusernametext.error = "Already Exists"
                                    progressbar.visibility = View.GONE
                                    donorupdatebtnupdate.visibility = View.VISIBLE
                                }, 2000
                            )
                        } else {
                            FirebaseFirestore.getInstance().collection("User_Information")
                                .document(username).update(mapOf("username" to username_update))
                            dbRef.document(username).get().addOnSuccessListener { doc ->
                                val data = doc.data
                                dbRef.document(username_update).set(data!!).addOnSuccessListener {
                                    FirebaseFirestore.getInstance().collection("User_Information")
                                        .document(username).delete()
                                    PreferenceManager.getDefaultSharedPreferences(requireContext())
                                        .edit().putString("username", username_update).apply()

                                    FirebaseFirestore.getInstance()
                                        .collection("User_Information_PhoneNo").document(phone)
                                        .update(
                                            mapOf("username" to username_update)
                                        )
                                }
                                Handler().postDelayed(
                                    {
                                        Toast.makeText(
                                            requireContext(),
                                            "Profile Updated",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        activity?.onBackPressed()
                                    }, 2000
                                )
                            }
                        }
                    }
                }
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        val toolbar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = "Profile Update"
    }
}
