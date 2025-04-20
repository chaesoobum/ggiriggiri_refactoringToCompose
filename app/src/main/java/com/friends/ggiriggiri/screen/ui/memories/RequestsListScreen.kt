package com.friends.ggiriggiri.screen.ui.memories

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.component.RequestItem

@Composable
fun RequestsListScreen(modifier: Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(20) {

            RequestItem("채수범","내용입니다","2025.03.04 00:15")
        }
    }
}