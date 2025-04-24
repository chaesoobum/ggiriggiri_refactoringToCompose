package com.friends.ggiriggiri.screen.ui.memories.viewonequestion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.component.ApngImageFromUrl

@Composable
fun QuestionImage(
    imageUrl: String,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ){
        Box (
            modifier = Modifier
                .weight(1f)){

        }
        Box (
            modifier = Modifier
                .weight(4f)){
            ApngImageFromUrl(
                imageUrl = "https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Confounded%20Face.png"
            )
        }
        Box (
            modifier = Modifier
                .weight(1f)){

        }
    }

}