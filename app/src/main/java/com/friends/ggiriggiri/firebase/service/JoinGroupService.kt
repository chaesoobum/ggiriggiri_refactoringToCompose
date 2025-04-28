package com.friends.ggiriggiri.firebase.service


import com.friends.ggiriggiri.firebase.model.GroupModel
import com.friends.ggiriggiri.firebase.repository.JoinGroupRepository
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
        joinGroupRepository.updateUserGroupDocumentId(userDocumentId,groupDocumentId)
    }
}