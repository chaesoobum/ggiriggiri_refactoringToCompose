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
        }
    }
}

