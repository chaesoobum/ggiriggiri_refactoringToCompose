package com.friends.ggiriggiri.firebase.service


import com.friends.ggiriggiri.firebase.model.GroupModel
import com.friends.ggiriggiri.firebase.repository.GroupRepository
import javax.inject.Inject

class GroupService @Inject constructor(
    val groupRepository: GroupRepository
) {
    suspend fun getGroupModel(groupCode: String, groupPw: String): GroupModel? {
        return groupRepository.getGroupModel(groupCode, groupPw)
    }

    suspend fun addUserToGroup(groupDocumentId: String, userDocumentId: String){
        groupRepository.addUserToGroup(groupDocumentId,userDocumentId)
    }

    suspend fun updateUserGroupDocumentId(userDocumentId: String, groupDocumentId: String) {
        groupRepository.updateUserGroupDocumentId(userDocumentId,groupDocumentId)
    }

    suspend fun removeUserFromGroup(groupDocumentId: String, userDocumentId: String) {
        groupRepository.removeUserFromGroup(groupDocumentId,userDocumentId)
    }

    suspend fun removeGroupDocumentIdFromUser(userDocumentId: String) {
        groupRepository.removeGroupDocumentIdFromUser(userDocumentId)
    }

    suspend fun checkDuplicationGroupCode(groupCode: String): Boolean {
        return groupRepository.checkDuplicationGroupCode(groupCode)
    }

    suspend fun makeGroup(groupModel: GroupModel): GroupModel {
        return groupRepository.makeGroup(groupModel)
    }
}