package com.janu.ai

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {
    private lateinit var status: TextView
    private var tts: TextToSpeech? = null
    private val requestAudio = 1001
    private val requestSpeech = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 48, 32, 32)
        }
        val title = TextView(this).apply {
            text = "জানু AI"
            textSize = 30f
        }
        status = TextView(this).apply {
            text = "নিচের বোতামে চাপ দিয়ে বাংলায় কথা বলো।"
            textSize = 18f
            setPadding(0, 28, 0, 28)
        }
        val listen = Button(this).apply {
            text = "🎙 কথা বলো"
            setOnClickListener { checkPermissionAndListen() }
        }
        layout.addView(title)
        layout.addView(status)
        layout.addView(listen)
        setContentView(layout)
    }

    override fun onInit(result: Int) {
        if (result == TextToSpeech.SUCCESS) {
            tts?.language = Locale("bn", "BD")
        }
    }

    private fun checkPermissionAndListen() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), requestAudio)
        } else {
            listen()
        }
    }

    private fun listen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "জানু AI-কে বলো")
        }
        try {
            startActivityForResult(intent, requestSpeech)
        } catch (_: Exception) {
            say("ফোনে বাংলা ভয়েস ইনপুট সেবা পাওয়া যাচ্ছে না।")
        }
    }

    @Deprecated("Deprecated Android callback retained for broad device compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestSpeech && resultCode == RESULT_OK) {
            val spoken = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull().orEmpty()
            status.text = "তুমি বলেছ: $spoken"
            handleCommand(spoken.lowercase(Locale.ROOT))
        }
    }

    private fun handleCommand(command: String) {
        when {
            command.contains("ইউটিউব") || command.contains("youtube") -> {
                say("ইউটিউব খুলছি")
                openUrl("https://www.youtube.com")
            }
            command.contains("ফেসবুক") || command.contains("facebook") -> {
                say("ফেসবুক খুলছি")
                openUrl("https://www.facebook.com")
            }
            command.contains("সময়") || command.contains("সময়") -> {
                val now = java.text.SimpleDateFormat("hh:mm a", Locale("bn", "BD")).format(java.util.Date())
                say("এখন সময় $now")
            }
            command.contains("হ্যালো") || command.contains("জানু") -> say("হ্যাঁ, আমি শুনছি। কী করতে হবে?")
            else -> say("এই কমান্ডটি এখনো শেখানো হয়নি।")
        }
    }

    private fun openUrl(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (_: Exception) {
            Toast.makeText(this, "অ্যাপ বা ব্রাউজার খোলা যায়নি", Toast.LENGTH_SHORT).show()
        }
    }

    private fun say(message: String) {
        status.text = message
        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "janu_reply")
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
