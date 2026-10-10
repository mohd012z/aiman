package com.aimanage.app

import org.junit.Assert.*
import org.junit.Test

class ChargingThermalPolicyTest {
    @Test fun unknownTemperatureDoesNotClaimSafety() {
        assertEquals(ChargingThermalBand.UNKNOWN,
            ChargingThermalPolicy.evaluate(null,true).band)
    }
    @Test fun warmAndHotAreAdvisory() {
        assertEquals(ChargingThermalBand.WARM,
            ChargingThermalPolicy.evaluate(40f,true).band)
        assertEquals(ChargingThermalBand.HOT,
            ChargingThermalPolicy.evaluate(44f,true).band)
    }
    @Test fun normalDoesNotClaimFastCharging() {
        val result=ChargingThermalPolicy.evaluate(30f,true)
        assertEquals(ChargingThermalBand.NORMAL,result.band)
        assertFalse(result.advice.contains("fast charging"))
    }
}
