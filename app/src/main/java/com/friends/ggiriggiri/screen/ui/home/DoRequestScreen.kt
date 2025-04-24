package com.friends.ggiriggiri.screen.ui.home

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.component.OutlinedTextField
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.component.UploadImage
import com.friends.ggiriggiri.screen.viewmodel.home.HomeViewModel
import com.friends.ggiriggiri.util.MainScreenName
import java.io.File
import java.nio.file.WatchEvent

@Composable
fun DoRequestScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    DoRequestContent(viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoRequestContent(
    viewModel: HomeViewModel
) {
    val context = LocalContext.current
    val focusManager: FocusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                // 외부 터치 시 포커스를 해제하고 키보드 내리기
                detectTapGestures(onTap = {
                    focusManager.clearFocus() // 포커스를 해제
                })
            },
        topBar = {
            TopAppBar(
                title = "요청하기",
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    viewModel.friendsApplication.navHostController.apply {
                        popBackStack(MainScreenName.SCREEN_DO_REQUEST.name, true)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            /*
            이미지 업로드
            */
            var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
            UploadImage(
                onImageSelected = { uri ->
                    selectedImageUri = uri
                }
            )
            //Uri를 파일로만든다
            selectedImageUri?.let { uri ->
                //content:// 형태의 Uri를 읽기 위한 InputStream을 얻는 코드
                val inputStream = context.contentResolver.openInputStream(uri)

                //요청보낼 파일이름
                val tempFile = File(context.cacheDir, "uploaded_image.jpg")

                //앱의 캐시 디렉토리에 파일을 새로 만듦
                inputStream.use { input ->
                    tempFile.outputStream().use { output ->
                        input?.copyTo(output)
                    }
                }

                // tempFile = 진짜 사용할 수 있는 파일 객체
                Log.d("사진", tempFile.isFile.toString())
            }

            /*
            요청 텍스트
            */
            OutlinedTextField(
                label = "요청하기",
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                paddingTop = 20.dp
            )
            /*
            요청 버튼
            */
            CustomButton(
                text = "요청하기",
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                paddingTop = 20.dp
            )


        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDoRequestContent() {
    DoRequestScreen()
}