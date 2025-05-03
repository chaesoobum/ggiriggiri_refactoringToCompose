package com.friends.ggiriggiri.firebase.service

import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.repository.HomeRepository
import javax.inject.Inject

class HomeService@Inject constructor(
    val homeRepository: HomeRepository
) {
    //그룹원들의 이미지들을 가져오는 함수
    suspend fun gettingUserProfileImage(groupDocumentId: String): List<String> {
        return homeRepository.gettingUserProfileImage(groupDocumentId)
    }

    //그룹명을 가져온다
    suspend fun gettingGroupName(groupDocumentId: String): String {
        return homeRepository.gettingGroupName(groupDocumentId)
    }

    //그룹에 요청이있는지 가져온다
    suspend fun getActiveRequestInGroup(groupDocumentId: String): RequestModel? {
        return homeRepository.getActiveRequestInGroup(groupDocumentId)
    }

    //유저아이디로 유저이름을 가져온다
    suspend fun getUserName(userDocumentId: String): String {
        return homeRepository.getUserName(userDocumentId)
    }

    //요청에 내가 응답을 했는지 안했는지
    suspend fun didIResponse(requestDocumentId: String, userDocumentId: String): Boolean {
        return homeRepository.didIResponse(requestDocumentId,userDocumentId)
    }
}
