package com.example.fera_lite.sikuda.anggota.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.R
import com.example.fera_lite.databinding.ActivityDashboardAnggotaBinding
import com.example.fera_lite.sikuda.guest.beranda.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class DashboardAnggotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardAnggotaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAnggotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Data dari LoginActivity (putExtra)
        val nama = intent.getStringExtra("nama") ?: "Admin"

        // Toolbar + tombol back
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.judul_dash_anggota)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        binding.txtSapaan.text = getString(R.string.sapaan, nama)

        // Menu cepat
        binding.menuInputCalon.setOnClickListener { fiturSegera("Pendaftaran") }
        binding.menuVerifikasi.setOnClickListener { fiturSegera("Verifikasi") }
        binding.menuDataAnggota.setOnClickListener { fiturSegera("Data Anggota") }
        binding.menuSimpanan.setOnClickListener { fiturSegera("Simpanan") }

        binding.btnLogout.setOnClickListener {
            tampilkanDialogLogout()
        }
    }

    // Menu logout di Toolbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            R.id.action_logout -> {
                tampilkanDialogLogout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // AlertDialog konfirmasi logout
    private fun tampilkanDialogLogout() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.konfirmasi))
            .setMessage(getString(R.string.logout_pesan))
            .setPositiveButton(getString(R.string.ya_keluar)) { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton(getString(R.string.batal)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun fiturSegera(namaFitur: String) {
        Snackbar.make(binding.root, getString(R.string.fitur_segera, namaFitur), Snackbar.LENGTH_SHORT).show()
    }
}
