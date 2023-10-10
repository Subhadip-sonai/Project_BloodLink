import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R
import com.sonai.bloodlink.mainactivity.home.findDonorRecyclerview.FindDonorMyAdapter
import com.sonai.bloodlink.mainactivity.home.findDonorRecyclerview.FindDonorDataData
import com.sonai.bloodlink.utilityClasses.LocationGets

class FindDonor : Fragment() {

    private lateinit var recyclerlist: RecyclerView
    private lateinit var progressbar: ProgressBar
    private lateinit var nothingPresent: TextView
    private lateinit var donor_radius_find: Spinner
    private lateinit var radius_filter_btn: Button
    private lateinit var enterTxt: EditText
    private lateinit var enterTxtbtn: Button

    private var radiusGetData: String? = null

    var shredName: String? = null

    private var latitude: Double? = null
    private var longitude: Double? = null
//    private var address: String? = null

    private var lat_max: Double = 0.0
    private var long_max: Double = 0.0
    private var lat_min: Double = 0.0
    private var long_min: Double = 0.0

    val db = FirebaseFirestore.getInstance()

    private val userArrayList: ArrayList<FindDonorDataData> = ArrayList()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_find_donor, container, false)

        try {
            shredName = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("username",null).toString()
        } catch (_: Exception){}

        val locationGets = LocationGets(requireContext())
        locationGets.locationRequest()

        recyclerlist = view.findViewById(R.id.recyclerlistfinddonor)
        progressbar = view.findViewById(R.id.progressbar)
        nothingPresent = view.findViewById(R.id.nothingPresent)
        donor_radius_find = view.findViewById(R.id.donor_redious_find)
        radius_filter_btn = view.findViewById(R.id.radious_filter)
        enterTxt = view.findViewById(R.id.enterTxt)
        enterTxtbtn = view.findViewById(R.id.enterTxtbtn)

        recyclerlist.layoutManager = LinearLayoutManager(requireContext())
        recyclerlist.setHasFixedSize(true)
        recyclerlist.adapter = FindDonorMyAdapter(userArrayList, requireContext())

        if(locationGets.locationAllowOrDeny()) {
            locationGets.getCurrentLocation { address ->
                if (address != null) {
                    latitude = address.latitude
                    longitude = address.longitude

                    lat_min = latitude?.minus(0.05)!!
                    long_min = longitude?.minus(0.05)!!
                    long_max = longitude?.plus(0.05)!!
                    lat_max = latitude?.plus(0.05)!!

//                    Log.d(TAG, latitude.toString())
//                    Log.d(TAG, longitude.toString())

                    getUserData(lat_min, lat_max, long_min, long_max)
                }
            }

            radius_filter_btn.setOnClickListener {
                radiusGetData = donor_radius_find.selectedItem.toString()
                updateLocationValues()
                getUserData(lat_min, lat_max, long_min, long_max)
            }

            enterTxtbtn.setOnClickListener {
                enterTxt.error = null
                val txt = enterTxt.text.toString()

                if (txt.isNotEmpty()) {
                    progressbar.visibility = View.VISIBLE
                    recyclerlist.visibility = View.GONE
                    nothingPresent.visibility = View.GONE
                    Handler().postDelayed({
                        progressbar.visibility = View.GONE
                        recyclerlist.visibility = View.VISIBLE

                        val add = locationGets.getLocationLatLong(txt)
                        latitude = add?.latitude
                        longitude = add?.longitude

                        lat_min = latitude?.minus(0.05)!!
                        long_min = longitude?.minus(0.05)!!
                        long_max = longitude?.plus(0.05)!!
                        lat_max = latitude?.plus(0.05)!!
                        getUserData(lat_min, lat_max, long_min, long_max)
                    }, 4000)
                } else {
                    enterTxt.error = "Enter correct location"
                    Toast.makeText(requireContext(), "Wrong location", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            nothingPresent.text = "Location permission deny"
            progressbar.visibility = View.GONE
            nothingPresent.visibility = View.VISIBLE
        }



        return view
    }


    private fun updateLocationValues() {
        radiusGetData?.let { radius ->
            val factor = when (radius) {
                "5km" -> 0.05
                "10km" -> 0.1
                "15km" -> 0.15
                "20km" -> 0.2
                else -> 0.0
            }

            factor.takeIf { it > 0.0 }?.let {
                lat_min = latitude?.minus(it)!!
                long_min = longitude?.minus(it)!!
                long_max = longitude?.plus(it)!!
                lat_max = latitude?.plus(it)!!
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getUserData(lat_min: Double, lat_max: Double, long_min: Double, long_max: Double) {
        Log.d(TAG,"line1")

        val collectionRef = db.collection("User_Information")
        collectionRef.get()
            .addOnSuccessListener { documents ->
                var count = 1
                userArrayList.clear()
                for (document in documents) {
                    val user = document.toObject(FindDonorDataData::class.java)
                    if (user.username != shredName) {
                        Log.d(TAG,"line5")
                        val lat = user.lat
                        val long = user.long

                        if ((lat != null) && (long != null) &&
                            (lat >= lat_min) && (lat <= lat_max) &&
                            (long >= long_min) && (long <= long_max)
                        ) {
                            userArrayList.add(user)
                           count = 2
                        }
                    }
                }
//                Log.d(TAG,"line10")
                recyclerlist.adapter?.notifyDataSetChanged()
//                Log.d(TAG,"line11")

                progressbar.visibility = View.GONE
                nothingPresent.visibility = View.GONE
//                Log.d(TAG,"line12")
                if (count == 1) {
                    nothingPresent.visibility = View.VISIBLE
                    recyclerlist.visibility = View.GONE
                    nothingPresent.text = "Unfortunately, no donors are available at the moment"
//                    Log.d(TAG,"line13")
                }
            }
            .addOnFailureListener {
                // Handle any errors here
                progressbar.visibility = View.GONE
            }
    }

    override fun onResume() {
        super.onResume()
        activity?.actionBar?.title = "Find Donor"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        latitude = null
        longitude = null
    }

}

