package com.aimanage.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.app.AppOpsManager
import android.os.Process

data class PermissionAuditEntry(val title:String,val granted:Boolean,val detail:String)
object PermissionAudit {
 fun capture(context:Context):List<PermissionAuditEntry> {
  val notifications=if(Build.VERSION.SDK_INT>=33)
   context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED
   else true
  val usage=runCatching {
   val ops=context.getSystemService(AppOpsManager::class.java)
   ops?.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,Process.myUid(),context.packageName)==AppOpsManager.MODE_ALLOWED
  }.getOrDefault(false)
  val notificationListener=Settings.Secure.getString(context.contentResolver,"enabled_notification_listeners")
   ?.split(':')?.any { it.startsWith(context.packageName+"/") }==true
  val overlay=Settings.canDrawOverlays(context)
  return listOf(
   PermissionAuditEntry("Notifications",notifications,"Required only for Aiman notification delivery on Android 13+."),
   PermissionAuditEntry("Usage access",usage,"Optional; needed for app usage statistics, not silent app termination."),
   PermissionAuditEntry("Notification listener",notificationListener,"Optional; permits reading enabled notifications. Review privacy before enabling."),
   PermissionAuditEntry("Display over other apps",overlay,"Optional; not required for normal device monitoring.")
  )
 }
}
