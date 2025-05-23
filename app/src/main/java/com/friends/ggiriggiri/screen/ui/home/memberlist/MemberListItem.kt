package com.friends.ggiriggiri.screen.ui.home.memberlist

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.component.ProfileImage
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import java.nio.file.WatchEvent

@Composable
fun MemberListItem(
    item: String,
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.White)
    ){
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .weight(5f)
        ){
            ProfileImage(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                ContentScale.Fit,
                item,
            )
        }
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ){
            Text("채수범")
        }
    }
}