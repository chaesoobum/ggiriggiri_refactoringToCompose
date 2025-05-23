package com.friends.ggiriggiri.screen.ui.home

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
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
import com.friends.ggiriggiri.component.UploadImage
import com.friends.ggiriggiri.screen.viewmodel.home.DoRequestViewModel
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName
import java.io.File
import java.nio.file.WatchEvent

@Composable
fun DoRequestScreen(
    viewModel: DoRequestViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    DoRequestContent(viewModel, navHostController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoRequestContent(
    viewModel: DoRequestViewModel,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val focusManager: FocusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                // 외부 터치 시 포커스를 해제하고 키보드 내리기
                detectTapGestures(onTap = {
                    focusManager.clearFocus() // 포커스를 해제
                })
            },
        topBar = {
            TopAppBar(
                title = "요청하기",
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    navHostController.popBackStack(MainScreenName.SCREEN_DO_REQUEST.name, true)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            /*
            이미지 업로드
            */
            UploadImage(
                onImageSelected = { uri ->
                    viewModel.requestImage.value = uri
                }
            )
            /*
            요청 텍스트
            */
            OutlinedTextField(
                textFieldValue = viewModel.requestText,
                label = "요청하기",
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                paddingTop = 20.dp,
                singleLine = true
            )
            /*
            요청 버튼
            */
            CustomButton(
                text = "요청하기",
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                paddingTop = 20.dp,
                onClick = {
                    viewModel.requestValid()
                }
            )
            // 프로그래스바
            CustomProgressDialog(
                isShowing = viewModel.isLoading.value,
                uploadProgress = viewModel.uploadProgress.value
            )

            if (viewModel.showFailDialog.value) {
                CustomAlertDialog(
                    onDismiss = { viewModel.changeShowFailDialog(false) },
                    onConfirmation = { viewModel.changeShowFailDialog(false) },
                    dialogTitle = "알림",
                    dialogText = "사진과 요청 내용을 모두 입력해주세요.",
                    icon = Icons.Default.Info
                )
            }

            if (viewModel.showConfirmDialog.value) {
                CustomAlertDialog(
                    onDismiss = { viewModel.changeShowConfirmDialog(false) },
                    onConfirmation = {
                        viewModel.changeShowConfirmDialog(false)
                        viewModel.uploadImageToStorage(context)
                    },
                    onNegativeText = "취소",
                    onDismissRequest = { viewModel.changeShowConfirmDialog(false) },
                    dialogTitle = "확인",
                    dialogText = "요청하시겠습니까?",
                    icon = Icons.Default.Info
                )

                if (viewModel.requestEnd.value) {
                    viewModel.friendsApplication.navHostController.popBackStack(
                        MainScreenName.SCREEN_DO_REQUEST.name,
                        true
                    )
                }
            }
        }
    }
}
