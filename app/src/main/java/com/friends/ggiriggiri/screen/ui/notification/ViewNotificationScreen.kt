package com.friends.ggiriggiri.screen.ui.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName

@Composable
fun ViewNotificationScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    ViewNotificationContent(viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewNotificationContent(
    viewModel: HomeViewModel
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "알림",
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    viewModel.friendsApplication.navHostController.apply {
                        popBackStack(MainScreenName.SCREEN_NOTIFICATION.name, true)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            repeat(10) {
                NotificationItem()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewViewNotificationContent() {
    ViewNotificationScreen()
}