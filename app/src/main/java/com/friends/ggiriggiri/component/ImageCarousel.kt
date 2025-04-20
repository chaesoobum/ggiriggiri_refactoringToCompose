package com.friends.ggiriggiri.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.friends.ggiriggiri.R
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ImageCarousel(
    items: List<String>,
    onImageClick: (String) -> Unit) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp, top = 20.dp)
    ) {
        LazyRow(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier
                .fillMaxWidth(),
            //horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(items) { item ->
                Box(
                    modifier = Modifier
                        .width(250.dp)
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = item,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit,
                    ) {
                        when (painter.state) {
                            is AsyncImagePainter.State.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .shimmer(shimmerInstance)
                                        .padding(start = 20.dp,end=20.dp)
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color.Gray.copy(alpha = 0.6f),
                                                    Color.LightGray.copy(alpha = 0.3f)
                                                )
                                            )
                                        )
                                )
                            }

                            is AsyncImagePainter.State.Error -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 20.dp,end=20.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Gray.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.material3.Text("이미지 로딩 에러", color = Color.White)
                                }
                            }

                            else -> {
                                Image(
                                    painter = painter,
                                    contentDescription = contentDescription,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 20.dp,end=20.dp)
                                        .clickable { onImageClick(item) },
                                    contentScale = ContentScale.Fit
                                )
                            }

                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CarouselSample() {
    val items = listOf(
        "https://picsum.photos/id/1015/300/200",
        "https://picsum.photos/id/1016/400/600",
        "https://picsum.photos/id/1018/1200/400",
        "https://picsum.photos/id/1019/800/800"
    )

    ImageCarousel(items = items,onImageClick = {})
}
