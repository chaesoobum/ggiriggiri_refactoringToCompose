package com.friends.ggiriggiri.screen.ui.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.TextButton
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer


@Composable
fun UserMain_MemberList(
    memberImageUrls: List<String>,
    onSeeAllClick: () -> Unit = {}
) {
    val shimmerInstance = rememberShimmer(
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

    val isLoading = memberImageUrls.isEmpty()

    //멤버가 몇명인지 가져오는데 오래걸린다 일단 4명으로 표기
    val displayList = if (isLoading) List(4) { "" } else memberImageUrls.take(4)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(colorResource(id = R.color.mainColor))
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    displayList.forEach { imageUrl ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray.copy(alpha = 0.3f))
                                        .shimmer(shimmerInstance)
                                )
                            } else {
                                SubcomposeAsyncImage(
                                    model = imageUrl,
                                    contentDescription = "프로필 이미지",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .border(1.dp, Color.Gray, CircleShape)
                                ) {
                                    when {
                                        painter.state is AsyncImagePainter.State.Loading -> {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .shimmer(shimmerInstance)
                                                    .background(Color.Gray, CircleShape)
                                            )
                                        }

                                        painter.state is AsyncImagePainter.State.Error -> {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(Color.Gray, CircleShape)
                                            )
                                        }

                                        else -> {
                                            Image(
                                                painter = painter,
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    text = "전체보기",
                    onClick = onSeeAllClick,
                    fontFamily = FontFamily(Font(R.font.nanumsquarebold))
                )
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewUserMain_MemberList() {
    UserMain_MemberList(
        memberImageUrls = listOf(
            "https://picsum.photos/id/1015/300/300",
            "https://picsum.photos/id/1016/300/300",
            "https://picsum.photos/id/1018/300/300",
            "https://picsum.photos/id/1019/300/300",
            "https://picsum.photos/id/1020/300/300"
        )
    )
}