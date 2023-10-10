package com.sonai.bloodlink.admin.home.agencylist

data class AgencyListDataClass(
    var username: String? = null,
    var name: String? = null,
    var phone_no: String? = null,
    val house: String? = null,
    val district: String? = null,
    val state: String? = null,
    val pincode: String? = null,
    val verify: String? = null,
    val pan_no: String? = null
)