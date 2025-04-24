package com.friends.ggiriggiri.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.friends.ggiriggiri.util.tools.rememberDefaultShimmer
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ProfileImage(
    modifier: Modifier,
    contentScale:ContentScale,
    imageUrl:String,
) {
    // 프로필 이미지
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = "프로필 이미지",
        modifier = modifier,
        contentScale = contentScale
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmer(rememberDefaultShimmer())
                        .background(Color.Gray.copy(alpha = 0.6f))
                        //.background(Color.Gray.copy(alpha = 0.6f), CircleShape)
                )
            }
            is AsyncImagePainter.State.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.6f))
                        //.background(Color.Gray.copy(alpha = 0.3f), CircleShape)
                )
            }
            else -> SubcomposeAsyncImageContent()
        }
    }
}