package com.friends.ggiriggiri.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.R

@Composable
fun CustomButton(
    text: String = "Button",
    paddingTop: Dp = 0.dp,
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp,
    paddingBottom:Dp = 0.dp,
    onClick: () -> Unit = {},
    buttonColor: Color = Color(0xFFFDF497), // 기본 버튼 색
    textColor: Color = Color.Black,         // 기본 텍스트 색
    fontFamily: FontFamily = FontFamily(
        Font(R.font.nanumsquarebold)
    ),          // 폰트 없으면 nanumsquarebold 폰트 사용
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop, start = paddingStart, end = paddingEnd, bottom = paddingBottom),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(5.dp),
    ) {
        Text(
            text = text,
            color = textColor,
            fontFamily = fontFamily
        )
    }
}

@Preview
@Composable
fun test() {
    CustomButton()
}
