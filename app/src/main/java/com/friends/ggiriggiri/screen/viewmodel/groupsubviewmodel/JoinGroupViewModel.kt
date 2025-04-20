package com.friends.ggiriggiri.screen.viewmodel.groupsubviewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinGroupViewModel@Inject constructor(
    @ApplicationContext context: Context,
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

    val showSuccessDialog = mutableStateOf(false)

    fun joinGroupProcessLoading(){
        viewModelScope.launch {
            isLoading.value = true

            delay(4000)

            if (textFieldJoinGroupCodeValue.value == "csb" && textFieldJoinGroupPasswordValue.value == "1234"){
                isGroupExist.value = true
            }else{
                isGroupExist.value = false
                showSuccessDialog.value = true
            }

            isLoading.value = false
        }
    }
}