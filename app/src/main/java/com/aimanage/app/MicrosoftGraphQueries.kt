package com.aimanage.app

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/** Read-only Graph request descriptions; network calls require an authenticated MSAL token. */
object MicrosoftGraphQueries {
    private const val BASE = "https://graph.microsoft.com/v1.0"
    private fun encode(value: String) = URLEncoder.encode(value, StandardCharsets.UTF_8.name())
    fun inbox(limit: Int = 10): String {
        val count = limit.coerceIn(1, 25)
        return "$BASE/me/mailFolders/inbox/messages?\$top=$count&\$select=id,subject,from,receivedDateTime,bodyPreview,isRead&\$orderby=receivedDateTime%20desc"
    }
    fun calendar(startUtc: String, endUtc: String, limit: Int = 20): String {
        require(Regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z").matches(startUtc))
        require(Regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z").matches(endUtc))
        require(startUtc < endUtc)
        return "$BASE/me/calendarView?startDateTime=${encode(startUtc)}&endDateTime=${encode(endUtc)}&\$top=${limit.coerceIn(1,50)}&\$select=id,subject,start,end,location,organizer&\$orderby=start/dateTime"
    }
    fun summarizePreview(subject: String?, preview: String?): String {
        val cleanSubject = subject.orEmpty().replace(Regex("\\s+")," ").take(100)
        val cleanPreview = preview.orEmpty().replace(Regex("\\s+")," ").take(500)
        return NotificationSummarizer.summarize(
            listOf(cleanSubject,cleanPreview).filter { it.isNotBlank() }.joinToString(". "),
            SummaryLength.SHORT
        )
    }
}
