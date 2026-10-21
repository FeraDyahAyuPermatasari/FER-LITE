package com.example.fera_lite.sikuda.anggota.simpanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fera_lite.R
import com.example.fera_lite.databinding.FragmentSimpananBinding
import com.example.fera_lite.sikuda.anggota.BaseActivity
import com.google.android.material.snackbar.Snackbar

// [MATERI P7 - FRAGMENT] A7 · Simpanan Anggota (menu Simpanan)
class SimpananFragment : Fragment() {

    // [MATERI P7] Langkah 5: definisikan binding di fragment
    private var _binding: FragmentSimpananBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // [MATERI P7] Langkah 6: Toolbar di setiap fragment
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Simpanan Anggota"
            setDisplayShowTitleEnabled(false) // judul memakai TextView custom sesuai Figma
        }

        // Tombol back di header -> kembali ke menu Beranda
        binding.btnKembali.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.beranda)
        }

        binding.btnTambah.setOnClickListener { fiturSegera("Tambah simpanan") }
        binding.btnTambahSimpanan.setOnClickListener { fiturSegera("Tambah simpanan") }
    }

    private fun fiturSegera(namaFitur: String) {
        Snackbar.make(binding.root, getString(R.string.fitur_segera, namaFitur), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
