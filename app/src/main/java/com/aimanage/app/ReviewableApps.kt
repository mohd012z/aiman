package com.aimanage.app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

data class ReviewableApp(val label:String,val packageName:String)

/** Lists launchable apps visible under Android package-visibility rules, not all packages. */
object ReviewableApps {
 fun load(context:Context):List<ReviewableApp> {
  val intent=Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
  val pm=context.packageManager
  val activities=runCatching { pm.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY) }
   .getOrElse { return emptyList() }
  return activities.mapNotNull { entry ->
   val pkg=entry.activityInfo?.packageName ?: return@mapNotNull null
   ReviewableApp(entry.loadLabel(pm).toString(),pkg)
  }.distinctBy { it.packageName }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.label })
 }
}
