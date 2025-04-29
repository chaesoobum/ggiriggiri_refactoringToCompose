package com.friends.ggiriggiri.firebase.service

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
}