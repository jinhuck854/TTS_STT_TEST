package com.example.tts_stt_test

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val REQ_STT = 100
    private lateinit var tvResult: TextView
    private lateinit var btnStartStt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1) XML의 뷰랑 코드 연결하기
        tvResult = findViewById(R.id.tvResult)
        btnStartStt = findViewById(R.id.btnStartStt)

        // 2) 마이크 권한이 없으면 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                0
            )
        }

        // 3) 버튼 눌렀을 때 STT 시작
        btnStartStt.setOnClickListener {
            startStt()
        }
    }

    private fun startStt() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") // 한국어
            putExtra(RecognizerIntent.EXTRA_PROMPT, "말씀해 주세요")
        }

        try {
            startActivityForResult(intent, REQ_STT)
        } catch (e: Exception) {
            Toast.makeText(this, "음성 인식을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 4) 음성 인식 결과 받는 부분
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_STT && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            tvResult.text = result?.get(0) ?: "인식 실패"
        }
    }
}