package com.aimanage.app

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** User-initiated, local export of review notes. No app permissions or system state are changed. */
object AppReviewExport {
 fun toJson(context:Context):String {
  val prefs=context.getSharedPreferences("aimanage_app_review_flags",Context.MODE_PRIVATE)
  val packages=prefs.all.keys.map { it.substringBefore(':') }
   .filter { AppPackageIdPolicy.valid(it) }.distinct().sorted()
  val items=JSONArray()
  packages.forEach { pkg ->
   val flags=AppReviewNotes.load(context,pkg)
   items.put(JSONObject().apply {
    put("packageName",pkg)
    put("essential",flags.essential)
    put("unwantedNotifications",flags.unwantedNotifications)
    put("unusualBatteryUse",flags.unusualBatteryUse)
    put("noLongerNeeded",flags.noLongerNeeded)
   })
  }
  return JSONObject().apply {
   put("schemaVersion",1)
   put("exportType","aimanage-app-review")
   put("apps",items)
  }.toString(2)
 }
}
