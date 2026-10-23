package com.example.fera_lite.sikuda.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.databinding.ActivityEmailGmailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// [QUIZ P8 - SOAL 1] Input dan validasi email Gmail.
class EmailGmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailGmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailGmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLanjut.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()
            if (email.isEmpty() || !email.endsWith("@gmail.com", ignoreCase = true) ||
                email.substringBefore("@").isBlank() || email.contains(" ")) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Email tidak valid")
                    .setMessage("Isi email Gmail dengan benar, contoh: nama@gmail.com")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                val intent = Intent(this, RegistrasiActivity::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
            }
        }
    }
}
