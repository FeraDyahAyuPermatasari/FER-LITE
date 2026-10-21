package com.example.fera_lite.sikuda.anggota

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.fera_lite.R
import com.example.fera_lite.databinding.ActivityBaseBinding
import com.example.fera_lite.sikuda.anggota.beranda.BerandaFragment
import com.example.fera_lite.sikuda.anggota.data.DataAnggotaFragment
import com.example.fera_lite.sikuda.anggota.pendaftaran.PendaftaranFragment
import com.example.fera_lite.sikuda.anggota.simpanan.SimpananFragment
import com.example.fera_lite.sikuda.anggota.verifikasi.VerifikasiFragment

// =====================================================================
// [MATERI P7 - BOTTOM NAVIGATION] BaseActivity
// Activity utama Admin Keanggotaan setelah login (menggantikan DashboardAnggotaActivity).
// Berisi FrameLayout (fragment_container) + BottomNavigationView 5 menu.
// =====================================================================
class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [MATERI P7] systemBars.bottom diubah menjadi 0 agar tidak ada jarak di bawah navigasi
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // [MATERI P7 - BOTTOM NAVIGATION] Langkah 3: BerandaFragment sebagai fragment default
        if (savedInstanceState == null) {
            replaceFragment(BerandaFragment())
        }

        // [MATERI P7 - BOTTOM NAVIGATION] Langkah 3: setOnItemSelectedListener -> ganti fragment
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.beranda -> {
                    replaceFragment(BerandaFragment())
                    true
                }
                R.id.daftar -> {
                    replaceFragment(PendaftaranFragment())
                    true
                }
                R.id.verifikasi -> {
                    replaceFragment(VerifikasiFragment())
                    true
                }
                R.id.anggota -> {
                    replaceFragment(DataAnggotaFragment())
                    true
                }
                R.id.simpanan -> {
                    replaceFragment(SimpananFragment())
                    true
                }
                else -> false // return false jika item tidak ada yang di klik
            }
        }
    }

    // [MATERI P7 - BOTTOM NAVIGATION] Langkah 3: function replaceFragment()
    // public agar bisa dipanggil dari fragment (contoh: Data Anggota -> Tracking Aktivitas)
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            //.addToBackStack(null) -> dinonaktifkan agar saat back langsung keluar aplikasi
            .commit()
    }

    // Dipakai tombol back / menu cepat di fragment untuk pindah tab bottom navigation
    fun bukaMenu(menuId: Int) {
        binding.bottomNavView.selectedItemId = menuId
    }
}
