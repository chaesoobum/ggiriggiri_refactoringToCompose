package com.friends.ggiriggiri.service

import android.app.Activity
import android.content.Context
import com.friends.ggiriggiri.dataclass.KakaoUserInfo
import com.friends.ggiriggiri.repository.KakaoLoginRepository
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject

class KakaoLoginService @Inject constructor(
    val kakaoLoginRepository: KakaoLoginRepository
){
    fun loginWithKakaoTalk(
        activity: Activity,
        onSuccess: (String, KakaoUserInfo) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        kakaoLoginRepository.loginWithKakaoTalk(activity, onSuccess, onFailure)
    }
}