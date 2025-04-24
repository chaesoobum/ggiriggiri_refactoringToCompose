package com.friends.ggiriggiri.screen.ui.memories.viewonequestion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer

@Composable
fun AnswersList(
    imageUrl: String,
    shimmerInstance: Shimmer,
    isLoading: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        if (isLoading) {
            repeat(5) {
                AnswersListItem(imageUrl, isLoading, shimmerInstance)
            }
        } else {
            repeat(20) {
                AnswersListItem(imageUrl, isLoading, shimmerInstance)
            }
        }
    }
}