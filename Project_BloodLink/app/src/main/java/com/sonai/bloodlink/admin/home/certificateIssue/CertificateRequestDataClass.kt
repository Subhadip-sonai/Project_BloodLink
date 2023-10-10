package com.sonai.bloodlink.admin.home.certificateIssue

data class CertificateRequestDataClass(
    var name: String? = null,
    var event: String? = null,
    var date: String? = null,
    var place: String? = null,
    val username: String? = null,
    val verify: String? = null,
    val campid: String? = null
)
