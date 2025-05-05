package com.friends.ggiriggiri

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.friends.ggiriggiri.component.CustomProgressDialog
import com.friends.ggiriggiri.firebase.model.UserModel
import com.friends.ggiriggiri.screen.ui.UserGroupScreen
import com.friends.ggiriggiri.screen.ui.UserLoginScreen
import com.friends.ggiriggiri.screen.ui.UserMainScreen
import com.friends.ggiriggiri.screen.ui.home.DoAnswerScreen
import com.friends.ggiriggiri.screen.ui.home.DoRequestScreen
import com.friends.ggiriggiri.screen.ui.home.DoResponseScreen
import com.friends.ggiriggiri.screen.ui.home.memberlist.MemberListDetail
import com.friends.ggiriggiri.screen.ui.memories.question.viewonequestion.ViewOneQuestionScreen
import com.friends.ggiriggiri.screen.ui.memories.request.viewonerequest.ViewOneRequestScreen
import com.friends.ggiriggiri.screen.ui.mypage.LegalScreen
import com.friends.ggiriggiri.screen.ui.mypage.SettingGroupScreen
import com.friends.ggiriggiri.screen.ui.notification.ViewNotificationScreen
import com.friends.ggiriggiri.screen.viewmodel.userlogin.UserLoginViewModel
import com.friends.ggiriggiri.ui.theme.GgiriggiriTheme
import com.friends.ggiriggiri.util.MainScreenName
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 상태바를 투명하게 만들기
        window.statusBarColor = Color.TRANSPARENT

        // 상태바 아이콘을 어둡게 (밝은 배경일 경우)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        setContent {
            GgiriggiriTheme {
                Main()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main(
    userLoginViewModel: UserLoginViewModel = hiltViewModel(),
) {
    val navHostController = rememberNavController()
    val context = LocalContext.current
    val friendsApplication = context.applicationContext as FriendsApplication
    friendsApplication.navHostController = navHostController

    Log.d("preferenceManager",userLoginViewModel.preferenceManager.isLoggedIn().toString())


    var startDestination by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (userLoginViewModel.preferenceManager.isLoggedIn()) {
            val userDocumentId = userLoginViewModel.preferenceManager.getUserDocumentId()
            if (!userDocumentId.isNullOrEmpty()) {
                try {
                    val userModel = userLoginViewModel.loadUserModel(userDocumentId)
                    friendsApplication.loginUserModel = userModel

                    startDestination = if (userModel.userGroupDocumentID.isEmpty()) {
                        MainScreenName.SCREEN_USER_GROUP.name
                    } else {
                        MainScreenName.SCREEN_USER_MAIN.name
                    }
                } catch (e: Exception) {
                    userLoginViewModel.preferenceManager.clearLoginInfo()
                    friendsApplication.loginUserModel = UserModel()
                    startDestination = MainScreenName.SCREEN_USER_LOGIN.name
                }
            } else {
                startDestination = MainScreenName.SCREEN_USER_LOGIN.name
            }
        } else {
            startDestination = MainScreenName.SCREEN_USER_LOGIN.name
        }
        isLoading = false
    }


    CustomProgressDialog(isShowing = isLoading)


    // 여기 중요
    if (startDestination != null) {
        NavHost(
            navController = navHostController,
            startDestination = startDestination!!,
            enterTransition = {
                fadeIn(tween(300)) +
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
            },
            popExitTransition = {
                fadeOut(tween(300)) +
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300))
            },
            exitTransition = {
                fadeOut(tween(300))
            },
            popEnterTransition = {
                fadeIn(tween(300))
            }
        ) {
            composable(route = MainScreenName.SCREEN_USER_LOGIN.name) {
                UserLoginScreen()
            }

            composable(route = MainScreenName.SCREEN_USER_MAIN.name) {
                UserMainScreen()
            }

            composable(route = MainScreenName.SCREEN_USER_GROUP.name) {
                UserGroupScreen()
            }

            composable(
                route = "${MainScreenName.SCREEN_VIEW_ONE_REQUEST.name}/{requestDocumentId}"
            ) { backStackEntry ->
                val requestDocumentId = backStackEntry.arguments?.getString("requestDocumentId") ?: ""
                ViewOneRequestScreen(requestDocumentId = requestDocumentId)
            }


            composable(route = MainScreenName.SCREEN_VIEW_ONE_QUESTION.name) {
                ViewOneQuestionScreen()
            }

            composable(route = MainScreenName.SCREEN_DO_REQUEST.name) {
                DoRequestScreen(navHostController = navHostController)
            }

            composable(route = MainScreenName.SCREEN_DO_ANSWER.name) {
                DoAnswerScreen(navHostController = navHostController)
            }

            composable(route = MainScreenName.SCREEN_DO_RESPONSE.name) {
                DoResponseScreen(navHostController = navHostController)
            }

            composable(route = MainScreenName.SCREEN_NOTIFICATION.name) {
                ViewNotificationScreen()
            }

            composable(route = MainScreenName.SCREEN_SETTING_GROUP.name) {
                SettingGroupScreen()
            }

            composable(route = MainScreenName.SCREEN_LEGAL.name) {
                LegalScreen(navHostController = navHostController)
            }

            composable(route = MainScreenName.SCREEN_MEMBER_LIST_DETAIL.name) {
                MemberListDetail(navHostController = navHostController)
            }

            composable("test") {
                TestScreen()
            }
            
        }
    }
}

//release(this)
//@RequiresApi(Build.VERSION_CODES.P)
//fun release(context: Context) {
//    val packageInfo = context.packageManager.getPackageInfo(
//        context.packageName,
//        PackageManager.GET_SIGNING_CERTIFICATES
//    )
//    val signatures = packageInfo.signingInfo!!.apkContentsSigners
//    val messageDigest = MessageDigest.getInstance("SHA")
//    for (signature in signatures) {
//        val hash = messageDigest.digest(signature.toByteArray())
//        val base64Hash = Base64.encodeToString(hash, Base64.NO_WRAP)
//        Log.d("ReleaseKeyHash", base64Hash)
//    }
//}



