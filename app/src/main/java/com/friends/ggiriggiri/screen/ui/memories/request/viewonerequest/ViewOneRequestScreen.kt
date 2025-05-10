package com.friends.ggiriggiri.screen.ui.memories.request.viewonerequest

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.screen.viewmodel.memories.ViewOneRequestViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewOneRequestScreen(
    navHostController: NavHostController,
    viewModel: ViewOneRequestViewModel = hiltViewModel(),
    requestDocumentId: String
) {
    //Log.d("requestDocumentId",requestDocumentId)
    ViewOneRequestContent(viewModel, requestDocumentId)
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewOneRequestContent(
    viewModel: ViewOneRequestViewModel,
    requestDocumentId: String
) {
    LaunchedEffect(Unit) {
        viewModel.getRequest(requestDocumentId)
    }
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    viewModel.friendsApplication.navHostController.apply {
                        popBackStack()
                    }
                }
            )
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            //  if (viewModel.isRequestInfoLoading.value == false) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    RequestItem(
                        modifierItem = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(15.dp))
                            .background(colorResource(id = R.color.mainColor)),
                        viewModel = viewModel,
                        requestComponent = true
                    )

                    for(i in 0 ..< viewModel.responsesMapList.value.size) {
                        RequestItem(
                            modifierItem = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
                                .clip(RoundedCornerShape(15.dp))
                                .background(colorResource(id = R.color.white)),
                            viewModel = viewModel,
                            requestComponent = false,
                            responseIndex = i
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            //}
            // 로딩 중이면 다이얼로그 보여주기
            //CustomProgressDialog(isShowing = viewModel.isRequestInfoLoading.value)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewViewOneRequestContent() {
    //ViewOneRequestContent()
}