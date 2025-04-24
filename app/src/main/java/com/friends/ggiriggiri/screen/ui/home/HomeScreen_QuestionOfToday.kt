package com.friends.ggiriggiri.screen.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.ApngImageFromUrl
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName

@Composable
fun UserMain_QuestionOfToday(
    viewModel: HomeViewModel= hiltViewModel()
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
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "오늘의 질문",
                                fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                                fontSize = 18.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(10.dp  )
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "친구의 애인의 도대체 애랑 왜 만나는지 궁금했던적이 있다",
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.size(100.dp),contentAlignment = Alignment.Center
                ) {
                    val imageUrl =
                        "https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Confounded%20Face.png"
                    ApngImageFromUrl(imageUrl,modifier = Modifier.fillMaxSize())
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
                text = "답변하기",
                buttonColor = colorResource(id = R.color.white) ,
                onClick = {
                    viewModel.friendsApplication.navHostController.apply {
                        navigate(MainScreenName.SCREEN_DO_ANSWER.name)
                    }
                }
            )

        }

    }
}



@Preview(showBackground = true)
@Composable
fun PreviewAnimatedEmojiImage() {
    UserMain_QuestionOfToday()
}