package com.friends.ggiriggiri.screen.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.QuestionListScreenItemSkeleton
import com.friends.ggiriggiri.util.tools.rememberDefaultShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun HomeScreenShimmer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp) //외부패딩
            .height(190.dp)
            .clip(RoundedCornerShape(15.dp))
            .shimmer(rememberDefaultShimmer())
    ){}
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreenShimmer(){
    HomeScreenShimmer()
}