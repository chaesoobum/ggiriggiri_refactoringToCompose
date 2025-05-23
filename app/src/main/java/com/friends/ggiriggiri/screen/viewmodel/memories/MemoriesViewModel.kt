package com.friends.ggiriggiri.screen.viewmodel.memories

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.model.QuestionListModel
import com.friends.ggiriggiri.firebase.service.MemoriesService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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

    //오늘의 질문 리스트를 갱신할 변수와 함수
    private var _listForQuestionsListScreen = mutableStateOf<List<List<String>>>(emptyList())
    val listForQuestionsListScreen: State<List<List<String>>> = _listForQuestionsListScreen


    fun getListInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            val requestsList = async (Dispatchers.IO){
                delay(5000)
                _listForRequestsListScreen.value = memoriesService.getRequestInfoWithGroupDocumentID(
                    friendsApplication.loginUserModel.userGroupDocumentID
                )
            }
            val questionsList = async (Dispatchers.IO){
                memoriesService.getQuestionsInfoWithGroupName(
                    friendsApplication.loginUserModel.userGroupDocumentID
                )
            }
            requestsList.join()
            _listForQuestionsListScreen.value = questionsList.await()
            _listForQuestionsListScreen.value = _listForQuestionsListScreen.value.sortedBy { it[0].toInt() }
            _isLoading.value = false
        }
    }
}