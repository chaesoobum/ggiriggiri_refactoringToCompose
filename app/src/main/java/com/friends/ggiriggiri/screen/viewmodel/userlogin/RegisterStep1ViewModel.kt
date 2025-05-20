package com.friends.ggiriggiri.screen.viewmodel.userlogin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.screen.viewmodel.userlogin.UserLoginViewModel.LoginNavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterStep1ViewModel @Inject constructor(): ViewModel() {

    // 닉네임 입력요소
    val nicknameText = mutableStateOf<String>("")

    // 실패다이얼로그
    val showLoginFailDialog = mutableStateOf(false)

    val navigateToNextStep = MutableSharedFlow<String>()

    fun checkNicknameText() {
        val nickname = nicknameText.value
        if (nickname.length < 2 || nickname.length > 10) {
            showLoginFailDialog.value = true
            return
        }
        viewModelScope.launch {
            navigateToNextStep.emit(nickname)
        }
    }




}