package com.lezurex.gpstracer.domain.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lezurex.gpstracer.domain.entity.Point

@Dao
interface PointDao {
    @Query("SELECT * FROM point")
    fun getAll(): LiveData<List<Point>>

    @Insert
    fun insertAll(vararg points: Point)

}
