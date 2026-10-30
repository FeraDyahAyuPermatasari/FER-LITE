package com.example.fera_lite.sikuda.anggota.verifikasi

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.sikuda.anggota.database.AnggotaEntity
import com.example.fera_lite.sikuda.anggota.database.SikudaDatabase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.Executors

// ===== [FITUR BARU - DETAIL VERIFIKASI BERKAS YANG NYATA] =====
// DetailVerifikasiActivity lama dibiarkan utuh; ini halaman khusus data Room.
class DetailRoomVerifikasiActivity : AppCompatActivity() {
    private val pekerja = Executors.newSingleThreadExecutor()
    private val idAnggota by lazy { intent.getIntExtra("anggotaId", -1) }
    private lateinit var konten: LinearLayout
    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        konten = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(20), dp(20), dp(20))
        }
        konten.addView(Button(this).apply { text = "← Kembali"; setOnClickListener { finish() } })
        setContentView(ScrollView(this).apply { addView(konten) })
    }

    override fun onResume() {
        super.onResume()
        pekerja.execute {
            val anggota = SikudaDatabase.get(applicationContext).anggotaDao().berdasarkanId(idAnggota)
            runOnUiThread { if (!isFinishing && !isDestroyed) tampilkan(anggota) }
        }
    }

    private fun tampilkan(a: AnggotaEntity?) {
        while (konten.childCount > 1) konten.removeViewAt(1)
        if (a == null) {
            tambahTeks("Pengajuan tidak ditemukan", 17f)
            return
        }
        tambahTeks("Detail Verifikasi Anggota", 20f)
        tambahTeks("Nama: ${a.nama}\nNIK: ${a.nik}\nAlamat: ${a.alamat}\nTelepon: ${a.telepon}\nTanggal: ${a.tanggal}\nNo Surat: ${a.noSurat}\nStatus: ${a.status}", 14f)
        buatTombolBerkas("Lihat KTP", a.ktpUri)
        buatTombolBerkas("Lihat KK", a.kkUri)
        buatTombolBerkas("Lihat Surat Pernyataan", a.suratUri)
        if (a.status == "MENUNGGU") {
            buatTombol("Terima menjadi anggota aktif") { ubahStatus(a, "AKTIF") }
            buatTombol("Tolak pengajuan") { ubahStatus(a, "DITOLAK") }
        }
    }

    private fun tambahTeks(teks: String, ukuran: Float) {
        konten.addView(TextView(this).apply {
            text = teks; textSize = ukuran
            setPadding(0, dp(12), 0, dp(12))
        })
    }

    private fun buatTombol(nama: String, aksi: () -> Unit) {
        konten.addView(Button(this).apply {
            text = nama
            setOnClickListener { aksi() }
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        })
    }

    private fun buatTombolBerkas(nama: String, uriString: String) {
        buatTombol(nama + if (uriString.isEmpty()) " (belum dilampirkan)" else " ✓") {
            if (uriString.isEmpty()) {
                Toast.makeText(this, "Berkas belum dilampirkan", Toast.LENGTH_SHORT).show()
                return@buatTombol
            }
            try {
                val uri = Uri.parse(uriString)
                val intentBuka = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, contentResolver.getType(uri) ?: "*/*")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(intentBuka)
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(this, "Tidak ada aplikasi pembuka berkas", Toast.LENGTH_LONG).show()
            } catch (_: SecurityException) {
                Toast.makeText(this, "Izin berkas sudah tidak berlaku; unggah ulang", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun ubahStatus(anggota: AnggotaEntity, status: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(if (status == "AKTIF") "Terima anggota?" else "Tolak pengajuan?")
            .setMessage("${anggota.nama} → $status")
            .setNegativeButton("Batal", null)
            .setPositiveButton("Konfirmasi") { _, _ ->
                pekerja.execute {
                    SikudaDatabase.get(applicationContext).anggotaDao().ubahStatus(anggota.id, status)
                    runOnUiThread {
                        Toast.makeText(this, "Status $status disimpan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }.show()
    }

    override fun onDestroy() {
        pekerja.shutdown()
        super.onDestroy()
    }
}
// ===== [AKHIR FITUR BARU DETAIL VERIFIKASI] =====
