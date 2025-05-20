package com.friends.ggiriggiri

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.friends.ggiriggiri.util.tools.sendPushNotification
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException


@Composable
fun TestScreen() {

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Button(onClick = { callIncrementGroupDayFunction() }) {
                Text("테스트 실행")
            }

        }
    }
}

fun callIncrementGroupDayFunction() {
    val functionsUrl =
        "https://us-central1-ggiriggiri-c33b2.cloudfunctions.net/createGroupDayUpdateTasks"

    val request = Request.Builder()
        .url(functionsUrl)
        .post(RequestBody.create(null, ByteArray(0))) // 빈 POST body
        .build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("FirebaseFunction", "Function call failed", e)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                Log.d("FirebaseFunction", "Function call succeeded: ${response.body?.string()}")
            } else {
                Log.e("FirebaseFunction", "Function call failed: ${response.code}")
            }
        }
    })
}

