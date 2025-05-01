package com.friends.ggiriggiri

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.friends.ggiriggiri.util.tools.sendPushNotification
import com.google.firebase.messaging.FirebaseMessaging


@Composable
fun TestScreen() {

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Button(
                onClick = {
                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val token = task.result
                                Log.d("FCM", "FCM 토큰: $token")
                                sendPushNotification(
                                    token = token,
                                    title = "친구 요청",
                                    body = "홍길동님이 친구 요청을 보냈습니다."
                                ) { success, result ->
                                    if (success) {
                                        Log.d("FCM", "알림 전송 성공: $result")
                                    } else {
                                        Log.e("FCM", "알림 전송 실패: $result")
                                    }
                                }
                            } else {
                                Log.w("FCM", "토큰 가져오기 실패", task.exception)
                            }
                        }
                }
            ) {

            }
        }
    }
}

