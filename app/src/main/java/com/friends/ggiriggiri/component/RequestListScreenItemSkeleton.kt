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
import com.valentinilk.shimmer.shimmer
import com.valentinilk.shimmer.rememberShimmer

@Composable
fun RequestListScreenItemSkeleton() {
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(16.dp)
                            .background(Color.Gray, RoundedCornerShape(4.dp))
                            .shimmer(shimmer)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(.2f)
                            .height(16.dp)
                            .background(Color.Gray, RoundedCornerShape(4.dp))
                            .shimmer(shimmer)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(16.dp)
                            .background(Color.Transparent, RoundedCornerShape(4.dp))
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Box(
                        modifier = Modifier
                            .weight(4f)
                            .height(16.dp)
                            .background(Color.Gray, RoundedCornerShape(4.dp))
                            .shimmer(shimmer)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(16.dp)
                            .background(Color.Transparent, RoundedCornerShape(4.dp))
                    )
                }
            }

        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(100.dp),
            contentAlignment = Alignment.BottomEnd
        ) {

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(Color.Gray, RoundedCornerShape(4.dp))
                        .shimmer(shimmer)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RequestListScreenItemSkeleton1(){
    RequestListScreenItemSkeleton()
}
