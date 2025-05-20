package com.friends.ggiriggiri.screen.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.friends.ggiriggiri.Main
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomAlertDialog
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.component.OutlinedTextField
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.viewmodel.userlogin.RegisterStep1ViewModel
import com.friends.ggiriggiri.util.MainScreenName
import java.net.URLEncoder

@Composable
fun RegisterStep1Screen(
    navController: NavController,
    viewModel: RegisterStep1ViewModel = hiltViewModel()
) {
    RegisterStep1Content(navController,viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep1Content(
    navController: NavController,
    viewModel: RegisterStep1ViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.navigateToNextStep.collect { nickname ->
            val encoded = URLEncoder.encode(nickname, "UTF-8")
            navController.navigate("${MainScreenName.REGISTER2.name}/$encoded")
        }
    }

    if (viewModel.showLoginFailDialog.value){
        CustomAlertDialog(
            onDismiss = {

            },
            onConfirmation = {
                viewModel.showLoginFailDialog.value = false
            },
            icon = Icons.Default.Info,
            dialogTitle = "알림",
            dialogText = "닉네임을 2자이상 10자 이하로 입력해주세요"
        )

    }

    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                // 외부 터치 시 포커스를 해제하고 키보드 내리기
                detectTapGestures(onTap = {
                    focusManager.clearFocus() // 포커스를 해제
                })
            },
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = "회원가입",
                isDivider = false,
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    //뒤로가기
                    navController.popBackStack()
                },
            )

        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                textFieldValue = viewModel.nicknameText,
                label = "닉네임 입력",
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                singleLine = true,
                leadingIcon = ImageVector.vectorResource(R.drawable.person_24px),
                readOnly = false
            )
            CustomButton(
                text = "다음",
                paddingTop = 20.dp,
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                onClick = {
                    viewModel.checkNicknameText()
                }
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun previewRegisterStep1Screen() {
//    RegisterStep1Screen()
//}