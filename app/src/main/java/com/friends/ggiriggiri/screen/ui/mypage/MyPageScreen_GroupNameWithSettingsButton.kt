package com.friends.ggiriggiri.screen.ui.mypage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.friends.ggiriggiri.R

@Composable
fun GroupNameWithSettingsButton(
    groupName: String = "그룹명",
    onGroupNameClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = groupName,
            fontSize = 20.sp,
            color = Color.Black,
            textDecoration = TextDecoration.Underline
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.settings_24px),
            contentDescription = "설정",
            tint = Color.Black,
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable { onGroupNameClick() }
        )
    }
}
