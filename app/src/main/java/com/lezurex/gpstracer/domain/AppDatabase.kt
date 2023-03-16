package com.lezurex.gpstracer.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lezurex.gpstracer.domain.dao.PointDao
import com.lezurex.gpstracer.domain.entity.LocalDateTimeConverter
import com.lezurex.gpstracer.domain.entity.Point

@Database(entities = [Point::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pointDao(): PointDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context, AppDatabase::class.java, "gpstracer")
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}
