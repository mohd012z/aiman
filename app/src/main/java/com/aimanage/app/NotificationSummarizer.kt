package com.aimanage.app

enum class SummaryLength { BRIEF, SHORT, DETAILED }
enum class SpeechContentMode { NOTIFICATION_ONLY, FULL_TEXT, SUMMARY }
data class SummaryPreferences(
    val mode: SpeechContentMode = SpeechContentMode.NOTIFICATION_ONLY,
    val length: SummaryLength = SummaryLength.SHORT,
    val consent: Boolean = false
)

/** Extractive, offline, conservative summarization. Does not invent facts. */
object NotificationSummarizer {
    fun summarize(text: String, length: SummaryLength): String {
        val clean = text.replace(Regex("\\s+"), " ").trim().take(2000)
        if (clean.isEmpty()) return ""
        val sentences = clean.split(Regex("(?<=[.!?])\\s+"))
        val count = when(length) {
            SummaryLength.BRIEF -> 1
            SummaryLength.SHORT -> 2
            SummaryLength.DETAILED -> 4
        }
        return sentences.take(count).joinToString(" ").take(500)
    }
    fun prepare(text: String, settings: SummaryPreferences): String? {
        if (!settings.consent || settings.mode == SpeechContentMode.NOTIFICATION_ONLY) return null
        return when(settings.mode) {
            SpeechContentMode.FULL_TEXT -> text.take(500)
            SpeechContentMode.SUMMARY -> summarize(text,settings.length)
            SpeechContentMode.NOTIFICATION_ONLY -> null
        }
    }
}
