package com.example.fera_lite.sikuda.anggota.pendaftaran

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.fera_lite.databinding.ActivityFormPendaftaranBinding
import com.example.fera_lite.sikuda.anggota.database.AnggotaEntity
import com.example.fera_lite.sikuda.anggota.database.SikudaDatabase
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar
import java.util.concurrent.Executors

class FormPendaftaranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormPendaftaranBinding
    private val pekerja = Executors.newSingleThreadExecutor()

    // ===== [FITUR BARU - OPEN DOCUMENT] URI 3 BERKAS =====
    private var ktpUri = ""
    private var kkUri = ""
    private var suratUri = ""
    private var jenisBerkas = ""
    private val pilihBerkas = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            try {
                contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                when (jenisBerkas) {
                    "ktp" -> ktpUri = uri.toString()
                    "kk" -> kkUri = uri.toString()
                    "surat" -> suratUri = uri.toString()
                }
                val nama = namaBerkas(uri)
                Toast.makeText(this, "$jenisBerkas dipilih: $nama", Toast.LENGTH_SHORT).show()
                tandaiBerkas()
            } catch (e: SecurityException) {
                Snackbar.make(binding.root, "Gagal mengakses berkas: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }
    // ===== [AKHIR FITUR BARU OPEN DOCUMENT] =====

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPendaftaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.btnKembali.setOnClickListener { finish() }
        binding.btnBatal.setOnClickListener { finish() }

        // ===== [FITUR BARU - DATE PICKER] TAP TANGGAL UNTUK KALENDER =====
        // [REFERENSI] https://developer.android.com/develop/ui/views/components/pickers
        binding.inputTanggal.apply {
            isFocusable = false
            isClickable = true
            setOnClickListener {
                val c = Calendar.getInstance()
                val dlg = DatePickerDialog(this@FormPendaftaranActivity, { _, tahun, bulan, hari ->
                    setText("%02d/%02d/%04d".format(hari, bulan + 1, tahun))
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                dlg.datePicker.maxDate = System.currentTimeMillis()
                dlg.show()
            }
        }
        // ===== [AKHIR FITUR BARU DATE PICKER] =====

        // ===== [FITUR BARU - UPLOAD BERKAS] KTP, KK, SURAT =====
        binding.berkasKtp.setOnClickListener { bukaDokumen("ktp") }
        binding.berkasKk.setOnClickListener { bukaDokumen("kk") }
        binding.berkasSurat.setOnClickListener { bukaDokumen("surat") }
        // ===== [AKHIR FITUR BARU UPLOAD] =====

        if (savedInstanceState != null) {
            ktpUri = savedInstanceState.getString("ktpUri", "")
            kkUri = savedInstanceState.getString("kkUri", "")
            suratUri = savedInstanceState.getString("suratUri", "")
            tandaiBerkas()
        }

        binding.btnSimpan.setOnClickListener {
            val nik = binding.inputNik.text.toString().trim().replace(" ", "")
            val nama = binding.inputNama.text.toString().trim()
            val tanggal = binding.inputTanggal.text.toString().trim()
            if (!nik.matches(Regex("[0-9]{16}"))) {
                Snackbar.make(binding.root, "NIK harus berisi 16 angka", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (nama.isBlank() || tanggal.isBlank()) {
                Snackbar.make(binding.root, "Nama dan tanggal wajib diisi", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (ktpUri.isBlank() || kkUri.isBlank() || suratUri.isBlank()) {
                Snackbar.make(binding.root, "Lengkapi berkas KTP, KK, dan surat", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            // ===== [PERBAIKAN ROOM] BERKAS DAN DATA DISIMPAN; STATUS MENUNGGU =====
            val anggota = AnggotaEntity(
                nik = nik, nama = nama,
                alamat = binding.inputAlamat.text.toString().trim(),
                telepon = binding.inputTelepon.text.toString().trim(),
                tanggal = tanggal,
                noSurat = binding.inputNoSurat.text.toString().trim(),
                status = "MENUNGGU", ktpUri = ktpUri, kkUri = kkUri, suratUri = suratUri
            )
            binding.btnSimpan.isEnabled = false
            pekerja.execute {
                val hasil = try {
                    val dao = SikudaDatabase.get(applicationContext).anggotaDao()
                    if (dao.cariNik(nik) != null) "DUPLIKAT" else {
                        dao.tambah(anggota)
                        "BERHASIL"
                    }
                } catch (e: Exception) { "GAGAL: ${e.message}" }
                runOnUiThread {
                    if (!isFinishing && !isDestroyed) {
                        binding.btnSimpan.isEnabled = true
                        when (hasil) {
                            "BERHASIL" -> {
                                Toast.makeText(this, "Pendaftaran tersimpan. Buka menu Verifikasi.", Toast.LENGTH_LONG).show()
                                finish()
                            }
                            "DUPLIKAT" -> Snackbar.make(binding.root, "NIK sudah terdaftar", Snackbar.LENGTH_LONG).show()
                            else -> Snackbar.make(binding.root, hasil, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
            // ===== [AKHIR PERBAIKAN ROOM] =====
        }
    }

    private fun bukaDokumen(jenis: String) {
        jenisBerkas = jenis
        pilihBerkas.launch(arrayOf("application/pdf", "image/*"))
    }

    private fun namaBerkas(uri: Uri): String {
        return try {
            contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else uri.lastPathSegment ?: "Berkas"
            } ?: (uri.lastPathSegment ?: "Berkas")
        } catch (_: Exception) { "Berkas terpilih" }
    }

    private fun tandaiBerkas() {
        binding.berkasKtp.contentDescription = if (ktpUri.isNotEmpty()) "KTP terpilih" else "Pilih KTP"
        binding.berkasKk.contentDescription = if (kkUri.isNotEmpty()) "KK terpilih" else "Pilih KK"
        binding.berkasSurat.contentDescription = if (suratUri.isNotEmpty()) "Surat terpilih" else "Pilih surat"
        // Indikator tambahan pada kartu, tanpa mengubah layout XML lama.
        binding.berkasKtp.alpha = if (ktpUri.isNotEmpty()) 1f else 0.65f
        binding.berkasKk.alpha = if (kkUri.isNotEmpty()) 1f else 0.65f
        binding.berkasSurat.alpha = if (suratUri.isNotEmpty()) 1f else 0.65f
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("ktpUri", ktpUri)
        outState.putString("kkUri", kkUri)
        outState.putString("suratUri", suratUri)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        pekerja.shutdown()
        super.onDestroy()
    }
}
