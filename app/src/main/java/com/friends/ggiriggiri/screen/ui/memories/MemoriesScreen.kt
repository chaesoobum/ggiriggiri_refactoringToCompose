package com.friends.ggiriggiri.screen.ui.memories

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.util.Memories
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun MemoriesScreen(modifier: Modifier) {
    MemoriesContent(
        modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MemoriesContent(
    modifier: Modifier,
) {
    val context = LocalContext.current
    val selectedColor = Color(ContextCompat.getColor(context, R.color.mainColor))

    val memoriesTabs = listOf(Memories.Requests, Memories.Answers)

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { memoriesTabs.size }
    )

    val coroutineScope = rememberCoroutineScope()

    var isRefreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                delay(1000)
                isRefreshing = false
            }
        }
    )

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                .height(5.dp),
                            color = selectedColor
                        )
                    }
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
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                when (memoriesTabs[page]) {
                    Memories.Answers -> AnswersListScreen(modifier)
                    Memories.Requests -> RequestsListScreen(modifier)
                }
            }

            // 새로고침 인디케이터
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMemoriesScreen() {
    MemoriesContent(modifier = Modifier)
}