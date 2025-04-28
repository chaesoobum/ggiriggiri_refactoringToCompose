package com.friends.ggiriggiri.firebase.service

import com.friends.ggiriggiri.firebase.model.UserModel
import com.friends.ggiriggiri.firebase.repository.LoginAndRegisterRepository
import javax.inject.Inject

class LoginAndRegisterService@Inject constructor(
    val loginAndRegisterRepository: LoginAndRegisterRepository
) {
    suspend fun loginOrRegister(userModel: UserModel): UserModel {
        return loginAndRegisterRepository.loginOrRegister(userModel)
    }
    suspend fun getUserModelByDocumentId(userDocumentId: String): UserModel {
        return loginAndRegisterRepository.getUserModelByDocumentId(userDocumentId)
    }
}