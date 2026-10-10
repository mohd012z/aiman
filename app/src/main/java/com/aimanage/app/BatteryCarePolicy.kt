package com.aimanage.app

/** General battery-care guidance, not a battery-health measurement or charge controller. */
data class BatteryCareAdvice(val priority:String,val messages:List<String>)
object BatteryCarePolicy {
 fun evaluate(level:Int?,temperatureC:Float?,charging:Boolean):BatteryCareAdvice {
  val messages=mutableListOf<String>()
  var priority="Normal"
  if(temperatureC==null) messages+="Battery temperature unavailable; thermal condition cannot be assessed."
  else if(temperatureC>=43f) {
   priority="High temperature"
   messages+="Battery is hot. Stop demanding activity and allow the phone to cool in a ventilated area."
   if(charging) messages+="Consider unplugging the charger if safe until the device cools."
  } else if(temperatureC>=39f) {
   priority="Warm"
   messages+="Battery is warm; reduce demanding workloads and avoid direct sunlight."
  }
  if(level!=null && level<=15 && !charging) messages+="Battery level is low. Enable Android Battery Saver if you need longer standby."
  if(level!=null && level>=90 && charging) messages+="Charge is near full. Use your phone manufacturer's charge limit if available."
  messages+="AImanage cannot set charge current, force fast charging, or verify battery capacity health."
  return BatteryCareAdvice(priority,messages)
 }
}
