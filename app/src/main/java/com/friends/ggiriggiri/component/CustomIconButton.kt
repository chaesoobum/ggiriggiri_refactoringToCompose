package com.friends.ggiriggiri.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomIconButton(
    icon:ImageVector,
    iconButtonOnClick : () -> Unit = {},
) {
    IconButton(
        onClick = {
            iconButtonOnClick()
        }
    ){
        Icon(
            imageVector = icon,
            tint = Color.Black,
            contentDescription = null
        )
    }
}