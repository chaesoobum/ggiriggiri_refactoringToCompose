package com.friends.ggiriggiri.screen.ui.groupsubscreen

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomAlertDialog
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.component.CustomProgressDialog
import com.friends.ggiriggiri.component.OutlinedTextField
import com.friends.ggiriggiri.component.OutlinedTextFieldEndIconMode
import com.friends.ggiriggiri.component.OutlinedTextFieldInputType
import com.friends.ggiriggiri.screen.viewmodel.groupsubviewmodel.JoinGroupViewModel
import com.friends.ggiriggiri.util.MainScreenName

@Composable
fun JoinGroupScreen(viewModel: JoinGroupViewModel = hiltViewModel()) {
    JoinGroupContent(viewModel)

    if (viewModel.navigateToMain.value) {
        LaunchedEffect(Unit) {
            viewModel.friendsApplication.navHostController.apply {
                popBackStack(MainScreenName.SCREEN_USER_GROUP.name, true)
                navigate(MainScreenName.SCREEN_USER_MAIN.name)
            }
        }
    }
}


@Composable
fun JoinGroupContent(viewModel: JoinGroupViewModel) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            }
    ) {
        OutlinedTextField(
            textFieldValue = viewModel.textFieldJoinGroupCodeValue,
            label = "그룹코드",
            paddingTop = 5.dp,
            paddingStart = 20.dp,
            paddingEnd = 20.dp,
            singleLine = true,
            leadingIcon = ImageVector.vectorResource(R.drawable.group_24px),
            readOnly = false
        )
        OutlinedTextField(
            textFieldValue = viewModel.textFieldJoinGroupPasswordValue,
            label = "그룹 비밀번호",
            paddingTop = 5.dp,
            paddingStart = 20.dp,
            paddingEnd = 20.dp,
            singleLine = true,
            trailingIconMode = OutlinedTextFieldEndIconMode.PASSWORD,
            inputType = OutlinedTextFieldInputType.PASSWORD,
            leadingIcon = ImageVector.vectorResource(R.drawable.key_24px),
            readOnly = false
        )
        CustomButton(
            text = "들어가기",
            paddingTop = 10.dp,
            paddingStart = 20.dp,
            paddingEnd = 20.dp,
            onClick = viewModel::joinGroup
        )
    }

    CustomProgressDialog(isShowing = isLoading)

    if (viewModel.triggerForToast.value) {
        viewModel.triggerForToast.value = false
        Toast.makeText(context, "그룹코드와 비밀번호를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
    }

    if (viewModel.showFailDialog.value) {
        CustomAlertDialog(
            onDismiss = { viewModel.showFailDialogFalse() },
            onConfirmation = { viewModel.showFailDialogFalse() },
            onDismissRequest = {},
            onNegativeText = null,
            dialogTitle = "알림",
            dialogText = "입력한 그룹코드와 비밀번호에 맞는 그룹이 없습니다",
            icon = Icons.Default.Info
        )
    }
}



