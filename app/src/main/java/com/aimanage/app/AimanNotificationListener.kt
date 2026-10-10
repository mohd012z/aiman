package com.aimanage.app

import android.app.Notification
import android.content.ComponentName
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.os.SystemClock

/**
 * Optional listener: user must explicitly enable Notification Access.
 * No notification text is persisted or transmitted.
 */
class AimanNotificationListener : NotificationListenerService() {
    private var voice: VoiceAlertEngine? = null
    private val lastSpoken = mutableMapOf<String, Long>()
    override fun onListenerConnected() {
        super.onListenerConnected()
        voice=VoiceAlertEngine(applicationContext).also { it.initialize() }
    }
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null || sbn.packageName == packageName) return
        val settings=VoiceSettings.options(this)
        if (!settings.enabled) return
        val now=SystemClock.elapsedRealtime()
        val last=lastSpoken[sbn.packageName] ?: 0L
        if (now-last < 10_000L) return
        val extras=sbn.notification.extras
        val app=runCatching {
            val info=packageManager.getApplicationInfo(sbn.packageName,0)
            packageManager.getApplicationLabel(info).toString()
        }.getOrDefault("an app")
        val sender=extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
        val content=if (VoiceSettings.canSpeakContent(this)) {
            extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
        } else null
        val effective=if (VoiceSettings.canSpeakContent(this)) settings
            else settings.copy(allowContent=false)
        val text=VoiceContentPolicy.compose(app,sender,content,effective) ?: return
        lastSpoken[sbn.packageName]=now
        voice?.speak(text, AlertVolumeSettings.forPackage(sbn.packageName))
    }
    override fun onListenerDisconnected() {
        voice?.shutdown()
        voice=null
        super.onListenerDisconnected()
    }
    override fun onDestroy() {
        voice?.shutdown()
        voice=null
        super.onDestroy()
    }
}
