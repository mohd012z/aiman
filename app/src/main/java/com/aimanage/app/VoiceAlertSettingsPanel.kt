package com.aimanage.app

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object VoiceSettings {
    private const val PREF = "voice_alerts"
    fun options(context: Context): VoicePrivacyOptions {
        val p=context.getSharedPreferences(PREF,Context.MODE_PRIVATE)
        val detail=runCatching { AnnouncementDetail.valueOf(p.getString("detail","APP_ONLY")!!) }
            .getOrDefault(AnnouncementDetail.APP_ONLY)
        return VoicePrivacyOptions(p.getBoolean("enabled",false),detail,
            p.getBoolean("allow_content",false),p.getBoolean("private_mode",true))
    }
    fun save(context: Context, options: VoicePrivacyOptions) {
        context.getSharedPreferences(PREF,Context.MODE_PRIVATE).edit()
            .putBoolean("enabled",options.enabled)
            .putString("detail",options.detail.name)
            .putBoolean("allow_content",options.allowContent)
            .putBoolean("private_mode",options.privateMode).apply()
    }
    fun canSpeakContent(context: Context): Boolean {
        val keyguard=context.getSystemService(KeyguardManager::class.java)
        return keyguard?.isDeviceLocked == false && keyguard.isKeyguardLocked == false
    }
}

@Composable
fun VoiceAlertSettingsPanel(context: Context) {
    var options by remember { mutableStateOf(VoiceSettings.options(context)) }
    fun save(next: VoicePrivacyOptions) { options=next; VoiceSettings.save(context,next) }
    Column(verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Text("Voice & notification privacy",style=MaterialTheme.typography.titleLarge)
        Text("Off by default. Notification Access must be granted in Android Settings.")
        Row(horizontalArrangement=Arrangement.SpaceBetween,modifier=Modifier.fillMaxWidth()) {
            Text("Enable spoken alerts")
            Switch(options.enabled,{ save(options.copy(enabled=it)) })
        }
        Text("Announcement detail")
        AnnouncementDetail.entries.forEach { detail ->
            Row(modifier=Modifier.fillMaxWidth()) {
                RadioButton(options.detail==detail,{save(options.copy(detail=detail))})
                Text(when(detail) {
                    AnnouncementDetail.APP_ONLY -> "App name only"
                    AnnouncementDetail.APP_AND_SENDER -> "App and sender"
                    AnnouncementDetail.INCLUDE_CONTENT -> "Include message content (extra consent)"
                },modifier=Modifier.padding(top=12.dp))
            }
        }
        Row(horizontalArrangement=Arrangement.SpaceBetween,modifier=Modifier.fillMaxWidth()) {
            Text("Allow message content")
            Switch(options.allowContent,{save(options.copy(allowContent=it))})
        }
        Row(horizontalArrangement=Arrangement.SpaceBetween,modifier=Modifier.fillMaxWidth()) {
            Text("Privacy mode")
            Switch(options.privateMode,{save(options.copy(privateMode=it))})
        }
        Text("Individual voice volumes",style=MaterialTheme.typography.titleMedium)
        AlertChannel.entries.forEach { channel ->
            var gain by remember(channel) { mutableFloatStateOf(AlertVolumeSettings.volume(context,channel)) }
            Column {
                Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
                    Text(channel.name.lowercase().replaceFirstChar { it.uppercase() })
                    Text("${(gain*100).toInt()}%")
                }
                Slider(value=gain,onValueChange={ gain=it },
                    onValueChangeFinished={ AlertVolumeSettings.save(context,channel,gain) },
                    valueRange=0f..1f,enabled=options.enabled)
            }
        }
        Text("These sliders control Aiman speech gain, not other apps' notification sounds or the system volume.")
        Text("Message bodies are never spoken while the device is locked. Turn off Privacy Mode and enable content explicitly to read a notification body.")
        OutlinedButton(onClick={
            context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }) { Text("Open Notification Access settings") }
        val example=VoiceContentPolicy.compose("WhatsApp","Sarah",
            "The meeting starts at three this afternoon.",options)
        Text("Preview: ${example ?: "Voice alerts disabled"}")
    }
}
