package com.aimanage.app

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConnectedAppsPanel(context: Context) {
    var emailVoice by remember {
        mutableStateOf(context.getSharedPreferences("connected_apps",Context.MODE_PRIVATE)
            .getBoolean("outlook_voice",false))
    }
    Column(verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Text("Microsoft Outlook",style=MaterialTheme.typography.titleLarge)
        Text("Open Outlook email or calendar. Full mailbox and calendar synchronization are not connected.")
        OutlinedButton(onClick={ConnectedAppLinks.outlook(context)},modifier=Modifier.fillMaxWidth()) {
            Text("Open Outlook email")
        }
        OutlinedButton(onClick={ConnectedAppLinks.calendar(context)},modifier=Modifier.fillMaxWidth()) {
            Text("Open Outlook calendar")
        }
        Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
            Text("Outlook voice notifications")
            Switch(emailVoice,{
                emailVoice=it
                context.getSharedPreferences("connected_apps",Context.MODE_PRIVATE)
                    .edit().putBoolean("outlook_voice",it).apply()
            })
        }
        Text("Requires Android Notification Access and Aiman voice alerts. Notification text availability depends on Outlook and device privacy settings.")
        Text("Future Microsoft Graph integration: sign in with OAuth, request Mail.Read and Calendars.Read separately, then summarize only content you approve.")
    }
}
