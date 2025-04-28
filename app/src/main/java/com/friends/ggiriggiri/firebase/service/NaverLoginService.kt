package com.friends.ggiriggiri.firebase.service

import android.app.Activity
import com.friends.ggiriggiri.firebase.socialdataclass.NaverUserInfo
import com.friends.ggiriggiri.firebase.repository.NaverLoginRepository
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