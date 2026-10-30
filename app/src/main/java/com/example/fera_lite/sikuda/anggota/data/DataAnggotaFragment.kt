package com.example.fera_lite.sikuda.anggota.data

import android.os.Bundle
import android.content.Intent
import com.example.fera_lite.sikuda.anggota.database.DaftarRoomActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fera_lite.R
import com.example.fera_lite.databinding.FragmentDataAnggotaBinding
import com.example.fera_lite.sikuda.anggota.BaseActivity
import com.example.fera_lite.sikuda.anggota.tracking.TrackingFragment
import com.google.android.material.snackbar.Snackbar

// [MATERI P7 - FRAGMENT] A6 · Data Anggota (menu Anggota)
class DataAnggotaFragment : Fragment() {

    // [MATERI P7] Langkah 5: definisikan binding di fragment
    private var _binding: FragmentDataAnggotaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataAnggotaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // [MATERI P7] Langkah 6: Toolbar di setiap fragment
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Data Anggota"
            setDisplayShowTitleEnabled(false) // judul memakai TextView custom sesuai Figma
        }

        // Tombol back di header -> kembali ke menu Beranda
        binding.btnKembali.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.beranda)
        }

        // Buka Tracking Aktivitas (A8) di dalam fragment_container (menu Anggota tetap aktif)
        binding.btnTracking.setOnClickListener {
            (requireActivity() as BaseActivity).replaceFragment(TrackingFragment())
        }
        binding.fabAnggotaLama.setOnClickListener { fiturSegera("Input anggota lama") }
        // ===== [FITUR BARU 1 - ROOM] TAHAN TOMBOL ANGGOTA LAMA UNTUK MELIHAT DATABASE =====
        // Click biasa tetap sama, sehingga fungsi lama tidak dihapus.
        binding.fabAnggotaLama.setOnLongClickListener {
            startActivity(Intent(requireContext(), DaftarRoomActivity::class.java))
            true
        }
        // ===== [AKHIR FITUR BARU 1] =====
        binding.btnUnduh.setOnClickListener { fiturSegera("Unduh data anggota") }
    }

    private fun fiturSegera(namaFitur: String) {
        Snackbar.make(binding.root, getString(R.string.fitur_segera, namaFitur), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
