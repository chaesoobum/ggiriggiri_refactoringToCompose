package com.friends.ggiriggiri.screen.ui.mypage

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomAlertDialog
import com.friends.ggiriggiri.component.CustomIconButton
import com.friends.ggiriggiri.component.CustomProgressDialog
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.viewmodel.mypage.MyPageViewModel
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.tools.openAppNotificationSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadGroupName()
    }

    MyPageContent(modifier, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageContent(
    modifier: Modifier,
    viewModel: MyPageViewModel
) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = "마이 페이지",
                menuItems = {
                    CustomIconButton(
                        icon = ImageVector.vectorResource(R.drawable.notifications_24px),
                        iconButtonOnClick = {
                            viewModel.friendsApplication.navHostController.apply {
                                navigate(MainScreenName.SCREEN_NOTIFICATION.name)
                            }
                        }
                    )
                },
            )
        }
    ) { innerPadding ->
        val focusManager: FocusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .background(Color.White)
                .pointerInput(Unit) {
                    // 외부 터치 시 포커스를 해제하고 키보드 내리기
                    detectTapGestures(onTap = {
                        focusManager.clearFocus() // 포커스를 해제
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //프로필 이미지 변경버튼
            ProfileImageButton()
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = viewModel.friendsApplication.loginUserModel.userName,
                fontFamily = FontFamily(Font(R.font.nanumsquarebold)),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                text = "그룹명",
                fontFamily = FontFamily(Font(R.font.nanumsquareregular)),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(5.dp))
            GroupNameWithSettingsButton(
                groupName = viewModel.groupName.value,
                onGroupNameClick = {
                    viewModel.friendsApplication.navHostController.
                    navigate("${MainScreenName.SCREEN_SETTING_GROUP.name}/${viewModel.groupName.value}")
                },
                loadingGroupName = viewModel.loadingGroupName.value
            )
            Spacer(modifier = Modifier.height(20.dp))
            Divider()
            MyPageButton(
                text = "알림설정",
                imageVector = ImageVector.vectorResource(R.drawable.notification_settings_24px),
                onMyPageButtonClick = {
                    openAppNotificationSettings(context)
                }
            )
            MyPageButton(
                text = "로그아웃",
                imageVector = ImageVector.vectorResource(R.drawable.logout_24px),
                onMyPageButtonClick = {
                    viewModel.showLogoutDialogTrue()
                }
            )
            MyPageButton(
                text = "그룹탈퇴",
                imageVector = ImageVector.vectorResource(R.drawable.close_24px),
                onMyPageButtonClick = {
                    viewModel.showLeaveGroupDialogTrue()
                }
            )
            MyPageButton(
                text = "회원탈퇴",
                imageVector = ImageVector.vectorResource(R.drawable.disabled_by_default_24px),
                onMyPageButtonClick = {
                    Toast.makeText(context, "토스트 메시지입니다!", Toast.LENGTH_SHORT).show()
                }
            )
            MyPageButton(
                text = "개인정보처리방침/\n이용약관",
                imageVector = ImageVector.vectorResource(R.drawable.info_24px),
                onMyPageButtonClick = {
                    viewModel.friendsApplication.navHostController.apply {
                        navigate(MainScreenName.SCREEN_LEGAL.name)
                    }
                }
            )

            // 프로그레스 다이얼로그
            CustomProgressDialog(viewModel.isLoading.value)

            if (viewModel.showLogoutDialog.value) {
                CustomAlertDialog(
                    onDismiss = { viewModel.showLogoutDialogFalse() },
                    onConfirmation = {
                        viewModel.showLogoutDialogFalse()
                        viewModel.logout()
                    },
                    onPositiveText = "네",
                    onDismissRequest = { viewModel.showLogoutDialogFalse() },
                    onNegativeText = "취소",
                    dialogTitle = "알림",
                    dialogText = "로그아웃 하시겠습니까?",
                    icon = Icons.Default.Info
                )
            }

            if (viewModel.showLeaveTheGroupDialog.value) {
                CustomAlertDialog(
                    onDismiss = { viewModel.showLeaveGroupDialogFalse() },
                    onConfirmation = {
                        viewModel.showLeaveGroupDialogFalse()
                        viewModel.leaveTheGroup()
                    },
                    onPositiveText = "네",
                    onDismissRequest = { viewModel.showLeaveGroupDialogFalse() },
                    onNegativeText = "취소",
                    dialogTitle = "알림",
                    dialogText = "그룹에서 나가시겠습니까?",
                    icon = Icons.Default.Info
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}