package com.friends.ggiriggiri.screen.ui.memories.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.QuestionListScreenItem
import com.friends.ggiriggiri.component.RequestListScreenItem
import com.friends.ggiriggiri.firebase.model.QuestionInfo
import com.friends.ggiriggiri.firebase.model.RequestInfo
import com.friends.ggiriggiri.screen.viewmodel.memories.MemoriesViewModel
import com.friends.ggiriggiri.util.MainScreenName
import java.nio.file.WatchEvent

@Composable
fun QuestionListScreen(
    viewModel: MemoriesViewModel,
    navHostController: NavHostController
) {
    val questionList = viewModel.questionList
    val isLoading = viewModel.isLoadingQuestions
    val isEndReachedQuestions = viewModel.isEndReachedQuestions
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        itemsIndexed(questionList) { index, item ->
            QuestionListScreenItem(
                questionNumber = item.questionNumber,
                questionTitle = item.questionContent,
                onClick = {
                    navHostController.navigate("${MainScreenName.SCREEN_VIEW_ONE_QUESTION}/${item.questionNumber}")
                }
            )

            if (index >= questionList.lastIndex - 2 && !isLoading && !isEndReachedQuestions) {
                viewModel.loadNextQuestionPage()
            }
        }

        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = colorResource(id = R.color.mainColor)
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(100.dp)) // 하단 여유 공간
        }
    }
}