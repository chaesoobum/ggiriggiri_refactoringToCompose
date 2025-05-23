package com.friends.ggiriggiri.screen.ui.home

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ModalDrawer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomAlertDialog
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.component.CustomProgressDialog
import com.friends.ggiriggiri.component.OutlinedTextField
import com.friends.ggiriggiri.component.ProfileImage
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.component.UploadImage
import com.friends.ggiriggiri.screen.viewmodel.PublicViewModel
import com.friends.ggiriggiri.screen.viewmodel.home.DoResponseViewModel
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.findActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.intellij.lang.annotations.JdkConstants

@Composable
fun DoResponseScreen(
    viewmodel: DoResponseViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val pvm: PublicViewModel = hiltViewModel(LocalContext.current.findActivity())
    LaunchedEffect(Unit) {
        pvm.requestModel.value?.let {requestModel->
            viewmodel.requestModel.value = requestModel
        }
        pvm.requesterName.value?.let{requesterName->
            viewmodel.requesterName.value = requesterName
        }
        //요청정보를삭제한다
        pvm.deleteRequestModel()
        pvm.deleteRequesterName()
    }
    
    DoResponseContent(viewmodel, navHostController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoResponseContent(
    viewmodel: DoResponseViewModel,
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
                title = "응답하기",
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    navHostController.popBackStack(MainScreenName.SCREEN_DO_RESPONSE.name, true)
                }
            )
        },
        containerColor = Color.White,
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = viewmodel.requesterName.value,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "의 요청",
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = viewmodel.requestModel.value?.requestMessage.toString(),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 20.dp, end = 20.dp),
                contentAlignment = Alignment.Center,

                ) {
                ProfileImage(
                    modifier = Modifier
                        .height(500.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit,
                    imageUrl = viewmodel.requestModel.value?.requestImage.toString()
                )
            }
            /*
            이미지 업로드
            */
            UploadImage(
                onImageSelected = { uri ->
                    viewmodel.responseImage.value = uri
                }
            )

            /*
            요청 텍스트
            */
            OutlinedTextField(
                textFieldValue = viewmodel.responseText,
                label = "응답하기",
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                paddingTop = 20.dp,
                singleLine = true
            )

            /*
            응답 버튼
            */
            CustomButton(
                text = "응답하기",
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                paddingTop = 20.dp,
                onClick = {
                    viewmodel.responseValid()
                }
            )

            Spacer(modifier = Modifier.height(100.dp))

            // 프로그래스바
            CustomProgressDialog(
                isShowing = viewmodel.isLoading.value,
                uploadProgress = viewmodel.uploadProgress.value.takeIf { it in 1..99 } // 진행 중일 때만 표시
            )

            if (viewmodel.showFailDialog.value) {
                CustomAlertDialog(
                    onDismiss = { viewmodel.changeShowFailDialog(false) },
                    onConfirmation = { viewmodel.changeShowFailDialog(false) },
                    dialogTitle = "알림",
                    dialogText = "사진과 응답 내용을 모두 입력해주세요.",
                    icon = Icons.Default.Info
                )
            }

            if (viewmodel.showConfirmDialog.value) {
                CustomAlertDialog(
                    onDismiss = { viewmodel.changeShowConfirmDialog(false) },
                    onConfirmation = {
                        viewmodel.changeShowConfirmDialog(false)
                        viewmodel.uploadImageToStorage(context)
                    },
                    onNegativeText = "취소",
                    onDismissRequest = { viewmodel.changeShowConfirmDialog(false) },
                    dialogTitle = "확인",
                    dialogText = "응답하시겠습니까?",
                    icon = Icons.Default.Info
                )
            }


        }
    }
}

@Preview
@Composable
private fun a() {
    //DoResponseContent()
}