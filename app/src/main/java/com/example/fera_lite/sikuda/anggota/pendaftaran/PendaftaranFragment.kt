package com.example.fera_lite.sikuda.anggota.pendaftaran

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fera_lite.R
import com.example.fera_lite.databinding.FragmentPendaftaranBinding
import com.example.fera_lite.sikuda.anggota.BaseActivity
import com.google.android.material.snackbar.Snackbar

// [MATERI P7 - FRAGMENT] A2 · Pendaftaran Anggota (menu Daftar)
class PendaftaranFragment : Fragment() {

    // [MATERI P7] Langkah 5: definisikan binding di fragment
    private var _binding: FragmentPendaftaranBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPendaftaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // [MATERI P7] Langkah 6: Toolbar di setiap fragment
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Pendaftaran Anggota"
            setDisplayShowTitleEnabled(false) // judul memakai TextView custom sesuai Figma
        }

        // Tombol back di header -> kembali ke menu Beranda
        binding.btnKembali.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.beranda)
        }

        // Tombol "Daftar Anggota" -> Form Pendaftaran (A3)
        binding.fabDaftar.setOnClickListener {
            // [MATERI P7] Intent di fragment: this -> requireContext()
            startActivity(Intent(requireContext(), FormPendaftaranActivity::class.java))
        }
        binding.btnFilter.setOnClickListener { fiturSegera("Filter") }
    }

    private fun fiturSegera(namaFitur: String) {
        Snackbar.make(binding.root, getString(R.string.fitur_segera, namaFitur), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
