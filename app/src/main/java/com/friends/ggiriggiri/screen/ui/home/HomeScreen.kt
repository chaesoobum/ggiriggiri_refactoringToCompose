package com.friends.ggiriggiri.screen.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomIconButton
import com.friends.ggiriggiri.component.ImageCarousel
import com.friends.ggiriggiri.component.TopAppBarWithShimmer
import com.friends.ggiriggiri.screen.ui.home.memberlist.UserMain_MemberList
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // 서버에서 데이터가져오는 부분
    LaunchedEffect(Unit) {
        // 타이틀 로딩 시뮬레이션(2초)
        viewModel.getAppBarTitle()

        // 딜레이 후 이미지 로딩 시뮬레이션
        viewModel.getMemberProfileImage()

        //그룹이미지 랜덤 가져오기
        viewModel.getImageCarousel()

        //오늘의 질문 이미지 가져오기
        viewModel.getQuestionImageUrl()
    }

    HomeContent(
        modifier = modifier,
        viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    modifier: Modifier,
    viewModel: HomeViewModel
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBarWithShimmer(
                title = "그룹명",
                isLoadingTitle = viewModel.isTitleLoading.value,
                menuItems = {
                    CustomIconButton(
                        icon = ImageVector.vectorResource(R.drawable.notifications_24px),
                        iconButtonOnClick = {
                            viewModel.friendsApplication.navHostController.apply {
                                navigate(MainScreenName.SCREEN_NOTIFICATION.name)
                            }
                        }
                    )
                },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                UserMain_MemberList(memberImageUrls = viewModel.memberImageUrls.value)
                UserMain_QuestionOfToday(questionImageUrl = viewModel.questionImageUrl.value)
                UserMain_ToAsk()

                val selectedImageUrl = remember { mutableStateOf<String?>(null) }
                ImageCarousel(items = viewModel.groupImageUrls.value , onImageClick = { url -> selectedImageUrl.value = url })
                if (selectedImageUrl.value != null) {
                    Dialog(
                        onDismissRequest = { selectedImageUrl.value = null },
                        properties = androidx.compose.ui.window.DialogProperties(
                            dismissOnClickOutside = true,
                            usePlatformDefaultWidth = false
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    onClick = { selectedImageUrl.value = null }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            SubcomposeAsyncImage(
                                model = selectedImageUrl.value,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(10.dp)
                                    .clickable(enabled = false) {},
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(modifier = Modifier)
}
