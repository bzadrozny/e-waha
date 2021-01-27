package com.jstarczewski.ewaha

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jstarczewski.mlog.multipart.MultiPartContent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.post
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val client = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        engine {
            config {
                retryOnConnectionFailure(true)
            }
        }
    }

    private companion object {
        private const val IMAGE_CODE = 3242
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            Timber.plant(Timber.DebugTree())

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            if (!checkPermissionForReadExtertalStorage())
                requestPermissionForReadExtertalStorage()
            if (!checkPermissionForLocation())
                requestPermissionForLocation()

            val b = findViewById<Button>(R.id.btUpload)
            b.setOnClickListener {
                val intent: Intent = Intent()
                intent.setType("image/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_CODE)
                Timber.d("ESSSAA")
            }
            val bta = findViewById<Button>(R.id.btAll)
            bta.setOnClickListener {
                startActivity(Intent(this, LocationsActivity::class.java))
            }
            getData()
        } catch (
            ex: java.lang.Exception
        ) {

        }

    }

    fun checkPermissionForReadExtertalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result: Int = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    fun checkPermissionForLocation(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result: Int = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                this!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                0
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    @Throws(Exception::class)
    fun requestPermissionForLocation() {
        try {
            ActivityCompat.requestPermissions(
                this!!,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("RESULT 997")
        if (requestCode == IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                val file = File(getImagePath(it))
                Timber.d("uri is $it")
                Timber.d("filename is $file")
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val loc = it.getResult()
                        Timber.d("Location is $loc")
                        sendData(file, loc)
                    }
                }
            }
        }
    }


    private fun sendData(file: File, loc: Location) {
        try {
            GlobalScope.launch {
                val b = client.post<String>(
                    "https://e-waha-web.azurewebsites.net/photo"
                ) {
                    body = MultiPartContent.build {
                        add("localization", "${loc.latitude}_${loc.longitude}")
                        add(
                            "photo",
                            file.readBytes(),
                            filename = "${loc.latitude}_${loc.longitude}"
                        )
                    }
                }
                client.close()
                Timber.d(b)
            }
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    private fun getData() {
        GlobalScope.launch {
            try {
                val data = client.get<String>("https://e-waha-web.azurewebsites.net/prices")
                val mapper = jacksonObjectMapper()
                val list = mapper.readValue(data, Array<Locationn>::class.java)
                list.forEach {
                    Timber.d(it.toString())
                }
                val stations = list.mapNotNull {
                    val latlang = it.localization?.split("_")
                    val lat = latlang?.get(0)?.toDouble()
                    val lng = latlang?.get(1)?.toDouble()
                    if (lat != null && lng != null) {
                        Station(
                            lat,
                            lng,
                            it.prices
                        )
                    } else {
                        null
                    }
                }
                stations.map {
                    Timber.d(it.toString())
                }
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@launch
                }
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    if (it.isSuccessful) {
                        var minDistance: Float = Float.MAX_VALUE
                        val loc = it.getResult()
                        Timber.d("DUPA")
                        Timber.d(loc.toString())
                        var nearestStation: Station? = null
                        stations.forEach {
                            val stationLocation = Location("")
                            stationLocation.longitude = it.lang
                            stationLocation.latitude = it.lat
                            val distance = loc.distanceTo(stationLocation)
                            if (distance < minDistance) {
                                minDistance = distance
                                nearestStation = it
                            }
                        }
                        nearestStation?.let {
                            Timber.d("Nearest station")
                            Timber.d(it.toString())
                            val distanceTv = findViewById<TextView>(R.id.tvDistance)
                            distanceTv.text = "Odległość: $minDistance"
                            val pricesTv = findViewById<TextView>(R.id.tvPrices)
                            it?.prices?.let {
                                pricesTv.text = it.toString()
                            }
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun getImagePath(uri: Uri): String? {
        var cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        var document_id: String = cursor!!.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor!!.close()
        cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null
        )
        cursor?.moveToFirst()
        val path: String = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor!!.close()
        return path
    }
}

