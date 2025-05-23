package com.friends.ggiriggiri.screen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.ui.groupsubscreen.JoinGroupScreen
import com.friends.ggiriggiri.screen.ui.groupsubscreen.MakeGroupScreen
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.screen.viewmodel.userlogin.UserLoginViewModel
import com.friends.ggiriggiri.util.Group
import com.friends.ggiriggiri.util.MainScreenName
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserGroupScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    userLoginViewModel: UserLoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val selectedColor = Color(ContextCompat.getColor(context, R.color.mainColor))

    val groupTabs = listOf(Group.MakeGroup, Group.JoinGroup)

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { groupTabs.size }
    )

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                TopAppBar(
                    title = "그룹 만들기/그룹 들어가기",
                    isDivider = false,
                    navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                    navigationIconOnClick = {
                        homeViewModel.friendsApplication.navHostController.apply {

                            //자동로그인 기록을 지운다
                            userLoginViewModel.preferenceManager.clearLoginInfo()

                            //화면을 이동한다
                            popBackStack(MainScreenName.SCREEN_USER_GROUP.name,true)
                            navigate(MainScreenName.SCREEN_USER_LOGIN.name)
                        }
                    },
                )
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                .height(5.dp),
                            color = selectedColor
                        )
                    },
                    containerColor = Color.White,
                ) {
                    groupTabs.forEachIndexed { index, group ->
                        val selected = pagerState.currentPage == index
                        Tab(
                            selected = selected,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(index)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = group.icon),
                                    contentDescription = null,
                                    tint = if (selected) Color(0xFF000000) else Color.Gray
                                )
                            },
                            text = {
                                Text(
                                    text = group.label,
                                    color = if (selected) Color(0xFF000000) else Color.Gray
                                )
                            },
                            selectedContentColor = selectedColor,
                            unselectedContentColor = Color.Gray
                        )
                    }
                }
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) { page ->
            when (groupTabs[page]) {
                is Group.MakeGroup -> MakeGroupScreen()
                is Group.JoinGroup -> JoinGroupScreen()
            }
        }
    }
}




