package com.friends.ggiriggiri.screen.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.service.MemoriesService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoriesViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val memoriesService: MemoriesService
) : ViewModel() {

    // 요청들 리스트를 갱신할 변수와 함수
    private var _listForRequestsListScreen = mutableStateOf<List<List<String>>>(emptyList())
    val listForRequestsListScreen: State<List<List<String>>> = _listForRequestsListScreen
    fun takeInformationForRequestsListScreen() {
        viewModelScope.launch {
            _listForRequestsListScreen.value = memoriesService.takeInformationForRequestsListScreen()
        }
    }
}