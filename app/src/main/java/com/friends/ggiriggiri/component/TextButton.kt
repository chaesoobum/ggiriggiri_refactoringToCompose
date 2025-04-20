package com.friends.ggiriggiri.component

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.friends.ggiriggiri.R

@Composable
fun TextButton(
    text: String,
    underline: Boolean = false,
    fontSize: TextUnit = 13.sp,
    fontFamily: FontFamily = FontFamily(Font(R.font.nanumsquareregular)),
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            color = Color.Black,
            fontFamily = fontFamily,
            textDecoration = if (underline) TextDecoration.Underline else TextDecoration.None,
        )
    }
}
