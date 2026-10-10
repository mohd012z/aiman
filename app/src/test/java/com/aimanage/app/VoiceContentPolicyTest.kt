package com.aimanage.app

import org.junit.Assert.*
import org.junit.Test

class VoiceContentPolicyTest {
    @Test fun defaultDoesNotSpeak() {
        assertNull(VoiceContentPolicy.compose("WhatsApp","Sarah","Secret",VoicePrivacyOptions()))
    }
    @Test fun contentRequiresTwoOptInsAndPrivateModeOff() {
        val base=VoicePrivacyOptions(true,AnnouncementDetail.INCLUDE_CONTENT,false,false)
        assertFalse(VoiceContentPolicy.compose("WhatsApp","Sarah","Secret",base)!!.contains("Secret"))
        assertFalse(VoiceContentPolicy.compose("WhatsApp","Sarah","Secret",base.copy(allowContent=true,privateMode=true))!!.contains("Secret"))
        assertTrue(VoiceContentPolicy.compose("WhatsApp","Sarah","Hello",base.copy(allowContent=true))!!.contains("Hello"))
    }
    @Test fun appOnlyNeverExposesSenderOrMessage() {
        val s=VoiceContentPolicy.compose("WhatsApp","Sarah","Secret",VoicePrivacyOptions(true,AnnouncementDetail.APP_ONLY,true,false))!!
        assertFalse(s.contains("Sarah"))
        assertFalse(s.contains("Secret"))
    }
}
