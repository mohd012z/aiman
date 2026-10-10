package com.aimanage.app

import org.junit.Assert.*
import org.junit.Test

class MicrosoftGraphQueriesTest {
    @Test fun inboxHasBoundedPageAndMinimalFields() {
        val url=MicrosoftGraphQueries.inbox(500)
        assertTrue(url.contains("top=25"))
        assertTrue(url.contains("bodyPreview"))
        assertFalse(url.contains("body,"))
    }
    @Test fun calendarRejectsInvalidRange() {
        try {
            MicrosoftGraphQueries.calendar("2026-10-11T12:00:00Z","2026-10-10T12:00:00Z")
            fail("Expected invalid range")
        } catch (_: IllegalArgumentException) {}
    }
    @Test fun summaryRemainsBounded() {
        val summary=MicrosoftGraphQueries.summarizePreview("Meeting", "Schedule changed. Confirm by noon.")
        assertTrue(summary.contains("Meeting"))
        assertTrue(summary.length <= 500)
    }
}
