package com.friends.ggiriggiri.screen.ui.mypage

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomAlertDialog
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.component.CustomProgressDialog
import com.friends.ggiriggiri.component.OutlinedTextField
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.screen.viewmodel.mypage.MyPageViewModel
import com.friends.ggiriggiri.screen.viewmodel.mypage.SettingGroupViewModel
import com.friends.ggiriggiri.util.MainScreenName

@Composable
fun SettingGroupScreen(
    navHostController: NavHostController,
    viewModel: SettingGroupViewModel = hiltViewModel(),
    groupName: String
) {

    //원래그룹이름을 세팅한다
    viewModel.groupName.value = groupName
    viewModel.changeGroupNameValue.value = groupName

    SettingGroupContent(navHostController, viewModel)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingGroupContent(
    navHostController: NavHostController,
    viewModel: SettingGroupViewModel,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            },
        topBar = {
            TopAppBar(
                title = "그룹명 변경",
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    navHostController.popBackStack()
                },
                isDivider = false
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                textFieldValue = viewModel.changeGroupNameValue,
                paddingTop = 20.dp,
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                label = "그룹명",
                singleLine = true,
            )

            CustomButton(
                text = "변경",
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                paddingTop = 20.dp,
                onClick = {
                    viewModel.changeGroupNameValid()
                }
            )

            CustomProgressDialog(isShowing = viewModel.isChangeGroupNameLoading.value)

            if (viewModel.isShowFailDialogSame.value) {
                CustomAlertDialog(
                    onDismiss = {}, // 외부 클릭 시 닫히지 않게 하려면 여기 빈 람다
                    onConfirmation = { viewModel.isShowFailDialogSame.value = false },
                    dialogTitle = "알림",
                    dialogText = "현재 그룹명과 같습니다",
                    icon = Icons.Default.Info
                )
            }
            if (viewModel.isShowFailDialogEmpty.value) {
                CustomAlertDialog(
                    onDismiss = {}, // 외부 클릭 시 닫히지 않게 하려면 여기 빈 람다
                    onConfirmation = { viewModel.isShowFailDialogEmpty.value = false },
                    dialogTitle = "알림",
                    dialogText = "그룹명이 공백입니다",
                    icon = Icons.Default.Info
                )
            }

            if (viewModel.isConfirmDialog.value) {
                CustomAlertDialog(
                    onDismiss = {}, // 외부 클릭 시 닫히지 않게 하려면 여기 빈 람다
                    onConfirmation = {
                        viewModel.isConfirmDialog.value = false
                        viewModel.changeGroupNameProcessing(
                            context,
                            viewModel.changeGroupNameValue.value
                        )
                    },
                    onDismissRequest = { viewModel.isConfirmDialog.value = false },
                    dialogTitle = "알림",
                    dialogText = "\"${viewModel.changeGroupNameValue.value}\"으로\n그룹명을 변경하시겠습니까?",
                    icon = Icons.Default.Info
                )

            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewSettingGroupContent() {
    //SettingGroupContent()
}