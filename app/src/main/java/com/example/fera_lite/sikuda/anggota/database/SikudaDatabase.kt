package com.example.fera_lite.sikuda.anggota.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// ===== [FITUR BARU 1 - ROOM] DATABASE UTAMA SIKUDA =====
// Referensi: https://developer.android.com/training/data-storage/room
@Database(entities = [AnggotaEntity::class], version = 2, exportSchema = false)
abstract class SikudaDatabase : RoomDatabase() {
    abstract fun anggotaDao(): AnggotaDao

    companion object {
        // ===== [PERBAIKAN FITUR 1] MIGRASI 1 KE 2, DATA LAMA TIDAK DIHAPUS =====
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE anggota ADD COLUMN ktpUri TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE anggota ADD COLUMN kkUri TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE anggota ADD COLUMN suratUri TEXT NOT NULL DEFAULT ''")
            }
        }
        // ===== [AKHIR PERBAIKAN MIGRASI] =====
        @Volatile private var INSTANCE: SikudaDatabase? = null

        fun get(context: Context): SikudaDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SikudaDatabase::class.java,
                    "sikuda_anggota.db"
                ).addMigrations(MIGRATION_1_2).build().also { INSTANCE = it }
            }
    }
}
// ===== [AKHIR FITUR BARU 1 - DATABASE] =====
