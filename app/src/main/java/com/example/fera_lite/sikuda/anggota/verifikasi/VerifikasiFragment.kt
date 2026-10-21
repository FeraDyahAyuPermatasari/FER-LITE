package com.example.fera_lite.sikuda.anggota.verifikasi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fera_lite.R
import com.example.fera_lite.databinding.FragmentVerifikasiBinding
import com.example.fera_lite.sikuda.anggota.BaseActivity
import com.google.android.material.snackbar.Snackbar

// [MATERI P7 - FRAGMENT] A4 · Antrian Verifikasi (menu Verifikasi)
class VerifikasiFragment : Fragment() {

    // [MATERI P7] Langkah 5: definisikan binding di fragment
    private var _binding: FragmentVerifikasiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifikasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // [MATERI P7] Langkah 6: Toolbar di setiap fragment
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Verifikasi Anggota"
            setDisplayShowTitleEnabled(false) // judul memakai TextView custom sesuai Figma
        }

        // Tombol back di header -> kembali ke menu Beranda
        binding.btnKembali.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.beranda)
        }

        // Tombol "Tinjau berkas" -> Detail Verifikasi (A5)
        listOf(binding.btnTinjau1, binding.btnTinjau2, binding.btnTinjau3).forEach { tombol ->
            tombol.setOnClickListener {
                startActivity(Intent(requireContext(), DetailVerifikasiActivity::class.java))
            }
        }
        listOf(binding.btnTolak1, binding.btnTolak2, binding.btnTolak3).forEach { tombol ->
            tombol.setOnClickListener { fiturSegera("Tolak langsung") }
        }
        binding.btnNotifikasi.setOnClickListener { fiturSegera("Notifikasi") }
    }

    private fun fiturSegera(namaFitur: String) {
        Snackbar.make(binding.root, getString(R.string.fitur_segera, namaFitur), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
