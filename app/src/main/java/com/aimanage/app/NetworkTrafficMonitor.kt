package com.aimanage.app

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
import android.os.SystemClock

data class NetworkTrafficSnapshot(
    val capturedAtMs: Long,
    val receivedBytes: Long?,
    val sentBytes: Long?,
    val wifiActive: Boolean,
    val cellularActive: Boolean,
    val vpnActive: Boolean
)
data class NetworkTrafficRate(val downBytesPerSecond: Long, val upBytesPerSecond: Long)

/** Device-wide TrafficStats are unavailable to ordinary apps on many Android releases.
 * The counters below are ONLY for Aiman's UID; never label them device-wide.
 */
object NetworkTrafficMonitor {
    fun capture(context: Context): NetworkTrafficSnapshot {
        val cm=context.getSystemService(ConnectivityManager::class.java)
        val caps=cm?.getNetworkCapabilities(cm.activeNetwork)
        val uid=context.applicationInfo.uid
        val rx=TrafficStats.getUidRxBytes(uid)
        val tx=TrafficStats.getUidTxBytes(uid)
        return NetworkTrafficSnapshot(SystemClock.elapsedRealtime(),
            rx.takeIf { it>=0 },tx.takeIf { it>=0 },
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)==true,
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)==true,
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN)==true)
    }
    fun rate(previous: NetworkTrafficSnapshot, current: NetworkTrafficSnapshot): NetworkTrafficRate? {
        val elapsed=current.capturedAtMs-previous.capturedAtMs
        val rx0=previous.receivedBytes ?: return null
        val rx1=current.receivedBytes ?: return null
        val tx0=previous.sentBytes ?: return null
        val tx1=current.sentBytes ?: return null
        if(elapsed<=0 || rx1<rx0 || tx1<tx0) return null
        return NetworkTrafficRate((rx1-rx0)*1000/elapsed,(tx1-tx0)*1000/elapsed)
    }
}
