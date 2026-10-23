package com.example.fera_lite.sikuda.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.R
import com.example.fera_lite.databinding.ActivityLoginBinding
import com.example.fera_lite.sikuda.anggota.BaseActivity
import com.example.fera_lite.sikuda.auth.register.EmailGmailActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

// Halaman Login (AuthActivity pada materi)
// Fokus: hanya role Admin Keanggotaan (role keuangan & pemilik tidak dilanjutkan)
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [MATERI P6 - SHAREDPREFERENCES] Langkah 1:
        // Kode ini harus selalu dipanggil saat butuh akses "user_pref"
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // [MATERI P6 - SPLASH SCREEN] Langkah 4:
        // Pengecekan isLogin (if isLogin -> MainActivity) sudah DIPINDAHKAN
        // ke SplashScreenActivity, jadi di sini tidak dicek lagi.

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

        // [QUIZ P8 - SOAL 1] Tombol Register With Gmail.
        binding.btnRegisterGmail.setOnClickListener {
            startActivity(Intent(this, EmailGmailActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            // (A) Ambil input dari EditText
            val username = binding.inputUsername.text.toString().trim()
            val password = binding.inputPassword.text.toString()

            // (B) Cek input kosong
            if (username.isEmpty() || password.isEmpty()) {
                Snackbar.make(binding.root, getString(R.string.login_kosong), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // [QUIZ P8 - SOAL 2] Memeriksa akun lama serta data registrasi.
            val akunPref = getSharedPreferences("registrasi_pref", MODE_PRIVATE)
            val cocokAkunBaru = username == akunPref.getString("username", "") &&
                password == akunPref.getString("password", "") &&
                akunPref.getBoolean("terdaftar", false)
            val cocokUsernamePassword = username == password
            val cocokAkunLama = username == "anggota" && password == "123"

            if (cocokAkunLama || cocokUsernamePassword || cocokAkunBaru) {

                // [MATERI P6 - SHAREDPREFERENCES] Langkah 2:
                // Set isLogin menjadi true dan simpan username jika autentikasi berhasil
                val editor = sharedPref.edit()
                editor.putBoolean("isLogin", true)
                editor.putString("username", username)
                // Nama hasil registrasi hanya dipakai ketika akun tersebut yang login.
                val namaPengguna = if (cocokAkunBaru) {
                    akunPref.getString("nama", username) ?: username
                } else if (cocokAkunLama) {
                    "Siti Rahma"
                } else {
                    username
                }
                editor.putString("nama", namaPengguna)
                editor.putString("role", "Admin Anggota")
                editor.apply()

                // (D) Pindah ke halaman utama (BaseActivity + bottom navigation)
                // CLEAR_TASK: Beranda & Login dihapus dari tumpukan halaman,
                // jadi tombol back di Dashboard tidak kembali ke Login/Beranda
                // [MATERI P7] alur AuthActivity (Login) diarahkan ke BaseActivity
                val intent = Intent(this, BaseActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                // (E) Gagal: kosongkan password & tampilkan AlertDialog
                binding.inputPassword.text.clear()
                tampilkanDialogGagal()
            }
        }
    }

    // [MATERI P6 - WARMING UP] Jika username/password salah -> AlertDialog "Silahkan coba lagi"
    private fun tampilkanDialogGagal() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.login_gagal_judul))
            .setMessage(getString(R.string.login_coba_lagi))
            .setPositiveButton(getString(R.string.tutup)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun isiAkun(username: String) {
        binding.inputUsername.setText(username)
        binding.inputPassword.setText("123")
    }

    // Tombol back di Toolbar -> kembali ke Beranda (Guest)
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
