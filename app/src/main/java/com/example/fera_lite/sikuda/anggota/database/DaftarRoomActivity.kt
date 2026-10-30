package com.example.fera_lite.sikuda.anggota.database

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.Executors

// ===== [FITUR BARU 1 - ROOM] LIHAT, VERIFIKASI, UBAH STATUS, HAPUS DATA =====
// Halaman baru yang terpisah dari desain asli SIKUDA.
class DaftarRoomActivity : AppCompatActivity() {
    private val pekerja = Executors.newSingleThreadExecutor()
    private lateinit var daftar: LinearLayout
    private val dao by lazy { SikudaDatabase.get(applicationContext).anggotaDao() }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val konten = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(20), dp(18), dp(20))
        }
        val kembali = Button(this).apply { text = "← Kembali"; setOnClickListener { finish() } }
        konten.addView(kembali)
        konten.addView(TextView(this).apply {
            text = "Data Anggota Tersimpan (Room)"
            textSize = 21f
            setPadding(0, dp(14), 0, dp(14))
        })
        konten.addView(TextView(this).apply {
            text = "Data lokal pada perangkat ini. Status MENUNGGU dapat diverifikasi menjadi AKTIF atau DITOLAK."
            textSize = 13f
        })
        daftar = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        konten.addView(daftar)
        setContentView(ScrollView(this).apply { addView(konten) })
    }

    override fun onResume() {
        super.onResume()
        muatData()
    }

    private fun muatData() {
        pekerja.execute {
            val anggota = dao.semua()
            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                daftar.removeAllViews()
                if (anggota.isEmpty()) {
                    daftar.addView(TextView(this).apply {
                        text = "Belum ada data di database. Tambahkan melalui menu Pendaftaran."
                        setPadding(0, dp(28), 0, 0)
                    })
                }
                anggota.forEach { orang -> kartuAnggota(orang) }
            }
        }
    }

    private fun kartuAnggota(orang: AnggotaEntity) {
        val kartu = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(14), dp(12), dp(14))
            setBackgroundColor(0xFFF0F5F0.toInt())
        }
        kartu.addView(TextView(this).apply {
            text = "${orang.nama} | ${orang.status}"
            textSize = 17f
        })
        kartu.addView(TextView(this).apply {
            text = "NIK: ${orang.nik}\nAlamat: ${orang.alamat}\nTelepon: ${orang.telepon}"
        })
        val tombol = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        fun aksi(label: String, statusBaru: String) {
            tombol.addView(Button(this).apply {
                text = label
                textSize = 11f
                setOnClickListener { konfirmasiStatus(orang, statusBaru) }
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            })
        }
        aksi("Terima", "AKTIF")
        aksi("Tolak", "DITOLAK")
        aksi("Pasif", "PASIF")
        kartu.addView(tombol)
        kartu.addView(Button(this).apply {
            text = "Hapus Data"
            setOnClickListener {
                MaterialAlertDialogBuilder(this@DaftarRoomActivity)
                    .setTitle("Hapus data ${orang.nama}?")
                    .setMessage("Data yang dihapus tidak dapat dikembalikan.")
                    .setPositiveButton("Hapus") { _, _ ->
                        pekerja.execute { dao.hapus(orang.id); runOnUiThread { muatData() } }
                    }
                    .setNegativeButton("Batal", null).show()
            }
        })
        daftar.addView(kartu, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = dp(12) })
    }

    private fun konfirmasiStatus(orang: AnggotaEntity, statusBaru: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Ubah status anggota?")
            .setMessage("${orang.nama}: ${orang.status} → $statusBaru")
            .setPositiveButton("Simpan") { _, _ ->
                pekerja.execute {
                    dao.ubahStatus(orang.id, statusBaru)
                    runOnUiThread {
                        Toast.makeText(this, "Status berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        muatData()
                    }
                }
            }.setNegativeButton("Batal", null).show()
    }

    override fun onDestroy() {
        pekerja.shutdown()
        super.onDestroy()
    }
}
// ===== [AKHIR FITUR BARU 1 - HALAMAN DATA] =====
