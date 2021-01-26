package edu.covidianie

import android.app.Application
import fr.dasilvacampos.network.monitoring.ConnectivityStateHolder.registerConnectivityBroadcaster

class CovidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        registerConnectivityBroadcaster()
    }
}