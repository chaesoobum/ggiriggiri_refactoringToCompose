package com.friends.ggiriggiri.screen.ui.groupsubscreen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
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
import com.friends.ggiriggiri.screen.viewmodel.groupsubviewmodel.MakeGroupViewModel
import com.friends.ggiriggiri.util.MainScreenName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MakeGroupScreen(viewModel: MakeGroupViewModel = hiltViewModel()) {
    val isLoading by viewModel.isLoading.collectAsState()

    MakeGroupContent(
        groupNameText = viewModel.textFieldMakeGroupNameValue,
        groupNameLengthError = viewModel.groupNameLengthError,
        isGroupNameLengthError = viewModel.isGroupNameLengthError,
        groupCodeText = viewModel.textFieldMakeGroupCodeValue,
        groupCodeTextDuplicateResult = viewModel.textMakeGroupCodeDuplicateResult,
        groupCodeTextDuplicateResultColor = viewModel.textMakeGroupCodeDuplicateResultColor,
        groupCodeLengthError = viewModel.groupCodeLengthError,
        isGroupCodeLengthError = viewModel.isGroupCodeLengthError,
        groupPassword1Text = viewModel.textFieldMakeGroupPassword1Value,
        groupPassword2Text = viewModel.textFieldMakeGroupPassword2Value,
        passwordMismatchError = viewModel.passwordMismatchError,
        isPasswordError = viewModel.isPasswordError,
        checkPasswordMatch = viewModel::checkPasswordMatch,
        checkGroupCodeDuplicate = viewModel::checkGroupCodeDuplicate,
        checkGroupCodeLength = viewModel::checkGroupCodeLength,
        checkGroupCodeChange = viewModel::checkGroupCodeChange,
        checkGroupNameLength = viewModel::checkGroupNameLength,
        onClickMakeGroup = viewModel::makeGroup,
        isGroupCodeTouched = viewModel.isGroupCodeTouched,
        isGroupNameTouched = viewModel.isGroupNameTouched,
        showFailDialog = viewModel.showFailDialog,
        isLoading = isLoading,
        showSuccessDialog = viewModel.showSuccessDialog,
        onSuccessNavigate = {
            viewModel.makeGroupProcessLoading {
                viewModel.friendsApplication.navHostController.apply {
                    popBackStack(MainScreenName.SCREEN_USER_GROUP.name, true)
                    navigate(MainScreenName.SCREEN_USER_MAIN.name)
                }
            }
        }
    )
}


@Composable
fun MakeGroupContent(
    groupNameText: MutableState<String>,
    groupNameLengthError: MutableState<String>,
    isGroupNameLengthError: MutableState<Boolean>,
    groupCodeText: MutableState<String>,
    groupCodeTextDuplicateResult: MutableState<String>,
    groupCodeTextDuplicateResultColor: MutableState<Color>,
    groupCodeLengthError: MutableState<String>,
    isGroupCodeLengthError: MutableState<Boolean>,
    groupPassword1Text: MutableState<String>,
    groupPassword2Text: MutableState<String>,
    passwordMismatchError: MutableState<String>,
    isPasswordError: MutableState<Boolean>,
    checkPasswordMatch: () -> Unit,
    checkGroupCodeDuplicate: () -> Unit,
    checkGroupCodeLength: () -> Unit,
    checkGroupCodeChange: () -> Unit,
    checkGroupNameLength: () -> Unit,
    onClickMakeGroup: () -> Unit,
    isGroupCodeTouched: MutableState<Boolean>,
    isGroupNameTouched: MutableState<Boolean>,
    showFailDialog: MutableState<Boolean>,
    isLoading: Boolean,
    showSuccessDialog: MutableState<Boolean>,
    onSuccessNavigate: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(groupNameText.value) {
        if (isGroupNameTouched.value) {
            checkGroupNameLength()
        }
    }
    LaunchedEffect(groupPassword1Text.value, groupPassword2Text.value) {
        checkPasswordMatch()
    }
    LaunchedEffect(groupCodeText.value) {
        if (isGroupCodeTouched.value) {
            checkGroupCodeLength()
            checkGroupCodeChange()
        }
    }

    val focusManager: FocusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            }
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            textFieldValue = groupNameText,
            label = "그룹명",
            paddingTop = 5.dp,
            paddingStart = 20.dp,
            paddingEnd = 20.dp,
            singleLine = true,
            readOnly = false,
            supportText = groupNameLengthError,
            isError = isGroupNameLengthError,
            onValueChanged = {
                if (!isGroupNameTouched.value) {
                    isGroupNameTouched.value = true
                }
                groupNameText.value = it
            }
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 5.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
        OutlinedTextField(
            paddingTop = 5.dp,
            paddingEnd = 20.dp,
            paddingStart = 20.dp,
            textFieldValue = groupCodeText,
            label = "그룹코드",
            singleLine = true,
            readOnly = false,
            supportText = groupCodeLengthError,
            isError = isGroupCodeLengthError,
            onValueChanged = {
                if (!isGroupCodeTouched.value) {
                    isGroupCodeTouched.value = true
                }
                groupCodeText.value = it
            }
        )
        CustomButton(
            text = "중복확인",
            paddingTop = 10.dp,
            paddingEnd = 20.dp,
            paddingStart = 20.dp,
            onClick = {
                checkGroupCodeDuplicate()
            }
        )



        Text(
            text = groupCodeTextDuplicateResult.value,
            color = groupCodeTextDuplicateResultColor.value,
            fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp, end = 10.dp),
            //.alpha(0f),
            textAlign = TextAlign.Center
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 5.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
        OutlinedTextField(
            textFieldValue = groupPassword1Text,
            label = "그룹 비밀번호",
            paddingTop = 5.dp,
            paddingStart = 20.dp,
            paddingEnd = 20.dp,
            singleLine = true,
            trailingIconMode = OutlinedTextFieldEndIconMode.PASSWORD,
            inputType = OutlinedTextFieldInputType.PASSWORD,
            leadingIcon = ImageVector.vectorResource(R.drawable.key_24px),
            readOnly = false,
        )
        OutlinedTextField(
            textFieldValue = groupPassword2Text,
            label = "그룹 비밀번호 재입력",
            paddingTop = 5.dp,
            paddingStart = 20.dp,
            paddingEnd = 20.dp,
            singleLine = true,
            trailingIconMode = OutlinedTextFieldEndIconMode.PASSWORD,
            inputType = OutlinedTextFieldInputType.PASSWORD,
            leadingIcon = ImageVector.vectorResource(R.drawable.key_24px),
            readOnly = false,
            supportText = passwordMismatchError,
            isError = isPasswordError
        )
        CustomButton(
            text = "들어가기",
            paddingTop = 10.dp,
            paddingStart = 20.dp,
            paddingEnd = 20.dp,
            onClick = onClickMakeGroup
        )

    }
    // 프로그래스바
    CustomProgressDialog(isShowing = isLoading)

    // 성공 다이얼로그
    if (showSuccessDialog.value) {
        CustomAlertDialog(
            onDismiss = { showSuccessDialog.value = false },
            onDismissRequest = { showSuccessDialog.value = false },
            onConfirmation = {
                showSuccessDialog.value = false
                CoroutineScope(Dispatchers.Main).launch {
                    onSuccessNavigate()
                }
            },
            dialogTitle = "\"${groupNameText.value}\"",
            dialogText = "이 그룹을 만드시겠습니까?",
            icon = Icons.Default.Info
        )
    }
    // 실패 다이얼로그
    if (showFailDialog.value) {
        CustomAlertDialog(
            onDismiss = { showFailDialog.value = false },
            onConfirmation = { showFailDialog.value = false },
            onDismissRequest = {},
            onNegativeText = null,
            dialogTitle = "알림",
            dialogText = "모든 입력이 완벽하지 않습니다\n다시확인해주세요",
            icon = Icons.Default.Info
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMakeGroupContent() {
    // 임시 상태값 정의
    val groupNameText = remember { mutableStateOf("") }
    val groupNameLengthError = remember { mutableStateOf("") }
    val isGroupNameLengthError = remember { mutableStateOf(false) }

    val groupCodeText = remember { mutableStateOf("") }
    val groupCodeTextDuplicateResult = remember { mutableStateOf("") }
    val groupCodeTextDuplicateResultColor = remember { mutableStateOf(Color.Gray) }
    val groupCodeLengthError = remember { mutableStateOf("") }
    val isGroupCodeLengthError = remember { mutableStateOf(false) }

    val groupPassword1Text = remember { mutableStateOf("") }
    val groupPassword2Text = remember { mutableStateOf("") }
    val passwordMismatchError = remember { mutableStateOf("") }
    val isPasswordError = remember { mutableStateOf(false) }

    val isGroupCodeTouched = remember { mutableStateOf(false) }
    val isGroupNameTouched = remember { mutableStateOf(false) }

    val showFailDialog = remember { mutableStateOf(false) }
    val showSuccessDialog = remember { mutableStateOf(false) }

    MakeGroupContent(
        groupNameText = groupNameText,
        groupNameLengthError = groupNameLengthError,
        isGroupNameLengthError = isGroupNameLengthError,
        groupCodeText = groupCodeText,
        groupCodeTextDuplicateResult = groupCodeTextDuplicateResult,
        groupCodeTextDuplicateResultColor = groupCodeTextDuplicateResultColor,
        groupCodeLengthError = groupCodeLengthError,
        isGroupCodeLengthError = isGroupCodeLengthError,
        groupPassword1Text = groupPassword1Text,
        groupPassword2Text = groupPassword2Text,
        passwordMismatchError = passwordMismatchError,
        isPasswordError = isPasswordError,
        checkPasswordMatch = {},
        checkGroupCodeDuplicate = {},
        checkGroupCodeLength = {},
        checkGroupCodeChange = {},
        checkGroupNameLength = {},
        onClickMakeGroup = {},
        isGroupCodeTouched = isGroupCodeTouched,
        isGroupNameTouched = isGroupNameTouched,
        showFailDialog = showFailDialog,
        isLoading = false,
        showSuccessDialog = showSuccessDialog,
        onSuccessNavigate = {}
    )
}



