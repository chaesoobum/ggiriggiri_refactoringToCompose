package com.friends.ggiriggiri.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
    questionDate: String = "2025.02.07"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

        Text(
            text = questionDate,
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.nanumsquareregular)),
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 10.dp, bottom = 10.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun previews(){
    QuestionListScreenItem()
}