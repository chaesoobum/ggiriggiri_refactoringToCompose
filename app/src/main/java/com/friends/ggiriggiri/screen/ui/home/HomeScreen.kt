package com.friends.ggiriggiri.screen.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomIconButton
import com.friends.ggiriggiri.component.ImageCarousel
import com.friends.ggiriggiri.component.ImageDialog
import com.friends.ggiriggiri.component.TopAppBarWithShimmer
import com.friends.ggiriggiri.messaging.PushEventBus
import com.friends.ggiriggiri.screen.ui.home.memberlist.UserMain_MemberList
import com.friends.ggiriggiri.screen.viewmodel.PublicViewModel
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.findActivity
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    modifier: Modifier,
    navBackStackEntry: State<NavBackStackEntry?>,
    viewModel: HomeViewModel = hiltViewModel()
) {
    //공용 뷰모델에 요청정보를 저장한다
    val pvm: PublicViewModel = hiltViewModel(LocalContext.current.findActivity())

    // 다른화면을 다녀올때 데이터업데아트
    val lifecycle = navBackStackEntry.value?.lifecycle
    val currentLifecycle by rememberUpdatedState(lifecycle)

    //서버에서 데이터가져오는 부분
    LaunchedEffect(Unit) {
        viewModel.apply {
            // 타이틀 로딩 시뮬레이션(2초)
            getAppBarTitle()

            // 딜레이 후 프로필 이미지 로딩 시뮬레이션
            getMemberProfileImage()

            //오늘의 질문 이미지 가져오기
            getQuestionImageUrl()

            //그룹이미지 랜덤 가져오기
            getImageCarousel()

            //그룹에 활성화된 요청이 있는지 가져오기
            getRequestStateInGroup()
        }
    }

    // 다른 Nav에서 돌아올 때(onResume) 다시 실행할 데이터 업데이트 함수
    DisposableEffect(currentLifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.getRequestStateInGroup() // 다시 로딩
            }
        }
        currentLifecycle?.addObserver(observer)
        onDispose {
            currentLifecycle?.removeObserver(observer)
        }
    }

    DisposableEffect(viewModel.requestState.value) {
        if (viewModel.requestState.value == true) {
            pvm.setRequestModel(viewModel.requestModel.value)
            pvm.setRequesterName(viewModel.requesterName.value)
        }
        onDispose { }
    }

    //푸시알림을 감지하고 데이터 리로드
    LaunchedEffect(Unit) {
        Log.d("push","LaunchedEffect")
        PushEventBus.refreshHomeEvent.collect {
            viewModel.clearHomeState()
            viewModel.getRequestStateInGroup()
        }
    }

    HomeContent(
        modifier = modifier,
        viewModel,
        pvm
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    modifier: Modifier,
    viewModel: HomeViewModel,
    pvm: PublicViewModel
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBarWithShimmer(
                title = viewModel.groupTitle.value,
                isLoadingTitle = viewModel.isTitleLoading.value,
                menuItems = {
                    CustomIconButton(
                        icon = ImageVector.vectorResource(R.drawable.notifications_24px),
                        iconButtonOnClick = {
                            viewModel.friendsApplication.navHostController.apply {
                                navigate(MainScreenName.SCREEN_NOTIFICATION.name)
                            }
                            viewModel.clearHomeState() // 상태 초기화
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
                UserMain_MemberList(viewModel, pvm)
                UserMain_QuestionOfToday(viewModel, pvm)

                if (viewModel.requestState.value == null) {
                    HomeScreenShimmer()
                } else if (viewModel.requestState.value == true) {
                    if (viewModel.isMyRequest.value == true) {
                        UserMain_Response(
                            title = "내 요청",
                            buttonText = "지금까지 온 응답보기",
                            button = {
                                //응답보는화면으로간다
                                viewModel.friendsApplication.navHostController.apply {
                                    navigate("${MainScreenName.SCREEN_VIEW_ONE_REQUEST.name}/${viewModel.requestModel.value?.requestDocumentId}")
                                }
                                viewModel.clearHomeState() // 상태 초기화
                            }
                        )
                    } else {
                        //이미응답함
                        if (viewModel.isResponse.value == true) {
                            UserMain_Response(
                                title = "응답한 요청",
                                buttonText = "지금까지 온 응답보기",
                                button = {
                                    //응답보는화면으로간다
                                    viewModel.friendsApplication.navHostController.apply {
                                        navigate("${MainScreenName.SCREEN_VIEW_ONE_REQUEST.name}/${viewModel.requestModel.value?.requestDocumentId}")
                                    }
                                    viewModel.clearHomeState() // 상태 초기화
                                }
                            )
                        } else {//응답안함
                            UserMain_Response(
                                title = "응답하기",
                                buttonText = "응답하기",
                                button = {
                                    viewModel.friendsApplication.navHostController.apply {
                                        navigate(MainScreenName.SCREEN_DO_RESPONSE.name)
                                    }
                                    viewModel.clearHomeState() // 상태 초기화
                                }
                            )
                        }
                    }
                } else if (viewModel.requestState.value == false) {
                    UserMain_ToAsk()
                }

                val selectedImageUrl = remember { mutableStateOf<String?>(null) }
                ImageCarousel(onImageClick = { url -> selectedImageUrl.value = url })
                if (selectedImageUrl.value != null) {
                    ImageDialog(
                        selectedImageUrl.value,
                        {
                            selectedImageUrl.value = null
                        }
                    )
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    //HomeScreen(modifier = Modifier)
}
