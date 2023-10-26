package com.example.quranisapp.data.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.DatabaseView
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quranisapp.R
import com.example.quranisapp.data.database.entities.Qoran


@Database(
    entities = [Qoran::class],
    views = [Surah::class,Juz::class,Page::class,SurahJuz::class],
    version = 1
)
abstract class QoranDatabase : RoomDatabase() {
    abstract fun dao(): QoranDao
    companion object {
        @Volatile
        private var INSTANCE: QoranDatabase? = null

        fun getInstance(context: Context): QoranDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }
        private fun buildDatabase(context: Context): QoranDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                QoranDatabase::class.java,
                "qoran.db"
            ).createFromInputStream {
                context.resources.openRawResource(R.raw.qoran)
            }.fallbackToDestructiveMigration().build()
        }
    }
}


@DatabaseView("SELECT id, sora, sora_name_ar, jozz ,sora_name_en, sora_descend_place, sora_name_id, COUNT(id) as ayah_total, jozz, sora_descend_place FROM quran GROUP by sora")
data class Surah(
    @PrimaryKey val id: Int? = 0,
    @ColumnInfo(name = "sora") val surahNumber:Int? = 0,
    @ColumnInfo(name = "sora_name_ar") val surahNameAr: String? = "",
    @ColumnInfo(name = "jozz") val juzNumber : Int? = 0,
    @ColumnInfo(name = "sora_name_en") val surahNameEn: String? = "",
    @ColumnInfo(name = "ayah_total") val numberOfAyah: Int? = 0,
    @ColumnInfo(name = "sora_descend_place") val surahDescendPlace  :String? = "",
    @ColumnInfo(name = "sora_name_id") val surahNameID: String? = ""
)

//SELECT DISTINCT jozz, sora_name_en FROM quran WHERE jozz = 29 GROUP by sora_name_en
@DatabaseView("SELECT id, jozz, sora, aya_text, sora_name_en,sora_name_ar, aya_no FROM quran GROUP by sora, jozz ORDER BY id ASC")
data class Juz(
    @ColumnInfo(name = "jozz") val juzNumber : Int = 0,
    @ColumnInfo(name = "sora") val surahNumber:Int = 0,
    @ColumnInfo(name = "sora_name_en") val surahNameEn : String = "",
    @ColumnInfo(name = "sora_name_ar") val surahNameAr: String = "",
)

@DatabaseView("SELECT DISTINCT sora_name_en,sora_name_ar,sora, page FROM quran GROUP by page")
data class Page(
    @ColumnInfo(name = "page") val page: Int = 0,
    @ColumnInfo(name = "sora_name_en") val surahNameEn: String = "",
    @ColumnInfo(name = "sora_name_ar") val surahNameAr: String? = "",
    @ColumnInfo(name = "sora") val surahNumber:Int? = 0,
)

@DatabaseView("SELECT DISTINCT sora_name_en, sora_name_ar FROM quran  GROUP by sora ")
data class SurahJuz(
    @PrimaryKey val id: Int? = 0,
    @ColumnInfo(name = "sora") val surahNumber:Int? = 0,
    @ColumnInfo(name = "sora_name_ar") val surahNameAr: String? = "",
    @ColumnInfo(name = "jozz") val juzNumber : Int? = 0,
    @ColumnInfo(name = "sora_name_en") val surahNameEN: String? = "",
    @ColumnInfo(name = "ayah_total") val numberOfAyah: Int? = 0,
    @ColumnInfo(name = "sora_descend_place") val surahDescendPlace  :String? = "",
    @ColumnInfo(name = "sora_name_id") val surahNameID: String? = ""
)