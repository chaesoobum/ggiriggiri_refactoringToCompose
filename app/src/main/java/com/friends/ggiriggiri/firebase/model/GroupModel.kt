package com.friends.ggiriggiri.firebase.model

import com.friends.ggiriggiri.firebase.vo.GroupVO
import com.friends.ggiriggiri.util.GroupState

data class GroupModel (
    // 그룹 문서 Id
    var groupDocumentId: String = "",
    // 그룹 생성시간
    var groupCreateTime: Long = 0L,
    // 그룹 상태
    var groupState: GroupState = GroupState.ACTIVE,// 1: 활성화, 2: 비활성화 (기본값: 1)
    // 그룹 이름
    var groupName: String = "",
    // 그룹 코드
    var groupCode: String = "",
    // 그룹 비밀번호
    var groupPw: String = "",
    // 유저리스트 DocumnetID
    var groupUserDocumentID: MutableList<String> = mutableListOf(),
    // 요청() 문서 아이디 리스트
    var groupRequestDocumentID: List<String> = listOf(),
    // 질문() 문서 아이디 리스트
    var groupQuestionDocumentID: List<String> = listOf(),
    // 그룹 갤러리(이미지들)
    var groupGallery: List<String> = listOf(),
    // 그룹내 요청중인 유저가있는가
    var groupIsRequesting: Boolean = false,
    // 그룹이 만들어진지 며칠됐는가
    var groupDayFromCreate: Int? = null
){
    fun toGroupVO() : GroupVO {
        val groupVO = GroupVO()

        groupVO.groupPw = groupPw
        groupVO.groupName = groupName
        groupVO.groupCode = groupCode
        groupVO.groupState = groupState.num
        groupVO.groupCreateTime = groupCreateTime
        groupVO.groupUserDocumentID = groupUserDocumentID
        groupVO.groupRequestDocumentID = groupRequestDocumentID
        groupVO.groupQuestionDocumentID = groupQuestionDocumentID
        groupVO.groupGallery = groupGallery
        groupVO.groupDayFromCreate = groupDayFromCreate

        return groupVO
    }
}