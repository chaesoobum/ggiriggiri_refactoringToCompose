package com.friends.ggiriggiri.screen.ui.memories.question

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
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.component.QuestionListScreenItem
import com.friends.ggiriggiri.component.QuestionListScreenItemSkeleton
import com.friends.ggiriggiri.screen.ui.memories.question.viewonequestion.NoQuestionScreen
import com.friends.ggiriggiri.util.MainScreenName

//MemoriesScreen 에서 리스트를 받아서 아이템을 채워준다
@Composable
fun QuestionListScreen(
    list: List<List<String>>,
    isRefreshing: Boolean,
    app: FriendsApplication
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
            if (list.isEmpty()) {
                Box(
                    modifier = Modifier
                        .height(400.dp)
                ) {
                    NoQuestionScreen()
                }

            } else {
                list.forEach {
                    //questionNumber: String = "#001",
                    //    questionTitle: String = "이 그룹에서 제로 콜라 안마실거 같은 사람은?",
                    //    questionDate: String = "2025.02.07"
                    QuestionListScreenItem(
                        "#001",
                        "이 그룹에서 제로 콜라 안마실거 같은 사람은?",
                        "2025.02.07",
                        onClick = {
                            app.navHostController.apply {
                                navigate(MainScreenName.SCREEN_VIEW_ONE_QUESTION.name)
                            }
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }

}