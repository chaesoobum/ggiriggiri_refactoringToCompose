package com.friends.ggiriggiri.screen.ui.memories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.component.RequestListScreenItem
import com.friends.ggiriggiri.component.RequestListScreenItemSkeleton

//MemoriesScreen 에서 리스트를 받아서 아이템을 채워준다
@Composable
fun RequestListScreen(
    list: List<List<String>>,
    isRefreshing: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (isRefreshing) {
            repeat(10) {
                RequestListScreenItemSkeleton()
            }
        } else {
            list.forEach {
                RequestListScreenItem(it[0], it[1], it[2])
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}