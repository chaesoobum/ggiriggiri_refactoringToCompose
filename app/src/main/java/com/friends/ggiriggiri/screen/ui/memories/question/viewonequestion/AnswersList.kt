package com.friends.ggiriggiri.screen.ui.memories.question.viewonequestion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnswersList(
    imageUrl: String,
    isLoading: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        if (isLoading) {
            repeat(5) {
                AnswersListItem(imageUrl, isLoading)
            }
        } else {
            repeat(20) {
                AnswersListItem(imageUrl, isLoading)
            }
        }
    }
}