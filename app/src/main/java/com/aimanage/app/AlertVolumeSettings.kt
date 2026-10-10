package com.aimanage.app

import android.content.Context

/** Relative TTS gain, not a replacement for Android's system media volume. */
enum class AlertChannel { CALL, WHATSAPP, MESSENGER, SMS, DEVICE, OTHER }
object AlertVolumeSettings {
    private const val PREF="alert_volume"
    fun volume(context: Context, channel: AlertChannel): Float =
        context.getSharedPreferences(PREF,Context.MODE_PRIVATE)
            .getFloat(channel.name, when(channel) {
                AlertChannel.CALL -> 0.85f
                AlertChannel.DEVICE -> 0.8f
                else -> 0.65f
            }).coerceIn(0f,1f)
    fun save(context: Context, channel: AlertChannel, gain: Float) {
        context.getSharedPreferences(PREF,Context.MODE_PRIVATE)
            .edit().putFloat(channel.name,gain.coerceIn(0f,1f)).apply()
    }
    fun forPackage(packageName: String): AlertChannel = when(packageName) {
        "com.whatsapp", "com.whatsapp.w4b" -> AlertChannel.WHATSAPP
        "com.facebook.orca" -> AlertChannel.MESSENGER
        "com.google.android.apps.messaging", "com.android.mms" -> AlertChannel.SMS
        else -> AlertChannel.OTHER
    }
}
