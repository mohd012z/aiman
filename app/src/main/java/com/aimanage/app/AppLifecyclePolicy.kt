package com.aimanage.app

/**
 * Conservative app lifecycle classification for Android usage-event observations.
 * These labels are evidence statements, NOT process or CPU measurements.
 */
enum class ObservedAppState {
    FOREGROUND_OBSERVED,
    BACKGROUND_TRANSITION_OBSERVED,
    USER_INTERACTION_NOT_OBSERVED,
    UNKNOWN
}

data class AppLifecycleObservation(
    val packageName: String,
    val state: ObservedAppState,
    val timestampMillis: Long,
    val evidence: String,
    val requiresReview: Boolean
)

object AppLifecyclePolicy {
    /**
     * The absence of a foreground event is never proof that an app has run
     * secretly or started a background service.
     */
    fun classify(
        packageName: String,
        eventType: Int?,
        timestampMillis: Long,
        userInteractionObserved: Boolean
    ): AppLifecycleObservation {
        val state = when (eventType) {
            android.app.usage.UsageEvents.Event.ACTIVITY_RESUMED ->
                ObservedAppState.FOREGROUND_OBSERVED
            android.app.usage.UsageEvents.Event.ACTIVITY_PAUSED,
            android.app.usage.UsageEvents.Event.ACTIVITY_STOPPED ->
                ObservedAppState.BACKGROUND_TRANSITION_OBSERVED
            else -> if (userInteractionObserved)
                ObservedAppState.UNKNOWN
            else
                ObservedAppState.USER_INTERACTION_NOT_OBSERVED
        }
        val explanation = when (state) {
            ObservedAppState.FOREGROUND_OBSERVED ->
                "Android usage event indicates an activity became foreground."
            ObservedAppState.BACKGROUND_TRANSITION_OBSERVED ->
                "Android usage event indicates an activity paused or stopped; the process may continue."
            ObservedAppState.USER_INTERACTION_NOT_OBSERVED ->
                "No matching user interaction was observed. This does not establish background execution."
            ObservedAppState.UNKNOWN ->
                "Available events do not establish whether this app is running."
        }
        return AppLifecycleObservation(packageName, state, timestampMillis, explanation, false)
    }
}
