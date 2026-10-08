package com.example.fera_lite.sikuda.guest.beranda

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.databinding.ActivitySikudaMainBinding
import com.example.fera_lite.sikuda.auth.login.LoginActivity
import com.example.fera_lite.sikuda.guest.layanan.LayananActivity
import com.example.fera_lite.sikuda.guest.tentang.TentangActivity
import com.example.fera_lite.sikuda.web.WebViewActivity

// Halaman awal SIKUDA (Guest) - tanpa Toolbar
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySikudaMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySikudaMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol Masuk di bagian atas
        binding.btnNavMasuk.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Tombol utama
        binding.btnMasukSistem.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnTentang.setOnClickListener {
            startActivity(Intent(this, TentangActivity::class.java))
        }
        binding.btnPelajariLayanan.setOnClickListener {
            startActivity(Intent(this, LayananActivity::class.java))
        }
        binding.btnBeritaKud.setOnClickListener {
            startActivity(Intent(this, WebViewActivity::class.java))
        }
    }
}
