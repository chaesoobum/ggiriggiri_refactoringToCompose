package com.friends.ggiriggiri.screen.ui.memories.request.viewonerequest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomProgressDialog
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.viewmodel.memories.MemoriesViewModel
import com.friends.ggiriggiri.util.MainScreenName
import kotlinx.coroutines.delay

@Composable
fun ViewOneRequestScreen(
    viewModel: MemoriesViewModel = hiltViewModel()
) {
    ViewOneRequestContent(viewModel)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewOneRequestContent(
    viewModel: MemoriesViewModel
) {

    var isLoading by remember { mutableStateOf(true) }

    // 이건 테스트용 - 실제론 데이터 로딩 완료 시점에 false로 바꿔야 함
    LaunchedEffect(Unit) {
        delay(2000L) // 2초 후 로딩 종료
        isLoading = false
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                //title = "설정",
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    viewModel.friendsApplication.navHostController.apply {
                        popBackStack(MainScreenName.SCREEN_VIEW_ONE_REQUEST.name,true)
                    }
                }
            )
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading == false){
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    RequestItem(
                        modifierItem = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(id = R.color.mainColor)),

                        )

                    repeat(3) {
                        RequestItem(
                            modifierItem = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                                .clip(RoundedCornerShape(15.dp))
                                .background(colorResource(id = R.color.white)),
                        )
                    }
                }
            }
            // 로딩 중이면 다이얼로그 보여주기
            CustomProgressDialog(isShowing = isLoading)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewViewOneRequestContent() {
    //ViewOneRequestContent()
}