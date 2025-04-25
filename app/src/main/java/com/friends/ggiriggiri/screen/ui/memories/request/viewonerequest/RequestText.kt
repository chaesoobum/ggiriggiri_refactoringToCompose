package com.friends.ggiriggiri.screen.ui.memories.request.viewonerequest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.util.tools.rememberDefaultShimmer
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun RequestText(
    text:String,
    isLoading: Boolean
) {
    //요청 텍스트
    Spacer(modifier = Modifier.height(10.dp))


        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            // 요청 텍스트
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(20.dp)
                        .background(
                            Color.LightGray.copy(alpha = 0.7f),
                            RoundedCornerShape(4.dp)
                        )
                        .shimmer(rememberDefaultShimmer())
                        .padding(16.dp)
                )
            } else Text(
                text = text,
                fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                color = Color.Black
            )
        }

    //요청 텍스트
    Spacer(modifier = Modifier.height(10.dp))

}