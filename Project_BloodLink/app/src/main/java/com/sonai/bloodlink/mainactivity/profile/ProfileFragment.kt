package com.sonai.bloodlink.mainactivity.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R

class ProfileFragment : Fragment() {

    lateinit var donornametext: TextView
    lateinit var donorusernametext: TextView
    lateinit var donorphonenumbertext: TextView
    lateinit var donoremailtext: TextView
    lateinit var donorgovtnametext: TextView
    lateinit var donorgovtidnumtext: TextView
    lateinit var donorbloodgrouptext: TextView
    lateinit var donorgendertext: TextView
    lateinit var donorfulladdresstext: TextView
    lateinit var donorupdatebtn: Button

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment and return it
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        donornametext = view.findViewById(R.id.donornametext)
        donorusernametext = view.findViewById(R.id.donorusernametext)
        donorphonenumbertext = view.findViewById(R.id.donorphonenumbertext)
        donoremailtext = view.findViewById(R.id.donoremailtext)
        donorgovtnametext = view.findViewById(R.id.donorgovtnametext)
        donorgovtidnumtext = view.findViewById(R.id.donorgovtidnumtext)
        donorbloodgrouptext = view.findViewById(R.id.donorbloodgrouptext)
        donorgendertext = view.findViewById(R.id.donorgendertext)
        donorfulladdresstext = view.findViewById(R.id.donorfulladdresstext)
        donorupdatebtn = view.findViewById(R.id.donorupdatebtn)


        val donor_username = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getString("username", null).toString()
        val dbRef =
            FirebaseFirestore.getInstance().collection("User_Information").document(donor_username)
        dbRef.get().addOnSuccessListener { task ->
            donornametext.text = task.data?.get("name").toString()
            donorphonenumbertext.text = task.data?.get("phone").toString()
            donoremailtext.text = task.data?.get("email").toString()
            donorgovtnametext.text = task.data?.get("govtName").toString()
            donorgovtidnumtext.text = task.data?.get("govtId").toString()
            donorbloodgrouptext.text = task.data?.get("bloodGroup").toString()
            donorgendertext.text = task.data?.get("gender").toString()
            donorfulladdresstext.text = "${task.data?.get("house").toString()}, ${
                task.data?.get("city").toString()
            }, ${task.data?.get("district").toString()}, ${
                task.data?.get("state").toString()
            }, ${task.data?.get("pinCode").toString()}"
        }

        donorusernametext.text = donor_username

        donorupdatebtn.setOnClickListener {

            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(
                R.id.framelayout,
                ProfileUpdateFragment()
            ) // Corrected the argument here
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }


        return view

    }

    override fun onResume() {
        super.onResume()
        val toolbar =
            requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = "Profile"
    }

}



