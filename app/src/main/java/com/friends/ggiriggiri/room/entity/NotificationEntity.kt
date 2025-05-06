package com.friends.ggiriggiri.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class NotificationEntity(
    val title: String,
    val content: String,
    val time: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
