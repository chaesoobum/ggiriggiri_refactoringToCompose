package com.friends.ggiriggiri.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.friends.ggiriggiri.room.dao.NotificationDao
import com.friends.ggiriggiri.room.entity.NotificationEntity

@Database(entities = [NotificationEntity::class], version = 1)
abstract class NotificationDatabase : RoomDatabase() {

    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var instance: NotificationDatabase? = null

        fun getInstance(context: Context): NotificationDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NotificationDatabase::class.java,
                    "notification-database"
                ).build().also { instance = it }
            }
        }
    }
}

//만약 하나의 데이터 베이스가 여러 개의 entity를 가져야 한다면 arrayOf() 안에 콤마로 구분해서 entity를 넣어주면 된다.
//@Database(entities = arrayOf(User::class, Student::class), version = 1)
//abstract class UserDatabase: RoomDatabase() {
//    abstract fun userDao(): UserDao
//}
