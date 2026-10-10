package com.aimanage.app

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/** Opt-in speech for non-sensitive alerts. Never speaks message bodies by default. */
class VoiceAlertEngine(private val context: Context) : TextToSpeech.OnInitListener {
    private var engine: TextToSpeech? = null
    private var ready = false
    private val preferences = context.getSharedPreferences("voice_alerts", Context.MODE_PRIVATE)

    fun enabled(): Boolean = preferences.getBoolean("enabled", false)

    fun setEnabled(value: Boolean) {
        preferences.edit().putBoolean("enabled", value).apply()
        if (!value) engine?.stop()
    }

    fun initialize() {
        if (engine == null) engine = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        ready = status == TextToSpeech.SUCCESS
        if (ready) {
            val tag = preferences.getString("language", "en") ?: "en"
            val locale = if (tag == "ms") Locale.forLanguageTag("ms-MY") else Locale.ENGLISH
            ready = engine?.setLanguage(locale) != TextToSpeech.LANG_MISSING_DATA &&
                engine?.isLanguageAvailable(locale) != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    fun announceCategory(category: String, appLabel: String) {
        if (!enabled() || !ready) return
        val allowed = setOf("device", "call", "message")
        if (category !in allowed) return
        val message = when (category) {
            "device" -> "Aiman device health alert."
            "call" -> "Incoming call."
            else -> "New notification from ${appLabel.take(48)}."
        }
        engine?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "aiman-${category}")
    }

    fun shutdown() {
        engine?.stop()
        engine?.shutdown()
        engine = null
        ready = false
    }
}
