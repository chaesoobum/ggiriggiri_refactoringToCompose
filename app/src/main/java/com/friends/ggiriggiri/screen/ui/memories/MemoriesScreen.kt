package com.friends.ggiriggiri.screen.ui.memories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomIconButton
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.ui.memories.question.QuestionListScreen
import com.friends.ggiriggiri.screen.ui.memories.request.RequestListScreen
import com.friends.ggiriggiri.screen.viewmodel.memories.MemoriesViewModel
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.Memories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MemoriesScreen(
    modifier: Modifier,
    navHostController: NavHostController,
    viewModel: MemoriesViewModel = hiltViewModel(),
) {
    // 최초 한 번만 요청/질문 첫 페이지 불러오기
    LaunchedEffect(Unit) {
        viewModel.loadFirstPagesIfNeeded()
    }

    MemoriesContent(
        modifier,
        navHostController,
        viewModel,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MemoriesContent(
    modifier: Modifier,
    navHostController: NavHostController,
    viewModel: MemoriesViewModel,
) {

    val context = LocalContext.current
    val selectedColor = Color(ContextCompat.getColor(context, R.color.mainColor))

    val memoriesTabs = listOf(Memories.Requests, Memories.Answers)

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { memoriesTabs.size }
    )

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .background(Color.White)
            ) {
                TopAppBar(
                    title = "추억들",
                    isDivider = false,
                    menuItems = {
                        CustomIconButton(
                            icon = ImageVector.vectorResource(R.drawable.notifications_24px),
                            iconButtonOnClick = {
                                viewModel.app.navHostController.apply {
                                    navigate(MainScreenName.SCREEN_NOTIFICATION.name)
                                }
                            }
                        )
                    },
                )
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                .height(5.dp),
                            color = selectedColor,
                        )
                    },
                    containerColor = Color.White,
                ) {
                    memoriesTabs.forEachIndexed { index, group ->
                        val selected = pagerState.currentPage == index
                        Tab(
                            selected = selected,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
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
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (memoriesTabs[page]) {
                    Memories.Answers -> QuestionListScreen(
                        viewModel,
                        navHostController
                    )

                    Memories.Requests -> RequestListScreen(
                        viewModel,
                        navHostController
                    )
                }
            }
        }
    }
}


