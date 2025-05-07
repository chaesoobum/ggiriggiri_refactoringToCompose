package com.friends.ggiriggiri.screen.viewmodel.mypage

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.service.MyPageService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingGroupViewModel@Inject constructor(
    @ApplicationContext context: Context,
    val myPageService: MyPageService
): ViewModel() {
    val friendsApplication = context as FriendsApplication
    //원래그룹이름
    val groupName = mutableStateOf("")

    //그룹명변경
    private val _isChangeGroupNameLoading = mutableStateOf(false)
    val isChangeGroupNameLoading: State<Boolean> = _isChangeGroupNameLoading
    val changeGroupNameValue = mutableStateOf("")


    val isShowFailDialogSame = mutableStateOf(false)
    val isShowFailDialogEmpty = mutableStateOf(false)
    val isConfirmDialog = mutableStateOf(false)

    fun changeGroupNameValid() {
        if (changeGroupNameValue.value == groupName.value) {
            isShowFailDialogSame.value = true
            //같음
            return
        }
        if (changeGroupNameValue.value == "") {
            isShowFailDialogEmpty.value = true
            //공백x
            return
        }

        isConfirmDialog.value = true
    }

    fun changeGroupNameProcessing(context: Context,newGroupName: String) {
        viewModelScope.launch {
            _isChangeGroupNameLoading.value = true
            myPageService.changeGroupName(
                newGroupName,
                friendsApplication.loginUserModel.userGroupDocumentID,
                onSuccess = {
                    Toast.makeText(context,"그룹명 변경 완료!", Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    Toast.makeText(context,"그룹명 변경 실패", Toast.LENGTH_SHORT).show()
                }
            )
            friendsApplication.navHostController.popBackStack()
            _isChangeGroupNameLoading.value = false
        }
    }
}