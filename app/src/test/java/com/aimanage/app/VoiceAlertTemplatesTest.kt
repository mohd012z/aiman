package com.aimanage.app

import org.junit.Assert.*
import org.junit.Test

class VoiceAlertTemplatesTest {
    @Test fun unknownCallerDoesNotInventIdentity() {
        assertTrue(VoiceAlertTemplates.render(VoiceAlertKind.KNOWN_CALLER).contains("unknown"))
    }
    @Test fun whatsappWithoutContactDoesNotRevealContent() {
        assertEquals("New WhatsApp notification.", VoiceAlertTemplates.render(VoiceAlertKind.WHATSAPP))
    }
    @Test fun supportsMalay() {
        assertTrue(VoiceAlertTemplates.render(VoiceAlertKind.SMS, "ms-MY").contains("baharu"))
    }
}
