package com.friends.ggiriggiri.service

import com.friends.ggiriggiri.dataclass.model.UserModel
import com.friends.ggiriggiri.repository.LoginAndRegisterRepository
import javax.inject.Inject

class LoginAndRegisterService@Inject constructor(
    val loginAndRegisterRepository: LoginAndRegisterRepository
) {
    suspend fun loginOrRegister(userModel: UserModel): UserModel {
        return loginAndRegisterRepository.loginOrRegister(userModel)
    }
}