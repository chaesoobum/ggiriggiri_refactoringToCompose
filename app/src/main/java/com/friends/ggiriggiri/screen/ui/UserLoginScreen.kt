package com.friends.ggiriggiri.screen.ui

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.component.CustomProgressDialog
import com.friends.ggiriggiri.component.OutlinedTextField
import com.friends.ggiriggiri.component.OutlinedTextFieldEndIconMode
import com.friends.ggiriggiri.component.OutlinedTextFieldInputType
import com.friends.ggiriggiri.component.TextButton
import com.friends.ggiriggiri.screen.viewmodel.userlogin.UserLoginViewModel

@Composable
fun UserLoginScreen(
    navHostController: NavHostController,
    userLoginViewModel: UserLoginViewModel = hiltViewModel()
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

    UserLoginContent(userLoginViewModel)

    CustomProgressDialog(isShowing = isLoading)
}


@Composable
fun UserLoginContent(
    userLoginViewModel: UserLoginViewModel
) {
    val activity = LocalActivity.current!!

    val idText = userLoginViewModel.textFieldUserLoginIdValue
    val passwordText = userLoginViewModel.textFieldUserLoginPasswordValue

    Scaffold { innerPadding ->
        val focusManager = LocalFocusManager.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
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

                OutlinedTextField(
                    textFieldValue = idText,
                    label = "아이디",
                    paddingTop = 50.dp,
                    paddingStart = 20.dp,
                    paddingEnd = 20.dp,
                    singleLine = true,
                    inputCondition = "[^a-zA-Z0-9_]?",
                    leadingIcon = ImageVector.vectorResource(R.drawable.person_24px),
                    readOnly = false
                )

                OutlinedTextField(
                    textFieldValue = passwordText,
                    label = "비밀번호",
                    paddingTop = 5.dp,
                    paddingStart = 20.dp,
                    paddingEnd = 20.dp,
                    singleLine = true,
                    trailingIconMode = OutlinedTextFieldEndIconMode.PASSWORD,
                    inputType = OutlinedTextFieldInputType.PASSWORD,
                    inputCondition = "[^a-zA-Z0-9_]?",
                    leadingIcon = ImageVector.vectorResource(R.drawable.key_24px),
                    readOnly = false
                )

                CustomButton(
                    text = "로그인",
                    paddingTop = 20.dp,
                    paddingStart = 20.dp,
                    paddingEnd = 20.dp
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier
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
                            },
                            underline = true,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.nanumsquareextrabold)),
                        )
                    }

                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            text = "아이디 찾기",
                            onClick = {
                                // 아이디 찾기 클릭 시 로직
                            },
                            underline = true,
                            fontSize = 13.sp,
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        TextButton(
                            text = "비밀번호 찾기",
                            onClick = {
                                // 비밀번호 찾기 클릭 시 로직
                            },
                            underline = true,
                            fontSize = 13.sp,
                        )
                    }
                }

                // SNS 로그인
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
                            text = "SNS 계정으로 로그인하기",
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
                                userLoginViewModel.onKakaoLoginClicked(activity)
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
                                userLoginViewModel.onNaverLoginClicked(activity)
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
                }
            }
        }
    }
}



@Preview
@Composable
fun MyScreenPreview() {
//    val idText = remember { mutableStateOf("exampleId") }
//    val passwordText = remember { mutableStateOf("examplePassword") }
//
//    UserLoginContent(
//        idText = idText,
//        passwordText = passwordText,
//        onKakaoLoginClicked = {},
//        onNaverLoginClicked = {},
//        onGoogleLoginClicked = {},
//    )

}
