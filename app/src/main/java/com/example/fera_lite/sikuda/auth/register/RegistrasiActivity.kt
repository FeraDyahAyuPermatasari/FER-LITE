package com.example.fera_lite.sikuda.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.databinding.ActivityRegistrasiBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// [QUIZ P8 - SOAL 2] Input registrasi + validasi + SharedPreferences.
class RegistrasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Email dikirim lewat Intent pada soal 1 dan field tidak dapat diedit.
        binding.inputEmail.setText(intent.getStringExtra("email") ?: "")

        binding.btnLanjut.setOnClickListener {
            val nama = binding.inputNama.text.toString().trim()
            val email = binding.inputEmail.text.toString().trim()
            val username = binding.inputUsername.text.toString()
            val password = binding.inputPassword.text.toString()

            val pesan = when {
                nama.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() ->
                    "Semua kolom wajib diisi."
                username.any { it.isWhitespace() } -> "Username tidak boleh mengandung spasi."
                password.length < 6 -> "Password minimal 6 karakter."
                else -> null
            }
            if (pesan != null) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Registrasi belum berhasil")
                    .setMessage(pesan)
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                // Materi Pertemuan 6: key-value SharedPreferences.
                // Demo kelas saja. Password asli tidak aman disimpan plaintext di SharedPreferences.
                val pref = getSharedPreferences("registrasi_pref", MODE_PRIVATE)
                pref.edit()
                    .putString("nama", nama)
                    .putString("email", email)
                    .putString("username", username)
                    .putString("password", password)
                    .putBoolean("terdaftar", true)
                    .apply()
                startActivity(Intent(this, RegistrasiBerhasilActivity::class.java))
                finish()
            }
        }
    }
}
