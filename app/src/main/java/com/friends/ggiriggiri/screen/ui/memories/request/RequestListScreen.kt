package com.friends.ggiriggiri.screen.ui.memories.request

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.component.RequestListScreenItem
import com.friends.ggiriggiri.component.RequestListScreenItemSkeleton
import com.friends.ggiriggiri.screen.ui.memories.request.viewonerequest.NoRequestScreen
import com.friends.ggiriggiri.screen.viewmodel.PublicViewModel
import com.friends.ggiriggiri.screen.viewmodel.memories.MemoriesViewModel
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.findActivity

//MemoriesScreen 에서 리스트를 받아서 아이템을 채워준다
@RequiresApi(Build.VERSION_CODES.O)
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
            if (viewModel.listForRequestsListScreen.value.isEmpty()){
                Box(
                    modifier = Modifier
                        .height(400.dp)
                ) {
                    NoRequestScreen()
                }
            }else{
                viewModel.listForRequestsListScreen.value.forEach {
                    RequestListScreenItem(
                        it[0], it[1], it[2],
                        onClick = {
                            val requestDocumentId = it[3]
                            viewModel.friendsApplication.navHostController.apply {
                                navigate("${MainScreenName.SCREEN_VIEW_ONE_REQUEST.name}/$requestDocumentId")
                            }
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}