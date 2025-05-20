package com.friends.ggiriggiri.screen.ui.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomAlertDialog
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.component.CustomProgressDialog
import com.friends.ggiriggiri.component.TextButton
import com.friends.ggiriggiri.screen.viewmodel.userlogin.UserLoginViewModel
import com.friends.ggiriggiri.screen.viewmodel.userlogin.UserLoginViewModel.LoginNavigationEvent
import com.friends.ggiriggiri.util.MainScreenName

@Composable
fun UserLoginScreen(
    navHostController: NavHostController,
    userLoginViewModel: UserLoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val activity = context as Activity
    val isLoading by userLoginViewModel.isLoading.collectAsState()
    val intent = userLoginViewModel.googleLoginIntent.value

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        userLoginViewModel.handleGoogleSignInResult(result.data)
    }

    LaunchedEffect(intent) {
        if (intent != null) {
            launcher.launch(intent)
        }
    }

    //화면이동
    LaunchedEffect(Unit) {
        userLoginViewModel.loginNavigationEvent.collect { event ->
            when (event) {
                is LoginNavigationEvent.NavigateToGroup -> {
                    navHostController.popBackStack(MainScreenName.SCREEN_USER_LOGIN.name, true)
                    navHostController.navigate(MainScreenName.SCREEN_USER_GROUP.name)
                }
                is LoginNavigationEvent.NavigateToMain -> {
                    navHostController.popBackStack(MainScreenName.SCREEN_USER_LOGIN.name, true)
                    navHostController.navigate(MainScreenName.SCREEN_USER_MAIN.name)
                }
            }
        }
    }

    CustomProgressDialog(isShowing = isLoading)

    if (userLoginViewModel.showLoginFailDialog.value){
        CustomAlertDialog(
            onDismiss = {

            },
            onConfirmation = {
                userLoginViewModel.showLoginFailDialog.value = false
            },
            icon = Icons.Default.Info,
            dialogTitle = "로그인 실패",
            dialogText = "회원가입을 먼저 진행해주세요"
        )

    }


    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            },
        contentAlignment = Alignment.Center
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.main_logo),
                contentDescription = "설명 텍스트",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "끼리끼리",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontFamily = FontFamily(
                    Font(R.font.nanumsquarebold)
                ),
                fontSize = 40.sp
            )
            Text(
                text = "찐친들의 커뮤니티",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontFamily = FontFamily(
                    Font(R.font.nanumsquareregular)
                ),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 왼쪽 Divider
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )

                    // 가운데 텍스트
                    Text(
                        text = "로그인",
                        fontFamily = FontFamily(Font(R.font.nanumsquareregular)),
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // 오른쪽 Divider
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            userLoginViewModel.kakaoLogin(activity)
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
                            userLoginViewModel.NaverLogin(activity)
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
                            userLoginViewModel.onGoogleLoginClicked(activity)
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
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "아직 회원이 아니신가요?",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.nanumsquareregular)),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    TextButton(
                        text = "회원가입",
                        onClick = {
                            // 회원가입 클릭 시 로직
                            navHostController.navigate(MainScreenName.REGISTER1.name)
                        },
                        underline = true,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.nanumsquareextrabold)),
                    )
                }
            }
            CustomButton(
                text = "이용 가이드",
                paddingTop = 20.dp,
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                onClick = {

                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewLogin() {
    //Login()
}


