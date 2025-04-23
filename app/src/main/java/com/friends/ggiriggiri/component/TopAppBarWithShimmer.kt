package com.friends.ggiriggiri.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.R
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithShimmer(
    title: String = "",
    isLoadingTitle: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIconImage: ImageVector? = null,
    navigationIconOnClick: () -> Unit = {},
    menuItems: @Composable RowScope.() -> Unit = {},
    isDivider: Boolean? = true
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

    Column {
        CenterAlignedTopAppBar(
            title = {
                if (isLoadingTitle) {
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(120.dp)
                            .shimmer(shimmerInstance)
                            .background(
                                Color.Gray.copy(alpha = 0.7f),
                                RoundedCornerShape(4.dp)
                            )
                    )
                } else {
                    Text(
                        text = title,
                        fontFamily = FontFamily(Font(R.font.nanumsquarebold))
                    )
                }
            },
            navigationIcon = if (navigationIconImage == null) {
                {}
            } else {
                {
                    IconButton(onClick = navigationIconOnClick) {
                        Icon(
                            imageVector = navigationIconImage,
                            contentDescription = null
                        )
                    }
                }
            },
            actions = {
                menuItems()
            },
            scrollBehavior = scrollBehavior
        )

        if (isDivider == true) {
            Divider(
                color = Color.Gray,
                thickness = 1.dp
            )
        }
    }
}
