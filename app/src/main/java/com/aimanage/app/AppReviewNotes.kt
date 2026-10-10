package com.aimanage.app

import android.content.Context

/** Local, user-controlled flags. Does not restrict or terminate other apps. */
data class AppReviewFlags(
 val essential:Boolean=false,
 val unwantedNotifications:Boolean=false,
 val unusualBatteryUse:Boolean=false,
 val noLongerNeeded:Boolean=false
)
object AppReviewNotes {
 private const val PREFS="aimanage_app_review_flags"
 private const val MAX_PACKAGES=250
 fun load(context:Context,packageName:String):AppReviewFlags {
  if(!AppPackageIdPolicy.valid(packageName)) return AppReviewFlags()
  val p=context.getSharedPreferences(PREFS,Context.MODE_PRIVATE)
  val prefix=packageName.trim()+":"
  return AppReviewFlags(
   p.getBoolean(prefix+"essential",false),
   p.getBoolean(prefix+"alerts",false),
   p.getBoolean(prefix+"battery",false),
   p.getBoolean(prefix+"unneeded",false)
  )
 }
 fun save(context:Context,packageName:String,flags:AppReviewFlags):Boolean {
  if(!AppPackageIdPolicy.valid(packageName)) return false
  val prefix=packageName.trim()+":"
  val prefs=context.getSharedPreferences(PREFS,Context.MODE_PRIVATE)
  val distinct=prefs.all.keys.map { it.substringBefore(":") }.toSet()
  if(packageName.trim() !in distinct && distinct.size>=MAX_PACKAGES) return false
  return prefs.edit()
   .putBoolean(prefix+"essential",flags.essential)
   .putBoolean(prefix+"alerts",flags.unwantedNotifications)
   .putBoolean(prefix+"battery",flags.unusualBatteryUse)
   .putBoolean(prefix+"unneeded",flags.noLongerNeeded).commit()
 }
 fun clear(context:Context,packageName:String):Boolean {
  if(!AppPackageIdPolicy.valid(packageName)) return false
  val prefix=packageName.trim()+":"
  val editor=context.getSharedPreferences(PREFS,Context.MODE_PRIVATE).edit()
  listOf("essential","alerts","battery","unneeded").forEach { editor.remove(prefix+it) }
  return editor.commit()
 }
}
