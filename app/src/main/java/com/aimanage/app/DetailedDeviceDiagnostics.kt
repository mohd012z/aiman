package com.aimanage.app

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.app.ActivityManager
import android.os.PowerManager

data class DetailedDeviceSnapshot(
    val manufacturer: String,
    val model: String,
    val product: String,
    val device: String,
    val androidRelease: String,
    val apiLevel: Int,
    val securityPatch: String,
    val supportedAbis: String,
    val cpuCores: Int,
    val totalRamBytes: Long?,
    val availableRamBytes: Long?,
    val storageTotalBytes: Long?,
    val storageAvailableBytes: Long?,
    val powerSaver: Boolean,
    val thermalStatus: Int?
)

object DetailedDeviceDiagnostics {
    fun capture(context: Context): DetailedDeviceSnapshot {
        val memory=ActivityManager.MemoryInfo()
        val am=context.getSystemService(ActivityManager::class.java)
        val ram=runCatching { am?.getMemoryInfo(memory); memory }.getOrNull()
        val stat=runCatching { StatFs(Environment.getDataDirectory().absolutePath) }.getOrNull()
        val power=context.getSystemService(PowerManager::class.java)
        return DetailedDeviceSnapshot(
            Build.MANUFACTURER,Build.MODEL,Build.PRODUCT,Build.DEVICE,
            Build.VERSION.RELEASE,Build.VERSION.SDK_INT,Build.VERSION.SECURITY_PATCH,
            Build.SUPPORTED_ABIS.joinToString(", "),Runtime.getRuntime().availableProcessors(),
            ram?.totalMem,ram?.availMem,
            stat?.totalBytes,stat?.availableBytes,
            power?.isPowerSaveMode==true,
            power?.currentThermalStatus
        )
    }
    fun gib(bytes: Long?): String =
        bytes?.let { String.format(java.util.Locale.US,"%.2f GiB",it.toDouble()/1073741824.0) }
            ?: "Unavailable"
}
