package com.aimanage.app

import org.junit.Assert.*
import org.junit.Test

class MicrosoftPkceTest {
    @Test fun generatesDistinctValidVerifiers() {
        val first=MicrosoftPkce.verifier()
        val second=MicrosoftPkce.verifier()
        assertNotEquals(first,second)
        assertTrue(first.length in 43..128)
        assertTrue(first.matches(Regex("[A-Za-z0-9_-]+")))
    }
    @Test fun matchesRfc7636ChallengeExample() {
        assertEquals("E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM",
            MicrosoftPkce.challenge("dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"))
    }
}
