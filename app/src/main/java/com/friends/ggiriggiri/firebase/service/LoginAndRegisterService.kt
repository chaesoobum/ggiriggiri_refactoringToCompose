package com.friends.ggiriggiri.firebase.service

import android.app.Activity
import com.friends.ggiriggiri.firebase.model.UserModel
import com.friends.ggiriggiri.firebase.repository.LoginAndRegisterRepository
import com.friends.ggiriggiri.firebase.socialdataclass.KakaoUserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class LoginAndRegisterService @Inject constructor(
    val loginAndRegisterRepository: LoginAndRegisterRepository
) {
    suspend fun register(userModel: UserModel): UserModel? {
        return loginAndRegisterRepository.register(userModel)
    }

    //유저가있으면 로그인하고 없으면 유저에게 알린다
    suspend fun userExistCheck(userModel: UserModel): UserModel? {
        return loginAndRegisterRepository.userExistCheck(userModel)
    }

    suspend fun getUserModelByDocumentId(userDocumentId: String): UserModel {
        return loginAndRegisterRepository.getUserModelByDocumentId(userDocumentId)
    }
}