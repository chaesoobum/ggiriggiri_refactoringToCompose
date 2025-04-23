package com.friends.ggiriggiri.screen.viewmodel.memories

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.service.MemoriesService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoriesViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val memoriesService: MemoriesService
) : ViewModel() {
    val friendsApplication = context as FriendsApplication


    //처음로딩될때 스캘레톤을 띄우기위한 변수
    private val _isLoading = mutableStateOf(true) // 기본값 true: 처음 들어왔을 땐 로딩 중
    val isLoading: State<Boolean> = _isLoading

    // 요청들 리스트를 갱신할 변수와 함수
    private var _listForRequestsListScreen = mutableStateOf<List<List<String>>>(emptyList())
    val listForRequestsListScreen: State<List<List<String>>> = _listForRequestsListScreen
    fun takeInformationForRequestsListScreen() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000) //스켈래톤을 보기위한 임시딜레이
            _listForRequestsListScreen.value = memoriesService.takeInformationForRequestsListScreen()
            _isLoading.value = false
        }
    }
}