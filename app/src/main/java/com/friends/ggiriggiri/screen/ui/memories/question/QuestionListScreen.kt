package com.friends.ggiriggiri.screen.ui.memories.question

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.component.QuestionListScreenItem
import com.friends.ggiriggiri.component.QuestionListScreenItemSkeleton
import com.friends.ggiriggiri.screen.ui.memories.question.viewonequestion.NoQuestionScreen
import com.friends.ggiriggiri.screen.viewmodel.memories.MemoriesViewModel
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.tools.formatMillisToDateTime

//MemoriesScreen 에서 리스트를 받아서 아이템을 채워준다
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuestionListScreen(
    viewModel: MemoriesViewModel,
    isRefreshing: Boolean,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (isRefreshing) {
            repeat(10) {
                QuestionListScreenItemSkeleton()
            }
        } else {
            if (viewModel.listForQuestionsListScreen.value.isEmpty()) {
                Box(
                    modifier = Modifier
                        .height(400.dp)
                ) {
                    NoQuestionScreen()
                }

            } else {
                Log.d("viewModel.listForQuestionsListScreen.value",viewModel.listForQuestionsListScreen.value.size.toString())
                viewModel.listForQuestionsListScreen.value.forEach {
                    QuestionListScreenItem(
                        questionNumber = it[0].toString(),
                        questionTitle = it[1],
                        onClick = {
                            val questionNumber = it[0].toString()
                            navHostController.navigate("${MainScreenName.SCREEN_VIEW_ONE_QUESTION.name}/$questionNumber")
                        }
                    )
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
    Spacer(modifier = Modifier.height(100.dp))
}

