package com.friends.ggiriggiri.firebase.vo

import com.friends.ggiriggiri.firebase.model.GroupModel
import com.friends.ggiriggiri.util.GroupState

class GroupVO {
    // 그룹 생성시간
    var groupCreateTime: Long = 0L
    // 그룹 상태
    var groupState = 1 // 1: 활성화, 2: 비활성화 (기본값: 1)
    // 그룹 이름
    var groupName: String = ""
    // 그룹 코드
    var groupCode: String = ""
    // 그룹 비밀번호
    var groupPw: String = ""
    // 유저리스트 DocumnetID
    var groupUserDocumentID: MutableList<String> = mutableListOf()
    // 요청() 문서 아이디 리스트
    var groupRequestDocumentID: List<String> = listOf()
    // 질문() 문서 아이디 리스트
    var groupQuestionDocumentID: List<String> = listOf()
    // 그룹 갤러리(이미지들)
    var groupGallery: List<String> = listOf()
    // 그룹내 요청중인 유저가있는가
    var groupIsRequesting: Boolean = false
    // 그룹이 만들어진지 며칠됐는가
    var groupDayFromCreate: Int? = 1

    fun toGroupModel(groupDocumentId:String) : GroupModel {
        val groupModel = GroupModel()

        groupModel.groupDocumentId = groupDocumentId
        groupModel.groupPw = groupPw
        groupModel.groupName = groupName
        groupModel.groupCode = groupCode

        when(groupState){
            GroupState.ACTIVE.num -> groupModel.groupState = GroupState.ACTIVE
            GroupState.INACTIVE.num -> groupModel.groupState = GroupState.INACTIVE
        }

        groupModel.groupCreateTime = groupCreateTime
        groupModel.groupUserDocumentID = groupUserDocumentID
        groupModel.groupRequestDocumentID = groupRequestDocumentID.toList()
        groupModel.groupQuestionDocumentID = groupQuestionDocumentID.toList()
        groupModel.groupGallery = groupGallery.toList()
        groupModel.groupIsRequesting = groupIsRequesting
        groupModel.groupDayFromCreate = groupDayFromCreate

        return groupModel
    }
}