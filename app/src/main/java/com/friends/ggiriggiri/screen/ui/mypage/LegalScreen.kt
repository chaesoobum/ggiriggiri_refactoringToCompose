package com.friends.ggiriggiri.screen.ui.mypage

import android.annotation.SuppressLint
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomAlertDialog
import com.friends.ggiriggiri.component.TopAppBar
import com.friends.ggiriggiri.util.MainScreenName


@Composable
fun LegalScreen(navHostController: NavHostController) {
    LegalContent(navHostController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalContent(navHostController: NavHostController) {
    val context = LocalContext.current

    val showDialog = remember { mutableStateOf(false) }
    val pendingSslHandler = remember { mutableStateOf<SslErrorHandler?>(null) }

    val webView = remember {
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            }
            webViewClient = object : WebViewClient() {
                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    // 다이얼로그 표시 요청
                    pendingSslHandler.value = handler
                    showDialog.value = true
                }
            }
            loadUrl("https://sites.google.com/view/ggiriggiri-information")
        }
    }

    // 다이얼로그 컴포저블
    if (showDialog.value) {
        CustomAlertDialog(
            onDismiss = {
                showDialog.value = false
                pendingSslHandler.value?.cancel()  // 취소 시 로딩 중지
            },
            onConfirmation = {
                showDialog.value = false
                pendingSslHandler.value?.proceed() // 확인 시 계속 진행
            },
            icon = Icons.Default.Info,
            dialogTitle = "SSL 인증서 경고",
            dialogText = "이 페이지는 보안 인증서에 문제가 있습니다.\n그래도 계속 진행하시겠습니까?"
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "개인정보처리방침/이용약관",
                navigationIconImage = ImageVector.vectorResource(id = R.drawable.arrow_back_ios_24px),
                navigationIconOnClick = {
                    navHostController.popBackStack(MainScreenName.SCREEN_LEGAL.name, true)
                },
                isDivider = false
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            AndroidView(
                factory = { webView },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLegalContent() {
    //LegalContent()
}