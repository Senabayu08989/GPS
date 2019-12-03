package com.example.gps

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.location.*


class GpsActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    val REQUEST_CODE = 1000;

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode)
        {
            REQUEST_CODE->{
                if (grantResults.size>0){
                    if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this@GpsActivity,"Permission Gramted", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@GpsActivity,"Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
        else{
            buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

            B_start.setOnClickListener(View.OnClickListener {
                if (ActivityCompat.checkSelfPermission(
                        this@GpsActivity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        this@GpsActivity,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@GpsActivity,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CODE
                    )
                    return@OnClickListener
                }

                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )

                B_start.isEnabled = !B_start.isEnabled
                B_pause.isEnabled = !B_pause.isEnabled

            });

            B_pause.setOnClickListener(View.OnClickListener{
                if
                        (ActivityCompat.checkSelfPermission(this@GpsActivity,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                        ActivityCompat.checkSelfPermission(this@GpsActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this@GpsActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
                    return@OnClickListener
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)

                B_start.isEnabled = !B_start.isEnabled
                B_pause.isEnabled = !B_pause.isEnabled

            });

        }
    }

    private fun buildLocationCallBack() {
      locationCallback = object :LocationCallback(){
          override fun onLocationResuld(p0 :LocationResult?){
              var location= p0!!.location.get(p0!!.location.size-1)
              T_location.text=location.latitude.toString()+"/"+location.longtitude.toString()
          }

      }
    }

    private fun buildLocationRequest() {
        locationRequest=LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.fastestInterval=3000
        locationRequest.smallestDisplacement=10f

    }
}

