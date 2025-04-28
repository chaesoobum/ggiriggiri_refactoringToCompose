package com.friends.ggiriggiri.service


import com.friends.ggiriggiri.dataclass.model.GroupModel
import com.friends.ggiriggiri.repository.JoinGroupRepository
import javax.inject.Inject

class JoinGroupService @Inject constructor(
    val joinGroupRepository: JoinGroupRepository
) {
    suspend fun getGroupModel(groupCode: String, groupPw: String): GroupModel? {
        return joinGroupRepository.getGroupModel(groupCode, groupPw)
    }

    suspend fun addUserToGroup(groupDocumentId: String, userDocumentId: String){
        joinGroupRepository.addUserToGroup(groupDocumentId,userDocumentId)
    }

    suspend fun updateUserGroupDocumentId(userDocumentId: String, groupDocumentId: String) {
        joinGroupRepository.updateUserGroupDocumentId(userDocumentId,userDocumentId)
    }
}