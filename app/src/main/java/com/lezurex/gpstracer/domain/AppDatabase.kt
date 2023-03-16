package com.lezurex.gpstracer.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lezurex.gpstracer.domain.dao.PointDao
import com.lezurex.gpstracer.domain.entity.LocalDateTimeConverter
import com.lezurex.gpstracer.domain.entity.Point

@Database(entities = [Point::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pointDao(): PointDao
}
