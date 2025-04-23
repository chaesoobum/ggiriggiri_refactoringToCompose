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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomIconButton
import com.friends.ggiriggiri.component.ImageCarousel
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.component.TopAppBarWithShimmer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import okhttp3.Dispatcher

val dummyMemberImageIds = listOf(
    R.drawable.ic_default_profile,
    R.drawable.ic_default_profile,
    R.drawable.ic_default_profile,
    R.drawable.ic_default_profile,
    R.drawable.ic_default_profile
)

@Composable
fun HomeScreen(modifier: Modifier) {
    val memberImageUrls = remember { mutableStateOf<List<String>>(emptyList()) }

    // 딜레이 후 이미지 로딩 시뮬레이션
    LaunchedEffect(Unit) {
        delay(2000L) // 2초 후에 데이터 세팅
        memberImageUrls.value = listOf(
            "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
            "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
            "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26"
        )
    }

    HomeContent(
        modifier = modifier,
        memberImageUrls = memberImageUrls.value
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    modifier: Modifier,
    memberImageUrls: List<String>
) {
    val loading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(2000)
        loading.value = false
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarWithShimmer(
                title = "그룹명",
                isLoadingTitle = loading.value,
                menuItems = {
                    CustomIconButton(
                        icon = ImageVector.vectorResource(R.drawable.notifications_24px),
                        iconButtonOnClick = {
                            // 클릭 시 동작
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
//                UserMain_MemberList(
//                    memberImageUrls = listOf(
//                        "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
//                        "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
//                        "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
//                    )
//                )
                UserMain_MemberList(memberImageUrls = memberImageUrls)
                UserMain_QuestionOfToday()
                UserMain_ToAsk()

                val selectedImageUrl = remember { mutableStateOf<String?>(null) }
                val items = listOf(
                    "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
                    "https://picsum.photos/id/1016/400/600",
                    "https://not-a-real-url/image.jpg", // 실패 테스트
                    "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
                )
                ImageCarousel(items = items, onImageClick = { url -> selectedImageUrl.value = url })
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
