package com.friends.ggiriggiri.screen.viewmodel.groupsubviewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.friends.ggiriggiri.firebase.service.GroupService
import com.friends.ggiriggiri.firebase.service.LoginAndRegisterService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinGroupViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val joinGroupService: GroupService,
) : ViewModel() {
    val friendsApplication = context as FriendsApplication

    val textFieldJoinGroupCodeValue = mutableStateOf("")
    val textFieldJoinGroupPasswordValue = mutableStateOf("")
    val isLoading = MutableStateFlow(false)
    val triggerForToast = mutableStateOf(false)

    private val _navigateToMain = mutableStateOf(false)
    val navigateToMain: MutableState<Boolean> = _navigateToMain

    private val _showFailDialog = mutableStateOf(false)
    val showFailDialog: State<Boolean> = _showFailDialog

    fun isFormValid(): Boolean {
        if (textFieldJoinGroupCodeValue.value.isBlank() || textFieldJoinGroupPasswordValue.value.isBlank()) {
            triggerForToast.value = true
            return false
        }
        return true
    }

    fun joinGroup() {
        if (isFormValid()) {
            joinGroupProcessLoading()
        }
    }

    fun showFailDialogTrue() { _showFailDialog.value = true }
    fun showFailDialogFalse() { _showFailDialog.value = false }

    private fun joinGroupProcessLoading() {
        viewModelScope.launch {
            isLoading.value = true

            val groupModel = joinGroupService.getGroupModel(
                textFieldJoinGroupCodeValue.value,
                textFieldJoinGroupPasswordValue.value
            )

            if (groupModel == null) {
                showFailDialogTrue()
            } else {

                // 그룹 문서에 유저 아이디 추가
                joinGroupService.addUserToGroup(groupModel.groupDocumentId, friendsApplication.loginUserModel.userDocumentId)
                // 유저 문서에 그룹 아이디 저장
                joinGroupService.updateUserGroupDocumentId(friendsApplication.loginUserModel.userDocumentId, groupModel.groupDocumentId)

                // 메모리의 loginUserModel 업데이트
                friendsApplication.loginUserModel.userGroupDocumentID = groupModel.groupDocumentId

                // 성공했으니 Main으로 이동 트리거
                _navigateToMain.value = true
            }
            isLoading.value = false
        }
    }
}
