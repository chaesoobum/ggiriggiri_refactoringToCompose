package com.friends.ggiriggiri.room.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.friends.ggiriggiri.room.entity.NotificationEntity

//Data Access Object

@Dao
interface NotificationDao {
    @Insert
    suspend fun insert(notificationEntity: NotificationEntity)

    @Query("SELECT * FROM NotificationEntity")
    suspend fun getAll(): List<NotificationEntity>
}