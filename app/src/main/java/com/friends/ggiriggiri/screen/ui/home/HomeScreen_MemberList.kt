package com.friends.ggiriggiri.screen.ui.home

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
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.TextButton

@Composable
fun UserMain_MemberList(
    memberImageIds: List<Int> // 멤버 이미지 리소스 ID 리스트 (최대 4개)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp) //외부패딩
            .height(80.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(colorResource(id = R.color.mainColor))
            .padding(15.dp) //내부패딩
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxHeight()
                    .background(colorResource(id = R.color.mainColor)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val limitedMembers = memberImageIds.take(4)
                    limitedMembers.forEach { imageRes ->
                        Box(
                            modifier = Modifier
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = "프로필 이미지",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.dp, Color.Gray, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    // 남은 공간은 Spacer로 균형 맞춤
                    val emptySlots = 4 - limitedMembers.size
                    repeat(emptySlots) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(colorResource(id = R.color.mainColor)),
                contentAlignment = Alignment.Center
            ) {
                TextButton(text = "전체보기", onClick = {}, fontFamily = FontFamily(Font(R.font.nanumsquarebold)))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewUserMain_MemberList() {
    UserMain_MemberList(
        memberImageIds = listOf(
            R.drawable.ic_default_profile,
            R.drawable.ic_default_profile,
            R.drawable.ic_default_profile,
            R.drawable.ic_default_profile,
            R.drawable.ic_default_profile
        )
    )
}