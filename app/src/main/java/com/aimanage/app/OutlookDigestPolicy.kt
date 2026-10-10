package com.aimanage.app

/** A local, read-only presentation model. No account data is stored by this class. */
data class OutlookMailItem(
    val subject: String,
    val sender: String,
    val preview: String,
    val unread: Boolean
)
data class OutlookMeetingItem(
    val title: String,
    val start: String,
    val location: String
)
data class OutlookDigest(
    val unreadCount: Int,
    val mailSummary: String,
    val meetingsSummary: String
)
object OutlookDigestPolicy {
    fun create(
        messages: List<OutlookMailItem>,
        meetings: List<OutlookMeetingItem>,
        allowMessageContent: Boolean
    ): OutlookDigest {
        val unread = messages.count { it.unread }
        val mail = if (!allowMessageContent) {
            "$unread unread emails. Message details are hidden."
        } else {
            val snippets = messages.filter { it.unread }.take(3).map {
                MicrosoftGraphQueries.summarizePreview(it.subject,it.preview)
            }.filter { it.isNotBlank() }
            if (snippets.isEmpty()) "No unread email previews."
            else "$unread unread emails. " + snippets.joinToString(" ")
        }
        val meetingText = if (meetings.isEmpty()) "No meetings in the selected period."
        else "${meetings.size} meetings. " + meetings.take(3).joinToString(" ") {
            "${it.title.take(80)} at ${it.start.take(40)}."
        }
        return OutlookDigest(unread,mail.take(600),meetingText.take(600))
    }
}
