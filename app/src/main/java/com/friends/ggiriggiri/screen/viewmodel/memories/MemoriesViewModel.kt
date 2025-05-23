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

    // 처음 한 번만 로드 여부 체크
    // 변수의 읽기는 외부에서 허용하지만, 쓰기(수정)는 내부에서만 가능
    var hasLoadedOnce = false
    private set


    suspend fun getListInfo(forceReload: Boolean = false) {
        // 이미 불러왔고 강제 새로고침이 아니면 무시
        if (hasLoadedOnce && !forceReload) return

        _isLoading.value = true
        val requestsList = memoriesService.getRequestInfoWithGroupDocumentID(
            friendsApplication.loginUserModel.userGroupDocumentID
        )
        val questionsList = memoriesService.getQuestionsInfoWithGroupName(
            friendsApplication.loginUserModel.userGroupDocumentID
        )

        _listForRequestsListScreen.value = requestsList
        _listForQuestionsListScreen.value = questionsList.sortedBy { it[0].toInt() }

        _isLoading.value = false
        hasLoadedOnce = true // 최초 로드 완료 표시
    }
}