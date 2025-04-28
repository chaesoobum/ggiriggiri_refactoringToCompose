package com.friends.ggiriggiri

import android.app.Application
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.firebase.model.UserModel
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FriendsApplication : Application() {
    // 네비게이션
    lateinit var navHostController: NavHostController

    // 로그인 유저
    lateinit var loginUserModel : UserModel

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(
            this,
            getString(R.string.kakao_native_app_key))

        NaverIdLoginSDK.initialize(
            this,
            getString(R.string.OAUTH_CLIENT_ID),
            getString(R.string.OAUTH_CLIENT_SECRET),
            getString(R.string.OAUTH_CLIENT_NAME)
        )
    }
}