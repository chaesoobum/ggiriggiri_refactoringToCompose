package com.friends.ggiriggiri.screen.ui.memories.request.viewonerequest

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

@Composable
fun RequestItem(
    modifierItem: Modifier,
) {

    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        delay(2000)
        isLoading = false
    }

    Column(
        modifier = modifierItem
    ) {
        // 사용자 정보 헤더
        RequestHeader(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
            nickname = "채수범",
            date = "2025.03.04 14:57",
            isLoading = isLoading,
        )

        // 본문 이미지 (비율 유지 & shimmer)
        RequestImage(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
        )
        RequestText(
            text = "요청내용",
            isLoading = isLoading
        )

    }
}

@Preview
@Composable
fun PreviewRequestItem(){
    RequestItem(Modifier)
}

