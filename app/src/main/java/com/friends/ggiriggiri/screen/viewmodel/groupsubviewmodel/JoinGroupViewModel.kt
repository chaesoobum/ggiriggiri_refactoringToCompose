package com.friends.ggiriggiri.screen.viewmodel.groupsubviewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.friends.ggiriggiri.firebase.service.GroupService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinGroupViewModel@Inject constructor(
    @ApplicationContext context: Context,
    private val preferenceManager: PreferenceManager,
    private val joinGroupService: GroupService
) :ViewModel(){
    val friendsApplication = context as FriendsApplication

    // 그룹코드 입력 요소
    val textFieldJoinGroupCodeValue = mutableStateOf("")

    // 그룹비밀번호 입력 요소
    val textFieldJoinGroupPasswordValue = mutableStateOf("")

    // 프로그래스바
    val isLoading = MutableStateFlow(false) // 로딩 상태

    // 입력한 그룹이 존재하는지
    val isGroupExist =  mutableStateOf(false)

    // 토스트 트리거
    val triggerForToast = mutableStateOf(false)

    fun isFormValid():Boolean{
        if (textFieldJoinGroupCodeValue.value.isBlank() || textFieldJoinGroupPasswordValue.value.isBlank()) {
            triggerForToast.value = true
            return false
        }else{
            return true
        }
    }

    // 그룹 들어가기 버튼
    fun joinGroup(){
        if (isFormValid()){
            joinGroupProcessLoading()
        }
    }

    private val _showFailDialog = mutableStateOf(false)
    val showFailDialog = _showFailDialog
    fun showFailDialogTrue(){
        _showFailDialog.value = true
    }
    fun showFailDialogFalse(){
        _showFailDialog.value = false
    }

    fun joinGroupProcessLoading(){
        viewModelScope.launch {
            isLoading.value = true

            //입력한 정보에 맞는 그룹 모델을 가져온다
            //없으면 null이다
            val groupModel = joinGroupService.getGroupModel(textFieldJoinGroupCodeValue.value,textFieldJoinGroupPasswordValue.value)

            if (groupModel == null){
                isGroupExist.value = false
                showFailDialogTrue()
            }else{
                //그룹에 들어갔으므로 preference를   그룹참여상태로 바꾼다
                preferenceManager.changeIsGroupInTrue()

                // 그룹문서에 유저아이디를 추가하고
                joinGroupService.addUserToGroup(groupModel.groupDocumentId,friendsApplication.loginUserModel.userDocumentId)
                // 유저문서에 그룹아이디를 추가한다
                joinGroupService.updateUserGroupDocumentId(friendsApplication.loginUserModel.userDocumentId,groupModel.groupDocumentId)

                isGroupExist.value = true
            }
            isLoading.value = false
        }
    }
}