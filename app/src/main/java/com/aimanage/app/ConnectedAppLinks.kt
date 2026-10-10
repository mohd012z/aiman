package com.aimanage.app

import android.content.Context
import android.content.Intent
import android.net.Uri

/** Explicit app handoff; no mailbox access or Microsoft credentials are collected. */
object ConnectedAppLinks {
    const val OUTLOOK_PACKAGE = "com.microsoft.office.outlook"
    fun outlook(context: Context): Boolean {
        val intent = context.packageManager.getLaunchIntentForPackage(OUTLOOK_PACKAGE)
            ?: Intent(Intent.ACTION_VIEW, Uri.parse("https://outlook.office.com/mail/"))
        return runCatching {
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }.isSuccess
    }
    fun calendar(context: Context): Boolean = runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW,
            Uri.parse("https://outlook.office.com/calendar/"))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }.isSuccess
    fun composeEmail(context: Context, recipient: String = "", subject: String = ""): Boolean =
        runCatching {
            val uri = Uri.parse("mailto:").buildUpon().appendQueryParameter("subject",subject).build()
            val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
                if (recipient.isNotBlank()) putExtra(Intent.EXTRA_EMAIL,arrayOf(recipient))
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent,"Choose email app")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }.isSuccess
    fun createCalendarEvent(context: Context, title: String): Boolean =
        runCatching {
            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = android.provider.CalendarContract.Events.CONTENT_URI
                putExtra(android.provider.CalendarContract.Events.TITLE,title)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }.isSuccess
    fun outlookPackage(packageName: String) = packageName == OUTLOOK_PACKAGE
}
