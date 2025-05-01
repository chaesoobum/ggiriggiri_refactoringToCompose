package com.friends.ggiriggiri.firebase.service

import com.friends.ggiriggiri.firebase.repository.MyPageRepository
import javax.inject.Inject

class MyPageService@Inject constructor(
    val myPageRepository: MyPageRepository
) {
    suspend fun getProfileImage(userDocumentId: String): String {
        return myPageRepository.getProfileImage(userDocumentId)
    }
}