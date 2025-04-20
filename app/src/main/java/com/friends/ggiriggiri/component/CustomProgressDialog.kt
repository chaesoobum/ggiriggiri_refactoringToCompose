package com.friends.ggiriggiri.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.friends.ggiriggiri.R

@Composable
fun CustomProgressDialog(
    isShowing: Boolean,
    onDismissRequest: () -> Unit = {}
) {
    if (isShowing) {
        Dialog(onDismissRequest = onDismissRequest) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp),
                    color = colorResource(id = R.color.mainColor),
                    strokeWidth = 6.dp
                )
            }
        }
    }
}
