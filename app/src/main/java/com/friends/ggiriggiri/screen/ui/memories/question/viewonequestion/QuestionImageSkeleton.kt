package com.friends.ggiriggiri.screen.ui.memories.question.viewonequestion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.friends.ggiriggiri.component.ApngImageFromUrl
import com.friends.ggiriggiri.util.rememberDefaultShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun QuestionImageSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {

        }
        Box(
            modifier = Modifier
                .weight(4f)
                .clip(CircleShape)
                .shimmer(rememberDefaultShimmer())
        ) {}
        Box(
            modifier = Modifier
                .weight(1f)
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewQuestionImageSkeleton() {
    QuestionImageSkeleton()
}