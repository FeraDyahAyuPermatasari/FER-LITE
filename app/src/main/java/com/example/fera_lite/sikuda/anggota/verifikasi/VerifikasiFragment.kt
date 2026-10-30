package com.example.fera_lite.sikuda.anggota.verifikasi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fera_lite.R
import com.example.fera_lite.databinding.FragmentVerifikasiBinding
import com.example.fera_lite.sikuda.anggota.BaseActivity
import com.example.fera_lite.sikuda.anggota.database.AnggotaEntity
import com.example.fera_lite.sikuda.anggota.database.SikudaDatabase
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.Executors

// [MATERI P7] Fragment lama dipertahankan, data MENUNGGU sekarang dibaca dari Room.
class VerifikasiFragment : Fragment() {
    private var _binding: FragmentVerifikasiBinding? = null
    private val binding get() = _binding!!
    private val pekerja = Executors.newSingleThreadExecutor()
    private var wadahRoom: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVerifikasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Verifikasi Anggota"
            setDisplayShowTitleEnabled(false)
        }
        binding.btnKembali.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.beranda)
        }
        binding.btnNotifikasi.setOnClickListener { fiturSegera("Notifikasi") }

        // ===== [PERBAIKAN FITUR 1 - VERIFIKASI] SEMBUNYIKAN KARTU CONTOH =====
        // Struktur XML lama tetap disimpan. Tiga kartu demo disembunyikan.
        val contoh1 = binding.btnTinjau1.parent.parent as View
        val contoh2 = binding.btnTinjau2.parent.parent as View
        val contoh3 = binding.btnTinjau3.parent.parent as View
        val listInduk = contoh1.parent as ViewGroup
        val posisi = listInduk.indexOfChild(contoh1)
        contoh1.visibility = View.GONE
        contoh2.visibility = View.GONE
        contoh3.visibility = View.GONE
        wadahRoom = LinearLayout(requireContext()).apply { orientation = LinearLayout.VERTICAL }
        listInduk.addView(wadahRoom, posisi, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        // ===== [AKHIR PERBAIKAN TAMPILAN] =====
    }

    override fun onResume() {
        super.onResume()
        muatPengajuan()
    }

    // ===== [PERBAIKAN FITUR 1 - VERIFIKASI] AMBIL PENGAJUAN MENUNGGU DARI ROOM =====
    private fun muatPengajuan() {
        pekerja.execute {
            val hasil = try {
                SikudaDatabase.get(requireContext().applicationContext).anggotaDao()
                    .semua().filter { it.status == "MENUNGGU" }
            } catch (_: Exception) { emptyList() }
            activity?.runOnUiThread {
                if (_binding == null) return@runOnUiThread
                val parent = wadahRoom ?: return@runOnUiThread
                parent.removeAllViews()
                if (hasil.isEmpty()) {
                    parent.addView(TextView(requireContext()).apply {
                        text = "Belum ada pengajuan baru. Daftarkan calon anggota di menu Pendaftaran."
                        textSize = 14f
                        setPadding(16, 24, 16, 24)
                    })
                } else {
                    hasil.forEach { buatKartu(it, parent) }
                }
            }
        }
    }

    private fun buatKartu(anggota: AnggotaEntity, parent: LinearLayout) {
        val scale = resources.displayMetrics.density
        fun dp(v: Int) = (v * scale).toInt()
        val kartu = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(14), dp(14), dp(14))
            setBackgroundResource(R.drawable.bg_kartu)
        }
        kartu.addView(TextView(requireContext()).apply {
            text = anggota.nama
            textSize = 16f
            setTextColor(0xFF263D30.toInt())
        })
        kartu.addView(TextView(requireContext()).apply {
            text = "NIK: ${anggota.nik}  •  ${anggota.tanggal}\nStatus: Menunggu verifikasi"
            textSize = 12f
        })
        kartu.addView(Button(requireContext()).apply {
            text = "Tinjau berkas & verifikasi"
            setOnClickListener {
                startActivity(Intent(requireContext(), DetailRoomVerifikasiActivity::class.java)
                    .putExtra("anggotaId", anggota.id))
            }
        })
        parent.addView(kartu, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = dp(10) })
    }
    // ===== [AKHIR PERBAIKAN VERIFIKASI ROOM] =====

    private fun fiturSegera(nama: String) {
        Snackbar.make(binding.root, getString(R.string.fitur_segera, nama), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        wadahRoom = null
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        pekerja.shutdown()
        super.onDestroy()
    }
}
