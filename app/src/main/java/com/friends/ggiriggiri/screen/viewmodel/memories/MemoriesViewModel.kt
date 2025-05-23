package com.friends.ggiriggiri.screen.viewmodel.memories

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.model.QuestionInfo
import com.friends.ggiriggiri.firebase.model.RequestInfo
import com.friends.ggiriggiri.firebase.service.MemoriesService
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoriesViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val memoriesService: MemoriesService
) : ViewModel() {

    val app = context as FriendsApplication

    // ------------------ 요청 (RequestInfo) ------------------

    private val _requestList = mutableStateListOf<RequestInfo>()
    val requestList: List<RequestInfo> = _requestList

    private var lastVisibleRequestDoc: DocumentSnapshot? = null
    var isLoadingRequests by mutableStateOf(false)
        private set
    var isEndReachedRequests by mutableStateOf(false)
        private set

    fun loadNextRequestPage(pageSize: Int = 7) {
        if (isLoadingRequests || isEndReachedRequests) return

        viewModelScope.launch {
            isLoadingRequests = true

            val (newItems, lastDoc) = memoriesService.getRequestInfoPage(
                groupDocumentId = app.loginUserModel.userGroupDocumentID,
                lastVisibleDocument = lastVisibleRequestDoc,
                pageSize = pageSize
            )

            // 새로운 데이터 추가
            _requestList.addAll(newItems)
            lastVisibleRequestDoc = lastDoc

            //    가져온 데이터가 pageSize보다 작으면 더 이상 없음
            isEndReachedRequests = newItems.size < pageSize

            isLoadingRequests = false

            Log.d("loadNextRequestPage", "로딩됨: ${_requestList.size}, isEnd: $isEndReachedRequests")
        }
    }

    // ------------------ 질문 (QuestionInfo) ------------------

    private val _questionList = mutableStateListOf<QuestionInfo>()
    val questionList: List<QuestionInfo> = _questionList

    private var nextStartAfterQuestionNumber: Long? = null
    var isLoadingQuestions by mutableStateOf(false)
        private set
    var isEndReachedQuestions by mutableStateOf(false)
        private set

    //그룹 생성날짜와 매핑
    var maxQuestionCount = 0

    fun loadNextQuestionPage(pageSize: Int = 10) {
        if (isLoadingQuestions || isEndReachedQuestions) return

        // 불러올 수 있는 최대 개수 도달 시 바로 return
        if (_questionList.size >= maxQuestionCount) {
            isEndReachedQuestions = true
            return
        }

        viewModelScope.launch {
            isLoadingQuestions = true

            val (newItems, nextStart) = memoriesService.getQuestionListPage(
                startAfterNumber = nextStartAfterQuestionNumber,
                pageSize = pageSize
            )

            val remaining = maxQuestionCount - _questionList.size
            val limitedItems = newItems.take(remaining)

            _questionList.addAll(limitedItems)
            nextStartAfterQuestionNumber = nextStart

            // 종료 처리
            if (_questionList.size >= maxQuestionCount || limitedItems.size < pageSize) {
                isEndReachedQuestions = true
            }

            Log.d(
                "loadNextQuestionPage",
                "로딩됨: ${_questionList.size}, isEnd: $isEndReachedQuestions"
            )

            isLoadingQuestions = false
        }
    }

    // ------------------ 초기 로딩 시 호출 ------------------

    fun loadFirstPagesIfNeeded() {
        viewModelScope.launch {
            //maxQuestionCount를 먼저 설정한다
            val setMaxQuestionCount = async(Dispatchers.IO) {
                maxQuestionCount = memoriesService.getGroupDayFromCreate(app.loginUserModel.userGroupDocumentID)
                        .toInt()
            }
            setMaxQuestionCount.join()
            if (_requestList.isEmpty()) loadNextRequestPage()
            if (_questionList.isEmpty()) loadNextQuestionPage()
        }
    }
}