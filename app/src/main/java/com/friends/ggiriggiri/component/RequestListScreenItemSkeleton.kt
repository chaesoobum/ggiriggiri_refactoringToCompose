package com.friends.ggiriggiri.component

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
import com.valentinilk.shimmer.shimmer
import com.valentinilk.shimmer.rememberShimmer

@Composable
fun RequestListScreenItemSkeleton() {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer(shimmer)
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(80.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .fillMaxWidth(0.7f)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .fillMaxWidth(0.5f)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .shimmer(shimmer),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(60.dp)
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
                    .padding(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RequestListScreenItemSkeleton1(){
    RequestListScreenItemSkeleton()
}
