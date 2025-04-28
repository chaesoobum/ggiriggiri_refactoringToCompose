package com.friends.ggiriggiri.service

import android.app.Activity
import com.friends.ggiriggiri.dataclass.KakaoUserInfo
import com.friends.ggiriggiri.dataclass.NaverUserInfo
import com.friends.ggiriggiri.dataclass.model.UserModel
import com.friends.ggiriggiri.repository.KakaoLoginRepository
import com.friends.ggiriggiri.repository.NaverLoginRepository
import javax.inject.Inject

class NaverLoginService @Inject constructor(
    val naverLoginRepository: NaverLoginRepository
){
    fun loginWithNaver(
        activity: Activity,
        onSuccess: (String, NaverUserInfo) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        naverLoginRepository.loginWithNaver(activity, onSuccess, onFailure)
    }
}