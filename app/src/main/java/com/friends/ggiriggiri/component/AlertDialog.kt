package com.friends.ggiriggiri.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

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
) {
    AlertDialog(
        containerColor = Color.White,
        icon = { Icon(icon, contentDescription = null, tint = Color.Black) },
        title = { Text(text = dialogTitle, color = Color.Black) },
        text = {
            Text(
                text = dialogText,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(text = onPositiveText, color = Color.Black)
            }
        },
        dismissButton = onDismissRequest?.let {
            {
                if (onNegativeText != null){
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