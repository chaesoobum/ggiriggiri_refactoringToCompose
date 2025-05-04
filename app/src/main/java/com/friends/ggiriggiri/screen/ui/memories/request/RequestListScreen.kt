package com.friends.ggiriggiri.screen.ui.memories.request

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.component.RequestListScreenItem
import com.friends.ggiriggiri.component.RequestListScreenItemSkeleton
import com.friends.ggiriggiri.screen.viewmodel.memories.MemoriesViewModel
import com.friends.ggiriggiri.util.MainScreenName

//MemoriesScreen 에서 리스트를 받아서 아이템을 채워준다
@Composable
fun RequestListScreen(
    viewModel: MemoriesViewModel,
    isRefreshing: Boolean,
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
            viewModel.listForRequestsListScreen.value.forEach {
                RequestListScreenItem(
                    it[0], it[1], it[2],
                    onClick = {
                        viewModel.friendsApplication.navHostController.apply {
                            navigate(MainScreenName.SCREEN_VIEW_ONE_REQUEST.name)
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}