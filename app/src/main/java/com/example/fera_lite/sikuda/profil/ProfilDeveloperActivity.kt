package com.example.fera_lite.sikuda.profil

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.R
import com.example.fera_lite.databinding.ActivityProfilDeveloperBinding

// Halaman Profil Developer (tugas P6)
// Isi data (nama, sosmed, hobi, kekuatan) ada di res/values/strings.xml bagian "PROFIL DEVELOPER"
class ProfilDeveloperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilDeveloperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilDeveloperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar + tombol back
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.profil_developer)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
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
