package com.friends.ggiriggiri.screen.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.component.CustomProgressDialog
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName

@Composable
fun UserMain_ToAsk(
    viewModel: HomeViewModel = hiltViewModel(),
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
                                            text = "요청",
                                            fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                                            fontSize = 18.sp
                                        )

                                        Spacer(modifier = Modifier.width(6.dp)) // 텍스트와 dot 사이 간격

                                        Image(
                                            painter = painterResource(id = R.drawable.ic_dot),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(10.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "요청시 30분간\n응답을 받을 수 있습니다",
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                                        fontSize = 14.sp
                                    )
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
                text = "요청하기",
                buttonColor = colorResource(id = R.color.white),
                onClick = {
                    viewModel.friendsApplication.navHostController.apply {
                        navigate(MainScreenName.SCREEN_DO_REQUEST.name)
                    }
                    viewModel.clearHomeState() // 상태 초기화
                    viewModel.clearAnswerState()
                }
            )

        }

    }
}



@Preview(showBackground = true)
@Composable
fun Preview() {
    UserMain_ToAsk()
}