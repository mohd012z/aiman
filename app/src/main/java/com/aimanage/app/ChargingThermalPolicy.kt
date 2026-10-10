package com.aimanage.app

/** Charging classification is advisory, not a charger protocol or thermal control API. */
enum class ChargingThermalBand { UNKNOWN, NORMAL, WARM, HOT }
data class ChargingGuidance(val band: ChargingThermalBand, val advice: String)
object ChargingThermalPolicy {
    fun evaluate(tempC: Float?, charging: Boolean): ChargingGuidance {
        if (tempC == null) return ChargingGuidance(ChargingThermalBand.UNKNOWN,
            "Battery temperature unavailable. Do not infer charging safety or speed.")
        if (tempC >= 43f) return ChargingGuidance(ChargingThermalBand.HOT,
            "Battery temperature is high. Disconnect charging if safe and allow the phone to cool in a ventilated place. Do not use external cooling tricks.")
        if (tempC >= 39f) return ChargingGuidance(ChargingThermalBand.WARM,
            "Battery is warm. Charging may slow automatically. Avoid demanding apps and direct sunlight.")
        return ChargingGuidance(ChargingThermalBand.NORMAL,
            if (charging) "Temperature is within the advisory range. Charging speed is controlled by the device and charger."
            else "Not charging. Battery temperature is within the advisory range.")
    }
}
