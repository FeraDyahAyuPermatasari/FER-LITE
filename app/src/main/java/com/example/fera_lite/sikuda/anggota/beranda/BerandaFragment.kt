package com.example.fera_lite.sikuda.anggota.beranda

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fera_lite.R
import com.example.fera_lite.databinding.FragmentBerandaBinding
import com.example.fera_lite.sikuda.anggota.BaseActivity
import com.example.fera_lite.sikuda.anggota.pendaftaran.FormPendaftaranActivity
import com.example.fera_lite.sikuda.guest.beranda.MainActivity
import com.example.fera_lite.sikuda.profil.ProfilDeveloperActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

// [MATERI P7 - FRAGMENT] A1 · Dashboard Admin Anggota (menu Beranda)
// Isi DashboardAnggotaActivity (P5 & P6) dipindahkan ke fragment ini
class BerandaFragment : Fragment() {

    // [MATERI P7] Langkah 5: definisikan binding di fragment
    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    // [MATERI P7] Langkah 5: binding tombol dipindahkan ke onViewCreated()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // [MATERI P7] Langkah 6: Toolbar di setiap fragment
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Beranda"
            setDisplayShowTitleEnabled(false) // judul memakai TextView custom sesuai Figma
        }

        // [MATERI P6 - SHAREDPREFERENCES] di fragment memakai requireContext()
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val nama = sharedPref.getString("nama", "Admin") ?: "Admin"
        binding.txtSapaan.text = getString(R.string.sapaan, nama)

        // Menu cepat
        binding.menuInputCalon.setOnClickListener {
            // [MATERI P7] Intent di fragment: this -> requireContext()
            startActivity(Intent(requireContext(), FormPendaftaranActivity::class.java))
        }
        binding.menuVerifikasi.setOnClickListener {
            (requireActivity() as BaseActivity).bukaMenu(R.id.verifikasi)
        }
        binding.menuCetakLaporan.setOnClickListener { fiturSegera("Cetak laporan") }
        binding.btnNotifikasi.setOnClickListener { fiturSegera("Notifikasi") }

        // Logo bulat -> Profil Developer (tugas P6)
        binding.btnProfil.setOnClickListener {
            startActivity(Intent(requireContext(), ProfilDeveloperActivity::class.java))
        }

        binding.btnKeluar.setOnClickListener { tampilkanDialogLogout() }
    }

    // [MATERI P6 - WARMING UP] AlertDialog konfirmasi logout
    private fun tampilkanDialogLogout() {
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.konfirmasi))
            .setMessage(getString(R.string.logout_pesan))
            .setPositiveButton(getString(R.string.ya_keluar)) { dialog, _ ->
                // [MATERI P6 - SHAREDPREFERENCES] Langkah 3: hapus data saat logout
                val editor = sharedPref.edit()
                editor.clear()
                editor.apply()
                dialog.dismiss()

                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                // [MATERI P7] finish() di fragment -> requireActivity().finish()
                requireActivity().finish()
            }
            .setNegativeButton(getString(R.string.batal)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun fiturSegera(namaFitur: String) {
        Snackbar.make(binding.root, getString(R.string.fitur_segera, namaFitur), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
