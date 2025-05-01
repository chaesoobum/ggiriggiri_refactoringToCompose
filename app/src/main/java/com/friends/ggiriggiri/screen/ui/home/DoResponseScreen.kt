package com.friends.ggiriggiri.screen.ui.home

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.ProfileImage
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.viewmodel.PublicViewModel
import com.friends.ggiriggiri.screen.viewmodel.home.DoResponseViewModel
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.findActivity

@Composable
fun DoResponseScreen(
    viewmodel: DoResponseViewModel= hiltViewModel(),
    navHostController:NavHostController
) {
    val pvm: PublicViewModel = hiltViewModel(LocalContext.current.findActivity())
    LaunchedEffect(Unit) {
        viewmodel.getRequestImage(pvm.requestImageUrl.value.toString())
        pvm.deleteRequestImageUrl()
    }
    DoResponseContent(viewmodel,navHostController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoResponseContent(
    viewmodel: DoResponseViewModel,
    navHostController:NavHostController
){
    val context = LocalContext.current
    val focusManager: FocusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                // 외부 터치 시 포커스를 해제하고 키보드 내리기
                detectTapGestures(onTap = {
                    focusManager.clearFocus() // 포커스를 해제
                })
            },
        topBar = {
            TopAppBar(
                title = "응답하기",
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    navHostController.popBackStack(MainScreenName.SCREEN_DO_RESPONSE.name, true)
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            ProfileImage(
                modifier = Modifier.height(100.dp).width(200.dp),
                contentScale = ContentScale.Fit,
                imageUrl = viewmodel.requestImageUrl.value.toString()
            )

        }
    }
}