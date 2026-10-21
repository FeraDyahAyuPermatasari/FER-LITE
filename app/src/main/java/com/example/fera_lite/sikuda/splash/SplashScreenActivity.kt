package com.example.fera_lite.sikuda.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fera_lite.databinding.ActivitySplashScreenBinding
import com.example.fera_lite.sikuda.anggota.BaseActivity
import com.example.fera_lite.sikuda.guest.beranda.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// =====================================================================
// [MATERI P6 - SPLASH SCREEN]
// Activity pertama yang dibuka (LAUNCHER di AndroidManifest.xml).
// Menampilkan logo/branding SIKUDA, lalu mengecek sesi login.
// =====================================================================
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [MATERI P6 - SHAREDPREFERENCES] Langkah 1:
        // Kode ini harus selalu dipanggil saat butuh akses "user_pref"
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // [MATERI P6 - SPLASH SCREEN] Langkah 1: tunda 2 detik
        lifecycleScope.launch {
            delay(2000) // simulasi pengambilan data selama 2 detik

            // [MATERI P6 - SPLASH SCREEN] Langkah 4:
            // Pengecekan isLogin dipindahkan ke SplashScreenActivity
            val isLogin = sharedPref.getBoolean("isLogin", false)

            val intent = if (isLogin) {
                // isLogin = true  -> langsung ke halaman utama
                // [MATERI P7] alur SplashScreen diarahkan ke BaseActivity (bottom navigation)
                Intent(this@SplashScreenActivity, BaseActivity::class.java)
            } else {
                // isLogin = false -> ke halaman Guest (Beranda) dulu,
                // dari Beranda pengguna menekan "Masuk" untuk ke halaman Login
                Intent(this@SplashScreenActivity, MainActivity::class.java)
            }
            startActivity(intent)
            finish() // Splash ditutup agar tidak kembali ke sini saat tombol back
        }
    }
}
