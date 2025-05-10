package com.friends.ggiriggiri.screen.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.component.ProfileImage
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.tools.rememberDefaultShimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay

@Composable
fun UserMain_Response(
    viewModel: HomeViewModel = hiltViewModel(),
    title: String,
    buttonText: String,
    button: () -> Unit
) {
    UserMain_ResponseContent(viewModel,title,buttonText,button)
}

@Composable
fun UserMain_ResponseContent(
    viewModel: HomeViewModel,
    title: String,
    buttonText: String,
    button: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp) //외부패딩
            .height(190.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(colorResource(id = R.color.mainColor))
            .padding(15.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center // 전체 Row를 중앙에 정렬
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically, // 텍스트와 이미지 수직 정렬
                                    ) {
                                        Text(
                                            text = title,
                                            fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                                            fontSize = 18.sp
                                        )

                                        Spacer(modifier = Modifier.width(6.dp)) // 텍스트와 dot 사이 간격

                                        Image(
                                            painter = painterResource(id = R.drawable.ic_dot_green),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )

                                        Spacer(modifier = Modifier.width(6.dp))

                                        Text(
                                            text = viewModel.remainingTimeFormatted.value,
                                            fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                                            fontSize = 15.sp
                                        )

                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(3f)
                                            .padding(10.dp)
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = viewModel.requesterName.value,
                                                textAlign = TextAlign.Center,
                                                fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                                                fontSize = 14.sp
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "의 요청",
                                                textAlign = TextAlign.Center,
                                                fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                                                fontSize = 12.sp
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .padding(top = 5.dp)
                                        ) {
                                            Text(
                                                text = viewModel.requestMessage.value,
                                                textAlign = TextAlign.Center,
                                                fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                                                fontSize = 12.sp
                                            )
                                        }
                                    }

                                    Box (
                                        modifier = Modifier
                                            .weight(1f)
                                    ){
                                        SubcomposeAsyncImage(
                                            model = viewModel.requestImageUrl.value,
                                            contentDescription = "요청 이미지",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            when (painter.state) {
                                                is AsyncImagePainter.State.Loading -> {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .shimmer(rememberDefaultShimmer())
                                                    )
                                                }

                                                is AsyncImagePainter.State.Error -> {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .background(Color.Transparent)
                                                    )
                                                }

                                                else -> {
                                                    SubcomposeAsyncImageContent()
                                                }
                                            }
                                        }
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
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            CustomButton(
                text = buttonText,
                buttonColor = colorResource(id = R.color.white),
                onClick = {
                    button()
                }
            )

        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewUserMain_Response() {
    //UserMain_ResponseContent()
}