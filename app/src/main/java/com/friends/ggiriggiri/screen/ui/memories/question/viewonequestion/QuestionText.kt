package com.friends.ggiriggiri.screen.ui.memories.question.viewonequestion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.util.tools.rememberDefaultShimmer
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun QuestionText(
    text:String,
    isLoading: Boolean
) {
    Spacer(modifier = Modifier.height(10.dp))
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.Center
    ){
        if (isLoading) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(20.sp.value.dp)
                    .shimmer(rememberDefaultShimmer())
                    .padding(16.dp)
            )
        } else Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
            fontSize = 20.sp,
            color = Color.Black
        )

    }


}