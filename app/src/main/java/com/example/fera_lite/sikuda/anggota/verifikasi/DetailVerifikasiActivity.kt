package com.example.fera_lite.sikuda.anggota.verifikasi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.databinding.ActivityDetailVerifikasiBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

// [P7] A5 · Detail Verifikasi
// Dibuat sebagai Activity karena di Figma halaman ini tidak memiliki bottom navigation
class DetailVerifikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailVerifikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailVerifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar + tombol back
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.btnKembali.setOnClickListener { finish() }

        listOf(binding.btnLihatKtp, binding.btnLihatKk, binding.btnLihatSurat, binding.btnBerkas).forEach {
            it.setOnClickListener {
                Snackbar.make(binding.root, "Fitur lihat PDF segera hadir", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnTerima.setOnClickListener { konfirmasi("Terima", "diterima") }
        binding.btnTolak.setOnClickListener { konfirmasi("Tolak", "ditolak") }
    }

    // [MATERI P6 - WARMING UP] AlertDialog konfirmasi
    private fun konfirmasi(aksi: String, hasil: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("$aksi pengajuan?")
            .setMessage("Pengajuan Ahmad Fauzi akan $hasil.")
            .setPositiveButton(aksi) { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Pengajuan #041 $hasil", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
