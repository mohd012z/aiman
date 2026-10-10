package com.aimanage.app

import org.junit.Assert.*
import org.junit.Test

class BatteryCarePolicyTest {
 @Test fun hotChargingHasSafetyAdvice() {
  val advice=BatteryCarePolicy.evaluate(70,44f,true)
  assertEquals("High temperature",advice.priority)
  assertTrue(advice.messages.any { it.contains("unplugging") })
 }
 @Test fun lowBatteryAdvisesSaver() {
  val advice=BatteryCarePolicy.evaluate(10,30f,false)
  assertTrue(advice.messages.any { it.contains("Battery Saver") })
 }
 @Test fun unknownTemperatureIsNotNormalClaim() {
  val advice=BatteryCarePolicy.evaluate(null,null,false)
  assertTrue(advice.messages.any { it.contains("unavailable") })
 }
}
