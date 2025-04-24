package com.friends.ggiriggiri.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.friends.ggiriggiri.R

@Composable
fun QuestionListScreenItem(
    questionNumber: String = "#001",
    questionTitle: String = "이 그룹에서 제로 콜라 안마실거 같은 사람은?",
    questionDate: String = "2025.02.07",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                onClick()
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    Text(
                        text = questionNumber,
                        fontSize = 15.sp,
                        maxLines = 1,
                        fontFamily = FontFamily(Font(R.font.nanumsquarebold))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = questionTitle,
                        fontSize = 14.sp,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                        fontFamily = FontFamily(Font(R.font.nanumsquarebold))
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = questionDate,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.nanumsquareregular)),
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previews() {
    QuestionListScreenItem()
}