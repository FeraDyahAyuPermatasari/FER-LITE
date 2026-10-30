package com.example.fera_lite.sikuda.anggota.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// ===== [FITUR BARU 1 - ROOM] ENTITY / STRUKTUR TABEL ANGGOTA =====
// Referensi: https://developer.android.com/training/data-storage/room/defining-data
@Entity(tableName = "anggota", indices = [Index(value = ["nik"], unique = true)])
data class AnggotaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nik: String,
    val nama: String,
    val alamat: String = "",
    val telepon: String = "",
    val tanggal: String = "",
    val noSurat: String = "",
    val status: String = "MENUNGGU",
    // ===== [PERBAIKAN FITUR 1] ALAMAT BERKAS PERSYARATAN =====
    val ktpUri: String = "",
    val kkUri: String = "",
    val suratUri: String = ""
)
// ===== [AKHIR FITUR BARU 1 - ENTITY] =====
