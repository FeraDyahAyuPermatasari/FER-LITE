package com.example.fera_lite.sikuda.anggota.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// ===== [FITUR BARU 1 - ROOM] DAO / PERINTAH DATABASE =====
// Referensi: https://developer.android.com/training/data-storage/room/accessing-data
@Dao
interface AnggotaDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun tambah(anggota: AnggotaEntity): Long

    @Query("SELECT * FROM anggota ORDER BY id DESC")
    fun semua(): List<AnggotaEntity>

    @Query("SELECT * FROM anggota WHERE nik = :nik LIMIT 1")
    fun cariNik(nik: String): AnggotaEntity?

    @Query("SELECT * FROM anggota WHERE id = :id LIMIT 1")
    fun berdasarkanId(id: Int): AnggotaEntity?

    @Query("UPDATE anggota SET status = :status WHERE id = :id")
    fun ubahStatus(id: Int, status: String)

    @Update
    fun ubah(anggota: AnggotaEntity)

    @Query("DELETE FROM anggota WHERE id = :id")
    fun hapus(id: Int)
}
// ===== [AKHIR FITUR BARU 1 - DAO] =====
