package com.friends.ggiriggiri.screen.ui.groupsubscreen

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun JoinGroupScreen(viewModel: JoinGroupViewModel = hiltViewModel()) {
    val isLoading by viewModel.isLoading.collectAsState()

    JoinGroupContent(
        viewModel = viewModel,
        groupCodeText = viewModel.textFieldJoinGroupCodeValue,
        groupPasswordText = viewModel.textFieldJoinGroupPasswordValue,
        isLoading = isLoading,
        triggerForToast = viewModel.triggerForToast,
        showFailDialog = viewModel.showFailDialog,
        isGroupExist = viewModel.isGroupExist,
        onJoinGroup = viewModel::joinGroup,
        onNavigateToMain = {
            viewModel.friendsApplication.navHostController.apply {
                popBackStack(MainScreenName.SCREEN_USER_GROUP.name, true)
                navigate(MainScreenName.SCREEN_USER_MAIN.name)
            }
        }
    )
}


@Composable
fun JoinGroupContent(
    viewModel: JoinGroupViewModel,
    groupCodeText: MutableState<String>,
    groupPasswordText: MutableState<String>,
    isLoading: Boolean,
    triggerForToast: MutableState<Boolean>,
    showFailDialog: MutableState<Boolean>,
    isGroupExist: MutableState<Boolean>,
    onJoinGroup: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            }
    ) {
        OutlinedTextField(
            textFieldValue = groupCodeText,
            label = "그룹코드",
            paddingTop = 5.dp,
            paddingStart = 20.dp,
            paddingEnd = 20.dp,
            singleLine = true,
            leadingIcon = ImageVector.vectorResource(R.drawable.group_24px),
            readOnly = false
        )
        OutlinedTextField(
            textFieldValue = groupPasswordText,
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
            onClick = onJoinGroup
        )
    }

    // 프로그레스 다이얼로그
    CustomProgressDialog(isShowing = isLoading)

    // 토스트
    if (triggerForToast.value) {
        triggerForToast.value = false
        Toast.makeText(context, "그룹코드와 비밀번호를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
    }

    // 그룹 존재 여부 확인 후 내비게이션
    if (isGroupExist.value) {
        isGroupExist.value = false
        onNavigateToMain()
    }

    // 실패 다이얼로그
    if (showFailDialog.value) {
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


