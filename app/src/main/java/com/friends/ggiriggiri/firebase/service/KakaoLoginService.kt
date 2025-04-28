package com.friends.ggiriggiri.firebase.service

import android.app.Activity
import com.friends.ggiriggiri.firebase.socialdataclass.KakaoUserInfo
import com.friends.ggiriggiri.firebase.repository.KakaoLoginRepository
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