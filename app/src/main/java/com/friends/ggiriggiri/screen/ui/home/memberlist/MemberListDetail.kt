package com.friends.ggiriggiri.screen.ui.home.memberlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.viewmodel.PublicViewModel
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.findActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberListDetail(
    viewModel: HomeViewModel = hiltViewModel(),
    navHostController:NavHostController
) {
    val items = remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    val pvm: PublicViewModel = hiltViewModel(LocalContext.current.findActivity())
    //전역뷰모델에서 값을가져오고 삭제함
    LaunchedEffect(Unit) {
        items.value = pvm.memberImageUrls.value
        pvm.deleteMemberImageUrls()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            TopAppBar(
                title = "그룹원",
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    navHostController.popBackStack(MainScreenName.SCREEN_MEMBER_LIST_DETAIL.name, true)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items.value) { item ->
                    MemberListItem(item)
                }
            }
        }
    }
}

