package com.example.fera_lite.sikuda.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.databinding.ActivityRegistrasiBerhasilBinding
import com.example.fera_lite.sikuda.auth.login.LoginActivity

// [QUIZ P8 - SOAL 2] Halaman informasi setelah data tersimpan.
class RegistrasiBerhasilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrasiBerhasilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBerhasilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLanjut.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}
