package com.friends.ggiriggiri.screen.ui.memories.question.viewonequestion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.screen.viewmodel.memories.ViewOneQuestionViewModel

@Composable
fun AnswersList(
    viewModel: ViewOneQuestionViewModel,
    isLoading: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        if (isLoading) {
            repeat(5) {


            }
        } else {
            viewModel.answerDisplayModelList.value.forEach{
                AnswersListItem(
                    name = it!!.userName,
                    answerContent = it!!.answerContent,
                    profile = it!!.userProfileImage,
                    time = it!!.answerResponseTime.toString(),
                    isLoading
                )
            }
        }
    }
}