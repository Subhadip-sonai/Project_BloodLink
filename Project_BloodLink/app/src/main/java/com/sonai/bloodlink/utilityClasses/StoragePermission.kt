package com.sonai.bloodlink.utilityClasses

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class StoragePermission(val context: Context) {

    @SuppressLint("ObsoleteSdkInt")
    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            TODO("VERSION.SDK_INT < DONUT")
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            ActivityCompat.requestPermissions(
                Activity(),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                101
            )
        }
    } //old version

}