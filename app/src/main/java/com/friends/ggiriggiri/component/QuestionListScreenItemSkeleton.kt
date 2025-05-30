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
import com.friends.ggiriggiri.util.tools.rememberDefaultShimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun QuestionListScreenItemSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
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
                            .shimmer(rememberDefaultShimmer())
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .weight(4f)
                            .padding(end = 100.dp)
                            .shimmer(rememberDefaultShimmer())
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun QuestionListScreenItemSkeleton1(){
    QuestionListScreenItemSkeleton()
}