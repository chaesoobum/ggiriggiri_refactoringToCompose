package com.friends.ggiriggiri.screen.ui.home

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.OutlinedTextField
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.ui.memories.question.viewonequestion.QuestionImage
import com.friends.ggiriggiri.screen.viewmodel.PublicViewModel
import com.friends.ggiriggiri.screen.viewmodel.home.DoAnswerViewModel
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.findActivity
import kotlinx.coroutines.delay

@Composable
fun DoAnswerScreen(
    doAnswerViewModel: DoAnswerViewModel = hiltViewModel()
) {
    DoAnswerContent(doAnswerViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoAnswerContent(
    doAnswerViewModel: DoAnswerViewModel,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    //서버의 지연시간을 테스트하기위한 변수와 딜레이
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(2000)
        isLoading = false
    }
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
                title = "답변하기",
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    homeViewModel.friendsApplication.navHostController.apply {
                        popBackStack(MainScreenName.SCREEN_DO_ANSWER.name,true)
                    }
                },
                isDivider = false
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            val pvm: PublicViewModel = hiltViewModel(LocalContext.current.findActivity())
            doAnswerViewModel.setQuestionImageUrl(pvm.questionImageUrl.value)
            pvm.deleteQuestionImageUrl()
            QuestionImage(doAnswerViewModel.questionImageUrl.value)

            Spacer(modifier = Modifier.height(10.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ){
                Text(
                    text = "친구의 애인의 도대체 애랑 왜 만나는지 궁금했던적이 있다",
                    fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
            OutlinedTextField(
                paddingTop = 20.dp,
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                label = "답변하기",
                singleLine = true,

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDoAnswerContent() {
    //DoAnswerContent()
}