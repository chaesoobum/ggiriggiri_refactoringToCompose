package com.friends.ggiriggiri.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage

@Composable
fun ImageDialog(selectedImageUrl:String?, callback: () -> Unit) {

    var url = selectedImageUrl

    if (url != null) {
        Dialog(
            onDismissRequest = { callback() },
            properties = androidx.compose.ui.window.DialogProperties(
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = { callback() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp)
                        .clickable(enabled = false) {},
                    contentScale = ContentScale.Fit
                )
            }
        }
    }

}