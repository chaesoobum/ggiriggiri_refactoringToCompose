package com.friends.ggiriggiri.screen.viewmodel.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.service.HomeService
import com.friends.ggiriggiri.util.tools.minutesAndSeconds
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val homeService: HomeService
) : ViewModel() {
    val friendsApplication = context as FriendsApplication

    // 그룹명 가져오기
    private var _groupTitle = mutableStateOf<String>("")
    val groupTitle: State<String> = _groupTitle
    private var _isTitleLoading = mutableStateOf(true)
    val isTitleLoading: State<Boolean> = _isTitleLoading
    fun getAppBarTitle() {
        viewModelScope.launch {
            _groupTitle.value =
                homeService.gettingGroupName(friendsApplication.loginUserModel.userGroupDocumentID)
            _isTitleLoading.value = false
        }
    }

    //그룹원 프로필 이미지가져오기
    private var _memberImageUrls = mutableStateOf<List<String>>(emptyList())
    val memberImageUrls: State<List<String>> = _memberImageUrls
    fun getMemberProfileImage() {
        viewModelScope.launch {
            _memberImageUrls.value =
                homeService.gettingUserProfileImage(friendsApplication.loginUserModel.userGroupDocumentID)
        }
    }

    //그룹이미지 랜덤 가져오기
    private var _groupImageUrls = mutableStateOf<List<String>>(emptyList())
    val groupImageUrls: State<List<String>> = _groupImageUrls
    fun getImageCarousel() {
        viewModelScope.launch {
            delay(500)
            _groupImageUrls.value = listOf(
                "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
                "https://picsum.photos/id/1016/400/600",
                "https://not-a-real-url/image.jpg", // 실패 테스트
                "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
            )
        }
    }

    //오늘의 질문 이미지 가져오기
    private var _questionImageUrl = mutableStateOf<String>("")
    val questionImageUrl: State<String> = _questionImageUrl

    fun getQuestionImageUrl() {
        viewModelScope.launch {
            delay(500)
            _questionImageUrl.value =
                "https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Confounded%20Face.png"
        }
    }

    private val _requestModel = mutableStateOf<RequestModel?>(null)
    val requestModel: State<RequestModel?> = _requestModel

    //그룹에 활성화된 요청이 있는지 가져오기
    fun getRequestStateInGroup() {
        viewModelScope.launch {
            _requestModel.value =
                homeService.getActiveRequestInGroup(friendsApplication.loginUserModel.userGroupDocumentID)
            //활성화된 요청이 있다면
            if (_requestModel.value != null){
                setRequestInfo()
                Log.d("setRequestInfo","${_requesterName.value},${_requestMessage.value}")
            }
            classifyRequestState()
        }
    }

    // 요청자 이름
    private val _requesterName = mutableStateOf("")
    val requesterName:State<String> = _requesterName
    // 요청 메세지
    private val _requestMessage = mutableStateOf("")
    val requestMessage:State<String> = _requestMessage
    // 남은 시간
    private val _remainingTimeMillis = mutableStateOf<List<Int>>(emptyList())
    val remainingTimeMillis:State<List<Int>> = _remainingTimeMillis

    //요청이 있는상태면 응답화면 구성하기
    suspend fun setRequestInfo(){
        _requesterName.value = homeService.getUserName(requestModel.value?.requestUserDocumentID.toString())
        _requestMessage.value = requestModel.value?.requestMessage.toString()
        requestModel.value?.requestTime?.let { requestTime ->
            val (min, sec) = minutesAndSeconds(requestTime)
            _remainingTimeMillis.value = listOf(min.toInt(), sec.toInt())
        }
    }

    //요청관련 컴포넌트를 띄울준비가되었는가
    private val _requestState = mutableStateOf<Boolean?>(null)
    val requestState:State<Boolean?> = _requestState
    //그룹에 활성화된 요청의 유무에따라 분기한다
    fun classifyRequestState() {
        //그룹에 활성화된 요청이 있다
        if (_requestModel.value != null) {
            _requestState.value = true
        }
        //그룹에 활성화된 요청이 없다
        else {
            _requestState.value = false
        }
    }


}