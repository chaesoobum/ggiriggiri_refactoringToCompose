package com.friends.ggiriggiri.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.tools.rememberDefaultShimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay

@Composable
fun ImageCarousel(
    viewModel: HomeViewModel = hiltViewModel(),
    onImageClick: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val currentIndex by remember {
        derivedStateOf {
            val firstVisibleItem = listState.firstVisibleItemIndex
            val offset = listState.firstVisibleItemScrollOffset
            if (offset > 125) firstVisibleItem + 1 else firstVisibleItem
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(viewModel.groupImageUrls.value) { item ->
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
                        when{
                            painter.state is AsyncImagePainter.State.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp)
                                ){
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .shimmer(rememberDefaultShimmer())
                                            .background(
                                                Color.LightGray.copy(alpha = 0.7f),
                                                RoundedCornerShape(4.dp)
                                            )
                                    )
                                }
                            }

                            painter.state is AsyncImagePainter.State.Error -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 20.dp, end = 20.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Color.LightGray.copy(alpha = 0.7f),
                                            RoundedCornerShape(4.dp)
                                        ),
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
                                        .padding(start = 20.dp, end = 20.dp)
                                        .clickable { onImageClick(item) },
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                }
            }
        }

        // 점 인디케이터 추가
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            viewModel.groupImageUrls.value.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (currentIndex == index) 10.dp else 8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (currentIndex == index) colorResource(id = R.color.mainColor)
                            else Color.Gray.copy(alpha = 0.4f)
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselSample() {
    val items = listOf(
        "https://picsum.photos/id/1015/300/200",
        "https://picsum.photos/id/1016/400/600",
        "https://picsum.photos/id/1018/1200/400",
        "https://picsum.photos/id/1019/800/800",
        "https://picsum.photos/id/1019/800/800"
    )

    //ImageCarousel()
}
