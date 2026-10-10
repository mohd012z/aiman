package com.aimanage.app

import org.junit.Assert.*
import org.junit.Test

class OutlookDigestPolicyTest {
    private val mails = listOf(
        OutlookMailItem("Project update","Alex","The meeting is tomorrow.",true),
        OutlookMailItem("Old email","Sam","Already handled.",false)
    )
    @Test fun privacyModeNeverIncludesMessageText() {
        val digest=OutlookDigestPolicy.create(mails,emptyList(),false)
        assertEquals(1,digest.unreadCount)
        assertFalse(digest.mailSummary.contains("meeting"))
        assertFalse(digest.mailSummary.contains("Project update"))
    }
    @Test fun contentRequiresExplicitPermission() {
        val digest=OutlookDigestPolicy.create(mails,emptyList(),true)
        assertTrue(digest.mailSummary.contains("Project update"))
    }
    @Test fun meetingsAreBounded() {
        val meetings=(1..20).map { OutlookMeetingItem("Meeting $it","10:00","Office") }
        val digest=OutlookDigestPolicy.create(emptyList(),meetings,false)
        assertTrue(digest.meetingsSummary.contains("20 meetings"))
        assertFalse(digest.meetingsSummary.contains("Meeting 20"))
    }
}
