package com.friends.ggiriggiri.screen.ui.home.memberlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.ProfileImage

@Composable
fun MemberListItem(
    item: Pair<String, String>
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.White)
    ){
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .weight(5f)
        ){
            ProfileImage(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                ContentScale.Fit,
                item.second,
            )
        }
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ){
            Text(
                item.first,
                fontFamily = FontFamily(Font(R.font.nanumsquarebold))
            )
        }
    }
}