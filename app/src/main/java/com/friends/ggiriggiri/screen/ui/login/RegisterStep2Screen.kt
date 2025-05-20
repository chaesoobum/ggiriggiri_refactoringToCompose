package com.friends.ggiriggiri.screen.ui.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomAlertDialog
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.component.CustomProgressDialog
import com.friends.ggiriggiri.component.OutlinedTextField
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.viewmodel.userlogin.RegisterStep1ViewModel
import com.friends.ggiriggiri.screen.viewmodel.userlogin.RegisterStep2ViewModel
import com.friends.ggiriggiri.screen.viewmodel.userlogin.UserLoginViewModel.LoginNavigationEvent
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.UserSocialLoginState
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep2Screen(
    navController: NavController,
    nickName: String,
    viewModel: RegisterStep2ViewModel = hiltViewModel()
) {
    RegisterStep2Content(navController, nickName, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep2Content(
    navController: NavController,
    nickName: String,
    viewModel: RegisterStep2ViewModel
) {
    val context = LocalContext.current
    val activity = context as Activity
    val focusManager: FocusManager = LocalFocusManager.current

    //구글 인텐트
    val intent = viewModel.googleLoginIntent.value
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleGoogleSignInResult(result.data,nickName)
    }
    LaunchedEffect(intent) {
        if (intent != null) {
            launcher.launch(intent)
        }
    }

    // 확인 다이얼로그
    if (viewModel.showSocialLoginDialog.value) {
        CustomAlertDialog(
            onDismiss = {},
            onConfirmation = {
                viewModel.showSocialLoginDialog.value = false
                //연결
                if (viewModel.socialLoginDialogText.value == UserSocialLoginState.KAKAO.str){
                    viewModel.kakaoRegisterProcess(activity,nickName)
                }
                else if (viewModel.socialLoginDialogText.value == UserSocialLoginState.NAVER.str){
                    viewModel.naverRegisterProcess(activity,nickName)
                }
                else if (viewModel.socialLoginDialogText.value == UserSocialLoginState.GOOGLE.str){
                    viewModel.onGoogleLoginClicked(activity)
                }
            },
            onDismissRequest = {
                viewModel.showSocialLoginDialog.value = false
                viewModel.resetSocialLoginDialogText()
            },
            icon = Icons.Default.Info,
            dialogTitle = "알림",
            dialogText = "${viewModel.socialLoginDialogText.value}로 연결하시겠습니까?"
        )
    }

    //이미 존재하는 회원
    if (viewModel.showAccountExistAlready.value) {
        CustomAlertDialog(
            onDismiss = {},
            onConfirmation = {
                viewModel.showAccountExistAlready.value = false
                             },
            onDismissRequest = {
                viewModel.showAccountExistAlready.value = false
            },
            icon = Icons.Default.Info,
            dialogTitle = "알림",
            dialogText = "이미 가입된 소셜 계정입니다"
        )
    }


    //화면이동
    LaunchedEffect(Unit) {
        viewModel.loginNavigationEvent.collect {
            navController.popBackStack(MainScreenName.SCREEN_USER_LOGIN.name, true)
            navController.navigate(MainScreenName.SCREEN_USER_GROUP.name)
        }
    }

    val isLoading by viewModel.isLoading.collectAsState()
    CustomProgressDialog(isShowing = isLoading)

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
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = "닉네임 : ${nickName}",
                fontFamily = FontFamily(
                    Font(R.font.nanumsquarebold)
                ),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = "연결할 소셜 계정을 선택해주세요",
                fontFamily = FontFamily(
                    Font(R.font.nanumsquarebold)
                ),
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            viewModel.connectToKakao()
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.kakao),
                            contentDescription = "Kakao Login",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(40.dp))

                    IconButton(
                        onClick = {
                            viewModel.connectToNaver()
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.naver),
                            contentDescription = "Naver Login",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(40.dp))

                    IconButton(
                        onClick = {
                            viewModel.connectToGoogle()
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "Google Login",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewRegisterStep2Screen() {
    //RegisterStep2Screen()
}