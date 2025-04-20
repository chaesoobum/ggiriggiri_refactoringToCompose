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
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun QuestionListScreenItemSkeleton() {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Question Number
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(16.dp)
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
                    .shimmer(shimmer)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Question Title
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.8f)
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
                    .shimmer(shimmer)
            )
        }

        // Date
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 10.dp, bottom = 10.dp)
                .height(14.dp)
                .align(Alignment.End)
                .background(Color.LightGray, RoundedCornerShape(4.dp))
                .shimmer(shimmer)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun QuestionListScreenItemSkeleton1(){
    QuestionListScreenItemSkeleton()
}