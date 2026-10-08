package com.example.fera_lite.sikuda.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.R
import com.example.fera_lite.databinding.ActivityLoginBinding
import com.example.fera_lite.sikuda.anggota.dashboard.DashboardAnggotaActivity
import com.example.fera_lite.sikuda.keuangan.dashboard.DashboardKeuanganActivity
import com.example.fera_lite.sikuda.pemilik.dashboard.DashboardPemilikActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar + tombol back
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.judul_login)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        // Ketuk akun percobaan untuk mengisi username & password otomatis
        binding.chipAnggota.setOnClickListener { isiAkun("anggota") }
        binding.chipKeuangan.setOnClickListener { isiAkun("keuangan") }
        binding.chipPemilik.setOnClickListener { isiAkun("pemilik") }

        binding.btnLogin.setOnClickListener {
            val username = binding.inputUsername.text.toString().trim()
            val password = binding.inputPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Snackbar.make(binding.root, getString(R.string.login_kosong), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Akun sementara (belum terhubung ke database web)
            val intent = when {
                username == "anggota" && password == "123" ->
                    Intent(this, DashboardAnggotaActivity::class.java).apply {
                        putExtra("nama", "Siti Rahma")
                        putExtra("role", "Admin Anggota")
                    }

                username == "keuangan" && password == "123" ->
                    Intent(this, DashboardKeuanganActivity::class.java).apply {
                        putExtra("nama", "Rina Marlina")
                        putExtra("role", "Admin Keuangan")
                    }

                username == "pemilik" && password == "123" ->
                    Intent(this, DashboardPemilikActivity::class.java).apply {
                        putExtra("nama", "Ketua KUD")
                        putExtra("role", "Pemilik")
                    }

                else -> null
            }

            if (intent == null) {
                binding.inputPassword.text.clear()
                Snackbar.make(binding.root, getString(R.string.login_salah), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.tutup)) { }
                    .show()
            } else {
                startActivity(intent)
            }
        }
    }

    private fun isiAkun(username: String) {
        binding.inputUsername.setText(username)
        binding.inputPassword.setText("123")
    }

    // Tombol back di Toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
