package com.friends.ggiriggiri.screen.ui.memories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.component.QuestionListScreenItem
import com.friends.ggiriggiri.component.QuestionListScreenItemSkeleton
import com.friends.ggiriggiri.component.RequestListScreenItem
import com.friends.ggiriggiri.component.RequestListScreenItemSkeleton

//MemoriesScreen 에서 리스트를 받아서 아이템을 채워준다
@Composable
fun QuestionListScreen(
    list:List<List<String>>,
    isRefreshing: Boolean
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
            list.forEach {
                //questionNumber: String = "#001",
                //    questionTitle: String = "이 그룹에서 제로 콜라 안마실거 같은 사람은?",
                //    questionDate: String = "2025.02.07"
                QuestionListScreenItem("#001", "이 그룹에서 제로 콜라 안마실거 같은 사람은?", "2025.02.07")
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }

}