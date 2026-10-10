package com.aimanage.app

/** Content reading is opt-in, and requires a separate explicit privacy decision. */
enum class AnnouncementDetail { APP_ONLY, APP_AND_SENDER, INCLUDE_CONTENT }
data class VoicePrivacyOptions(
    val enabled: Boolean = false,
    val detail: AnnouncementDetail = AnnouncementDetail.APP_ONLY,
    val allowContent: Boolean = false,
    val privateMode: Boolean = true
)
object VoiceContentPolicy {
    fun compose(
        appName: String,
        sender: String?,
        content: String?,
        options: VoicePrivacyOptions
    ): String? {
        if (!options.enabled) return null
        val app = appName.trim().take(40).ifEmpty { "an app" }
        val base = "New notification from $app."
        if (options.detail == AnnouncementDetail.APP_ONLY) return base
        val who = sender?.trim()?.take(48)?.takeIf { it.isNotBlank() }
        val withSender = if (who != null) "New $app notification from $who." else base
        if (options.detail != AnnouncementDetail.INCLUDE_CONTENT ||
            !options.allowContent || options.privateMode) return withSender
        val safe = content?.replace(Regex("[\\r\\n\\t]+"), " ")
            ?.trim()?.take(160)?.takeIf { it.isNotEmpty() } ?: return withSender
        return "$withSender Message: $safe"
    }
}
