package com.januai.app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {
    private lateinit var status: TextView
    private var tts: TextToSpeech? = null
    private val requestAudio = 101
    private val requestSpeech = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 48, 32, 32)
        }
        status = TextView(this).apply {
            text = "জানু AI প্রস্তুত। মাইক্রোফোনে চাপ দিয়ে কথা বলো।"
            textSize = 20f
        }
        val listen = Button(this).apply {
            text = "🎙 কথা বলুন"
            setOnClickListener { startListening() }
        }
        layout.addView(status)
        layout.addView(listen)
        setContentView(layout)
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), requestAudio)
        }
    }

    private fun startListening() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), requestAudio)
            return
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "জানু শুনছে, বলুন")
        }
        try {
            startActivityForResult(intent, requestSpeech)
        } catch (_: Exception) {
            status.text = "এই ফোনে speech recognition service পাওয়া যায়নি।"
            speak("দুঃখিত, কথা শোনার সুবিধাটি পাওয়া যাচ্ছে না।")
        }
    }

    @Deprecated("Legacy speech activity callback")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestSpeech && resultCode == RESULT_OK) {
            val words = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull().orEmpty()
            status.text = "তুমি বলেছ: $words"
            handleCommand(words.lowercase(Locale.ROOT))
        }
    }

    private fun handleCommand(command: String) {
        when {
            command.contains("ইউটিউব") || command.contains("youtube") -> openApp("com.google.android.youtube", "ইউটিউব খুলছি।")
            command.contains("ফেসবুক") || command.contains("facebook") -> openApp("com.facebook.katana", "ফেসবুক খুলছি।")
            command.contains("সময়") || command.contains("সময়") || command.contains("কটা বাজে") -> {
                val time = SimpleDateFormat("h:mm a", Locale("bn", "BD")).format(Date())
                status.text = "এখন সময় $time"
                speak("এখন সময় $time")
            }
            command.contains("জানু") -> speak("হ্যাঁ, বলো। আমি শুনছি।")
            else -> {
                status.text = "এখনো এই কমান্ডটি শেখানো হয়নি।"
                speak("দুঃখিত, এই কাজটি এখনো শিখিনি।")
            }
        }
    }

    private fun openApp(packageName: String, message: String) {
        val launch = packageManager.getLaunchIntentForPackage(packageName)
        if (launch != null) {
            startActivity(launch)
            speak(message)
        } else {
            status.text = "অ্যাপটি ফোনে ইনস্টল করা নেই।"
            speak("অ্যাপটি ফোনে ইনস্টল করা নেই।")
        }
    }

    private fun speak(message: String) {
        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "janu_reply")
    }

    override fun onInit(statusCode: Int) {
        if (statusCode == TextToSpeech.SUCCESS) {
            tts?.language = Locale("bn", "BD")
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
