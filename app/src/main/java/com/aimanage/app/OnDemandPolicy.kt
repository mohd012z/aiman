package com.aimanage.app

/** Advisory only: never force-stop or silently change other applications. */
enum class DemandMode { ECO, BALANCED, PERFORMANCE }
enum class AppImportance { CRITICAL, ORDINARY }
data class DemandContext(
    val mode: DemandMode,
    val foreground: Boolean,
    val thermalPressure: Boolean,
    val batteryLow: Boolean,
    val importance: AppImportance
)
data class DemandDecision(
    val runOptionalDiagnostics: Boolean,
    val suggestedReview: Boolean,
    val explanation: String
)
object OnDemandPolicy {
    fun decide(context: DemandContext): DemandDecision {
        val optional = context.foreground && !context.thermalPressure &&
            !context.batteryLow && context.mode != DemandMode.ECO
        val review = !context.foreground && context.importance == AppImportance.ORDINARY
        val message = when {
            context.thermalPressure -> "Thermal pressure: pause optional Aiman work; do not force-stop other apps."
            context.batteryLow -> "Low battery: defer optional Aiman diagnostics."
            context.importance == AppImportance.CRITICAL -> "Protect essential notifications and services."
            context.foreground -> "Foreground app: prioritize responsiveness; Android controls CPU and RAM."
            else -> "Background activity alone is not suspicious. Review restrictions only with supporting evidence."
        }
        return DemandDecision(optional, review, message)
    }
}
