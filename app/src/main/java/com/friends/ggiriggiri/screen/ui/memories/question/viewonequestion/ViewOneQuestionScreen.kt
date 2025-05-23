package com.friends.ggiriggiri.screen.ui.memories.question.viewonequestion

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.viewmodel.memories.ViewOneQuestionViewModel


@Composable
fun ViewOneQuestionScreen(
    navHostController: NavHostController,
    questionNumber: String,
    viewModel: ViewOneQuestionViewModel = hiltViewModel(),
) {
    Log.d("ViewOneQuestionScreen", "questionNumber: ${questionNumber}")
    ViewOneQuestionContent(navHostController, questionNumber, viewModel)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewOneQuestionContent(
    navHostController: NavHostController,
    questionNumber: String,
    viewModel: ViewOneQuestionViewModel
) {
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        viewModel.getOneQuestionAndAnswers(questionNumber)
        isLoading = false
    }

    //CustomProgressDialog(isShowing = isLoading)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    navHostController.popBackStack()
                }
            )
        },
        containerColor = Color.White,
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                if(!isLoading){
                    QuestionImage(viewModel.questionListModel.value?.questionImg ?: "dummy")
                    QuestionText(
                        viewModel.questionListModel.value?.questionContent ?: null,
                    )
                    AnswersList(
                        viewModel,
                        isLoading
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewViewOneQuestionScreen() {
    //ViewOneQuestionScreen()
}