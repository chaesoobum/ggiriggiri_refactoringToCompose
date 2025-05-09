package com.friends.ggiriggiri.screen.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.friends.ggiriggiri.component.BottomNavigationBar
import com.friends.ggiriggiri.screen.ui.home.HomeScreen
import com.friends.ggiriggiri.screen.ui.memories.MemoriesScreen
import com.friends.ggiriggiri.screen.ui.mypage.MyPageScreen
import com.friends.ggiriggiri.screen.viewmodel.UserMainViewModel
import com.friends.ggiriggiri.util.Screen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserMainScreen(
    viewModel: UserMainViewModel = hiltViewModel()
) {

    val navBackStackEntry = viewModel.friendsApplication.navHostController.currentBackStackEntryAsState()

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        containerColor = Color(0xFFFFFFFF)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .padding(0.dp)
                .fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                HomeScreen(Modifier.padding(innerPadding),navBackStackEntry)
            }
            composable(Screen.Memories.route) {
                MemoriesScreen(Modifier.padding(innerPadding))
            }
            composable(Screen.MyPage.route) {
                MyPageScreen(Modifier.padding(innerPadding))
            }
        }
    }
}


@Preview
@Composable
fun viwe(){
    UserMainScreen()
}