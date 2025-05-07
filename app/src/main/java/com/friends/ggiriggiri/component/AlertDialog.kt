package com.friends.ggiriggiri.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CustomAlertDialog(
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit,
    onPositiveText: String = "확인",
    onDismissRequest: (() -> Unit)? = null,
    onNegativeText: String? = "취소",
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    image: Uri? = null
) {
    AlertDialog(
        containerColor = Color.White,
        icon = {
            Icon(icon, contentDescription = null, tint = Color.Black)
        },
        title = {
            Text(
                text = dialogTitle,
                color = Color.Black
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = dialogText,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                image?.let {
                    Spacer(modifier = Modifier.height(height = 12.dp))
                    Image(
                        painter = rememberAsyncImagePainter(model = it),
                        contentDescription = "선택된 이미지 미리보기",
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(text = onPositiveText, color = Color.Black)
            }
        },
        dismissButton = onDismissRequest?.let {
            {
                if (onNegativeText != null) {
                    TextButton(onClick = it) {
                        Text(text = onNegativeText, color = Color.Black)
                    }
                }
            }
        }
    )
}



@Preview
@Composable
fun AlertDialogExamplePreview() {
    CustomAlertDialog(
        onDismiss = {},
        onConfirmation = {},
        onDismissRequest = {},
        dialogTitle = "그룹 만들기",
        dialogText = "그룹을 만드시겠습니까?",
        icon = Icons.Default.Info
    )
}