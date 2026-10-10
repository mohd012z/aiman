package com.aimanage.app

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * OAuth authorization-code + PKCE preparation for Microsoft identity platform.
 * Never use an embedded client secret in an Android application.
 * This class prepares a browser request; it does not exchange tokens.
 */
object MicrosoftConnection {
    const val CLIENT_ID_KEY = "microsoft_client_id"
    private const val PREF = "microsoft_connection"
    fun clientId(context: Context): String =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(CLIENT_ID_KEY, "") ?: ""
    fun setClientId(context: Context, value: String) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit()
            .putString(CLIENT_ID_KEY, value.trim()).apply()
    }
    fun openRegistrationGuide(context: Context) {
        val intent=Intent(Intent.ACTION_VIEW,
            Uri.parse("https://learn.microsoft.com/en-us/entra/identity-platform/quickstart-register-app"))
        context.startActivity(intent)
    }
    fun scopes(mail: Boolean, calendar: Boolean): List<String> =
        listOf("openid", "profile", "offline_access") +
            (if (mail) listOf("Mail.Read") else emptyList()) +
            (if (calendar) listOf("Calendars.Read") else emptyList())
}
