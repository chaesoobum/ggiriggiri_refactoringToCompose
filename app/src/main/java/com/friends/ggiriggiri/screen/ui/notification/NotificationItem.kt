package com.friends.ggiriggiri.screen.ui.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.room.entity.NotificationEntity

@Composable
fun NotificationItem(entity: NotificationEntity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = entity.title,
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )

        Text(
            text = entity.content,
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )

        Text(
            text = entity.time,
            style = androidx.compose.material3.MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNotificationItem() {
    NotificationItem(
        entity = NotificationEntity(
            title = "제목입니다",
            content = "내용입니다. 새로운 알림이 도착했습니다.",
            time = "2025.05.05 15:40"
        )
    )
}
