package com.aimanage.app

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/** Stateless PKCE helpers. Verifier must be kept transient and matched on callback. */
object MicrosoftPkce {
    fun verifier(): String {
        val bytes=ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
    fun challenge(verifier: String): String {
        val hash=MessageDigest.getInstance("SHA-256").digest(verifier.toByteArray(Charsets.US_ASCII))
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
    }
}
