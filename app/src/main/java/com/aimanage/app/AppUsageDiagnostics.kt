package com.aimanage.app

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Process

data class AppUsageReading(
 val packageName:String,
 val lookbackHours:Int,
 val foregroundMillis:Long?,
 val lastUsedAtMillis:Long?,
 val permissionGranted:Boolean
)

/** UsageStats reports historical foreground activity, not live CPU or background process state. */
object AppUsageDiagnostics {
 fun read(context:Context,packageName:String,lookbackHours:Int=24):AppUsageReading {
  val hours=lookbackHours.coerceIn(1,168)
  val permitted=runCatching {
   val ops=context.getSystemService(AppOpsManager::class.java)
   ops?.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,Process.myUid(),context.packageName)==AppOpsManager.MODE_ALLOWED
  }.getOrDefault(false)
  if(!permitted) return AppUsageReading(packageName,hours,null,null,false)
  val now=System.currentTimeMillis()
  val start=now-hours*3_600_000L
  val stats=runCatching {
   val manager=context.getSystemService(UsageStatsManager::class.java)
   manager?.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,start,now)
    ?.filter { it.packageName==packageName }
  }.getOrNull()
  return AppUsageReading(packageName,hours,
   stats?.sumOf { it.totalTimeInForeground }?.takeIf { stats.isNotEmpty() },
   stats?.maxOfOrNull { it.lastTimeUsed }?.takeIf { it>0L },
   true)
 }
}
