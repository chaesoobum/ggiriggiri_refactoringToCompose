package com.friends.ggiriggiri.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun QuestionListScreenItemSkeleton() {
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = ShimmerTheme(
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 500,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            blendMode = androidx.compose.ui.graphics.BlendMode.SrcOver,
            rotation = 0f, // 또는 20f로 기울기 효과
            shaderColors = listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.3f),
                Color.LightGray.copy(alpha = 0.6f)
            ),
            shaderColorStops = null, // 자동 분포
            shimmerWidth = 200.dp // shimmer wave 넓이
        )
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
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
                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .weight(1f)
                            .background(Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                            .shimmer(shimmer)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .weight(4f)
                            .padding(end = 100.dp)
                            .background(Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                            .shimmer(shimmer)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row{
                    Spacer(modifier = Modifier.weight(1f)) // 왼쪽 공간 밀기
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(16.dp)
                            .background(Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                            .shimmer(shimmer)
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun QuestionListScreenItemSkeleton1(){
    QuestionListScreenItemSkeleton()
}