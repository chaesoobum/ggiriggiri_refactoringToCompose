package com.friends.ggiriggiri.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.friends.ggiriggiri.MainActivity
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.room.database.NotificationDatabase
import com.friends.ggiriggiri.room.entity.NotificationEntity
import com.friends.ggiriggiri.util.tools.formatMillisToDateTime
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("FCM", "새 토큰: $token")
        super.onNewToken(token)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("FCM", "메시지 수신: ${message.notification?.title}, ${message.notification?.body}")

        val title = message.notification?.title ?: message.data["title"] ?: return
        val body = message.notification?.body ?: message.data["body"] ?: return

        //홈 스크린(요청) 갱신
        PushEventBus.refreshHomeEvent.tryEmit(Unit)
        
        showNotification(title, body)
        // 백그라운드에서 Room 저장
        CoroutineScope(Dispatchers.IO).launch {
            try {
                saveRoom(title, body)
            } catch (e: Exception) {
                Log.e("RoomInsert", "Room 저장 실패", e)
            }
        }
    }

    private suspend fun saveRoom(title: String, body: String) {
        try {
            val newNotification = NotificationEntity(
                title = title,
                content = body,
                time = formatMillisToDateTime(System.currentTimeMillis())
            )
            val db = NotificationDatabase.getInstance(applicationContext)
            db.notificationDao().insert(newNotification)
            Log.d("RoomInsert", "Room 저장 성공: $title")
        } catch (e: Exception) {
            Log.e("RoomInsert", "Room 저장 실패", e)
        }
    }


    private fun showNotification(title: String, body: String) {
        val channelId = "default_channel_id"
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "기본 채널",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        // 인텐트 설정
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigateTo", "GoHomeScreen")
        }

        // PendingIntent 생성
        val pendingIntent =
            PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 생성 및 클릭 시 인텐트 실행 설정
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.main_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent) // 이 부분이 핵심
            .build()

        manager.notify(0, notification)
    }

}
