package com.friends.ggiriggiri.screen.ui.memories.question.viewonequestion

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.ProfileImage
import com.friends.ggiriggiri.screen.viewmodel.memories.ViewOneQuestionViewModel
import com.friends.ggiriggiri.util.tools.rememberDefaultShimmer
import com.kakao.sdk.user.model.Profile
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun AnswersListItem(
    name:String,
    answerContent:String,
    profile: String,
    time:String,
    isLoading: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(6f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(20.sp.value.dp) // 텍스트 크기 기준 높이
                                .shimmer(rememberDefaultShimmer())

                        )
                    } else {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = name,
                            textAlign = TextAlign.End,
                            fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                            fontSize = 20.sp,
                        )
                    }

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(15.sp.value.dp) // 텍스트 크기 기준 높이
                                .shimmer(rememberDefaultShimmer())
                        )
                    } else {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = answerContent,
                            textAlign = TextAlign.End,
                            fontFamily = FontFamily(Font(R.font.nanumsquareregular)),
                            fontSize = 15.sp,
                        )
                    }

                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            ProfileImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color.Gray, CircleShape),
                ContentScale.Crop,
                profile,
            )
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewAnswersList() {
    //AnswersListItem("123",true)
}