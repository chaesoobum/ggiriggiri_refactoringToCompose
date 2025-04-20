package com.friends.ggiriggiri.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.friends.ggiriggiri.R

// date는 포맷팅 된상태로 들어올것
@Composable
fun RequestListScreenItem(
    name: String,
    content:String,
    date:String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1.5f)
                .height(100.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                        fontSize = 16.sp
                    )
                    Text(
                        text = "님",
                        fontFamily = FontFamily(Font(R.font.nanumsquareregular)),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = content,
                    fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                    fontSize = 14.sp,
                    maxLines = 2,
                )
            }

        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(100.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = date,
                modifier = Modifier
                    .padding(20.dp),
                fontFamily = FontFamily(Font(R.font.nanumsquareregular)),
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    RequestListScreenItem("채수범","내용입니다","2025.03.04 00:15")
}