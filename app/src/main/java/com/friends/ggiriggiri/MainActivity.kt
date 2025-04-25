package com.friends.ggiriggiri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.friends.ggiriggiri.screen.ui.UserGroupScreen
import com.friends.ggiriggiri.screen.ui.UserLoginScreen
import com.friends.ggiriggiri.screen.ui.UserMainScreen
import com.friends.ggiriggiri.ui.theme.GgiriggiriTheme
import com.friends.ggiriggiri.util.MainScreenName
import dagger.hilt.android.AndroidEntryPoint
import com.friends.ggiriggiri.screen.ui.ViewNotificationScreen
import com.friends.ggiriggiri.screen.ui.home.DoAnswerScreen
import com.friends.ggiriggiri.screen.ui.home.DoRequestScreen
import com.friends.ggiriggiri.screen.ui.home.memberlist.MemberListDetail
import com.friends.ggiriggiri.screen.ui.memories.question.viewonequestion.ViewOneQuestionScreen
import com.friends.ggiriggiri.screen.ui.memories.request.viewonerequest.ViewOneRequestScreen
import com.friends.ggiriggiri.screen.ui.mypage.LegalScreen
import com.friends.ggiriggiri.screen.ui.mypage.SettingGroupScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 상태바를 투명하게 만들기
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        // 상태바 아이콘을 어둡게 (밝은 배경일 경우)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        setContent {
            GgiriggiriTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    val navHostController = rememberNavController()
    val context = LocalContext.current
    val friendsApplication = context.applicationContext as FriendsApplication
    friendsApplication.navHostController = navHostController

    val preferenceManager = remember { PreferenceManager(context) }

    // 자동 로그인 여부에 따라 시작 목적지 결정
    val startDestination = if (preferenceManager.isLoggedIn()) {
        //MainScreenName.SCREEN_USER_LOGIN.name
        MainScreenName.SCREEN_USER_MAIN.name
    } else {
        //MainScreenName.SCREEN_USER_LOGIN.name
        //MainScreenName.SCREEN_USER_GROUP.name
        MainScreenName.SCREEN_USER_MAIN.name
    }

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
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

        composable(route = MainScreenName.SCREEN_VIEW_ONE_REQUEST.name) {
            ViewOneRequestScreen()
        }

        composable(route = MainScreenName.SCREEN_VIEW_ONE_QUESTION.name) {
            ViewOneQuestionScreen()
        }

        composable(route = MainScreenName.SCREEN_DO_REQUEST.name) {
            DoRequestScreen()
        }

        composable(route = MainScreenName.SCREEN_DO_ANSWER.name) {
            DoAnswerScreen()
        }

        composable(route = MainScreenName.SCREEN_NOTIFICATION.name) {
            ViewNotificationScreen()
        }

        composable(route = MainScreenName.SCREEN_SETTING_GROUP.name) {
            SettingGroupScreen()
        }

        composable(route = MainScreenName.SCREEN_LEGAL.name) {
            LegalScreen()
        }

        composable(route = MainScreenName.SCREEN_MEMBER_LIST_DETAIL.name) {
            MemberListDetail()
        }
    }
}



