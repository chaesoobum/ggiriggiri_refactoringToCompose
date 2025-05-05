package com.friends.ggiriggiri.screen.ui.memories.request.viewonerequest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.friends.ggiriggiri.R

@Composable
fun NoRequestScreen() {
    Row (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            "홈 에서 그룹원들에게 요청을 날려보세요!",
            fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
            fontSize = 15.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoRequestScreen(){
    NoRequestScreen()
}