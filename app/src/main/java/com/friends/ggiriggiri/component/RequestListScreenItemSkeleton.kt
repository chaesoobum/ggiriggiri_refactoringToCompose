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
import com.valentinilk.shimmer.shimmer
import com.valentinilk.shimmer.rememberShimmer

@Composable
fun RequestListScreenItemSkeleton() {
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
                            .background(Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                            .shimmer(rememberDefaultShimmer())
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(.2f)
                            .height(16.dp)
                            .background(Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                            .shimmer(rememberDefaultShimmer())
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
                            .background(Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                            .shimmer(rememberDefaultShimmer())
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
                        .background(Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .shimmer(rememberDefaultShimmer())
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
