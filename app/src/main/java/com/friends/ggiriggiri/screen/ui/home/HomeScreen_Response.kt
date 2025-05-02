package com.friends.ggiriggiri.screen.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName
import kotlinx.coroutines.delay

@Composable
fun UserMain_Response(
    viewModel: HomeViewModel = hiltViewModel()
) {

//    val remainingTime = viewModel.remainingTimeMillis.value
//
//    // 1 상태를 remember로 로컬 카운트다운 상태 만들기
//    var minutes by remember { mutableStateOf(remainingTime.getOrNull(0) ?: 0) }
//    var seconds by remember { mutableStateOf(remainingTime.getOrNull(1) ?: 0) }
//
//    // 2 1초마다 줄어드는 타이머
//    LaunchedEffect(key1 = remainingTime) {
//        while (minutes > 0 || seconds > 0) {
//            delay(1000)
//            if (seconds > 0) {
//                seconds--
//            } else if (minutes > 0) {
//                minutes--
//                seconds = 59
//            }
//        }
//    }
//
//    // 3. 타이머 출력 형식
//    val formattedTime = String.format("%02d:%02d", minutes, seconds)

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
                                            text = "응답하기",
                                            fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                                            fontSize = 18.sp
                                        )

                                        Spacer(modifier = Modifier.width(6.dp)) // 텍스트와 dot 사이 간격

                                        Image(
                                            painter = painterResource(id = R.drawable.ic_dot),
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

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
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
                text = "응답하기",
                buttonColor = colorResource(id = R.color.white),
                onClick = {

                }
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserMain_Response() {
    UserMain_Response()
}