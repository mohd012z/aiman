package com.aimanage.app

/** Sample speech templates. Never invent caller identity or read private content by default. */
enum class VoiceAlertKind { KNOWN_CALLER, UNKNOWN_CALLER, SUSPECTED_SPAM, WHATSAPP, MESSENGER, SMS, DEVICE }
object VoiceAlertTemplates {
    fun render(kind: VoiceAlertKind, locale: String = "en", contact: String? = null): String {
        val name = contact?.trim()?.take(40)?.takeIf { it.isNotEmpty() }
        val ms = locale.startsWith("ms")
        return when (kind) {
            VoiceAlertKind.KNOWN_CALLER -> if (name == null) render(VoiceAlertKind.UNKNOWN_CALLER, locale)
                else if (ms) "Panggilan masuk daripada $name. Nombor dalam kenalan."
                else "Incoming call from $name. Saved contact."
            VoiceAlertKind.UNKNOWN_CALLER -> if (ms) "Panggilan masuk daripada nombor tidak dikenali. Identiti belum disahkan."
                else "Incoming call from an unknown number. Caller identity not verified."
            VoiceAlertKind.SUSPECTED_SPAM -> if (ms) "Amaran. Panggilan berkemungkinan spam. Identiti pemanggil belum disahkan."
                else "Warning. Potential spam call. Caller identification is uncertain."
            VoiceAlertKind.WHATSAPP -> if (name == null) {
                if (ms) "Notifikasi WhatsApp baharu." else "New WhatsApp notification."
            } else if (ms) "Mesej WhatsApp baharu daripada $name." else "New WhatsApp message from $name."
            VoiceAlertKind.MESSENGER -> if (name == null) {
                if (ms) "Notifikasi Messenger baharu." else "New Messenger notification."
            } else if (ms) "Notifikasi Messenger baharu daripada $name." else "New Messenger notification from $name."
            VoiceAlertKind.SMS -> if (ms) "Mesej teks baharu diterima." else "New text message received."
            VoiceAlertKind.DEVICE -> if (ms) "Amaran Aiman. Suhu peranti tinggi."
                else "Aiman alert. Device temperature is high."
        }
    }
}
