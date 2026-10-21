package com.example.fera_lite.sikuda.anggota.pendaftaran

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.databinding.ActivityFormPendaftaranBinding
import com.google.android.material.snackbar.Snackbar

// [P7] A3 · Form Pendaftaran Anggota
// Dibuat sebagai Activity karena di Figma halaman ini tidak memiliki bottom navigation
class FormPendaftaranActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormPendaftaranBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPendaftaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar + tombol back
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.btnKembali.setOnClickListener { finish() }
        binding.btnBatal.setOnClickListener { finish() }

        binding.berkasSurat.setOnClickListener {
            Snackbar.make(binding.root, "Fitur unggah berkas segera hadir", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnSimpan.setOnClickListener {
            val nik = binding.inputNik.text.toString().trim()
            val nama = binding.inputNama.text.toString().trim()

            if (nik.isEmpty() || nama.isEmpty()) {
                Snackbar.make(binding.root, "NIK dan nama lengkap wajib diisi", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Pendaftaran $nama tersimpan, menunggu verifikasi", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
