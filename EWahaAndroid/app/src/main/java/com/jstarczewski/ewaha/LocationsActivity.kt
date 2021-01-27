package com.jstarczewski.ewaha

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class LocationsActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locations)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getData()

    }

    private fun getData() {
        GlobalScope.launch {
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
                    this@LocationsActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this@LocationsActivity,
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
                    val markers = stations.map {
                        val stationLocation = Location("")
                        stationLocation.longitude = it.lang
                        stationLocation.latitude = it.lat
                        val distance = loc.distanceTo(stationLocation)
                        Marker(
                            it,
                            distance
                        )
                    }
                    val rec = this@LocationsActivity.findViewById<RecyclerView>(R.id.recycler)
                    rec.apply {
                        adapter = StationsAdapter()
                        setHasFixedSize(true)
                    }
                    (rec.adapter as? StationsAdapter)?.let {
                        it.updateItems(markers)
                        it.notifyDataSetChanged()
                    }
                }
            }
        }
    }

}