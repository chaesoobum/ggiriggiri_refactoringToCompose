package com.friends.ggiriggiri.screen.viewmodel.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.model.AnswerModel
import com.friends.ggiriggiri.firebase.model.QuestionListModel
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.service.HomeService
import com.friends.ggiriggiri.firebase.vo.AnswerVO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
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
                //Log.d("setRequestInfo","${_requesterName.value},${_requestMessage.value}")
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
    //private val _remainingTimeFormatted = mutableStateOf("00:00")
    private val _remainingTimeFormatted = mutableStateOf("")
    val remainingTimeFormatted: State<String> = _remainingTimeFormatted
    // 요청 image Url
    private val _requestImageUrl = mutableStateOf<String?>(null)
    val requestImageUrl:State<String?> = _requestImageUrl

    //요청이 있는상태면 응답화면 구성하기
    suspend fun setRequestInfo() {
        _requesterName.value = homeService.getUserName(requestModel.value?.requestUserDocumentID.toString())
        _requestMessage.value = requestModel.value?.requestMessage.toString()
        _requestImageUrl.value = requestModel.value?.requestImage.toString()

        requestModel.value?.requestTime?.let { requestTime ->
            startRequestCountdownTimer(requestTime)
        }
    }


    //요청관련 컴포넌트를 띄울준비가되었는가
    private val _requestState = mutableStateOf<Boolean?>(null)
    val requestState:State<Boolean?> = _requestState

    //내가한 요청인가
    private val _isMyRequest = mutableStateOf<Boolean?>(null)
    val isMyRequest:State<Boolean?> = _isMyRequest

    //내가한 요청이 아니라면 응답을 했는가 안했는가
    private val _isResponse = mutableStateOf<Boolean?>(null)
    val isResponse:State<Boolean?> = _isResponse



    //그룹에 활성화된 요청의 유무에따라 분기한다
    fun classifyRequestState() {
        viewModelScope.launch {
            //그룹에 활성화된 요청이 있다
            if (_requestModel.value != null) {
                //내가한 요청이라면
                if (_requestModel.value!!.requestUserDocumentID == friendsApplication.loginUserModel.userDocumentId) {
                    _isMyRequest.value = true
                } else {
                    _isMyRequest.value = false
                }
                //내가한 요청이 아니고 응답도 안했다면
                val didIResponse = homeService.didIResponse(
                    _requestModel.value!!.requestDocumentId,
                    friendsApplication.loginUserModel.userDocumentId
                )
                Log.d("classfy",didIResponse.toString())
                //응답한 요청임
                if (didIResponse) {
                    _isResponse.value = true
                } else { //응답안한요청임
                    _isResponse.value = false
                }
                _requestState.value = true

            }
            //그룹에 활성화된 요청이 없다
            else {
                _requestState.value = false
            }
        }
    }

    // 시간이 다됐을때 컴포넌트를 바꾸기위한 함수
    private val _isTimeOver = mutableStateOf(false)
    val isTimeOver:State<Boolean> = _isTimeOver

    //타이머 함수
    private var timerJob: Job? = null
    fun startRequestCountdownTimer(requestTime: Long) {
        timerJob?.cancel() // 기존 타이머 중지
        timerJob = viewModelScope.launch {
            while (true) {
                val thirtyMinutesLater = requestTime + (30 * 60 * 1000) // 요청 생성 + 30분
                val now = System.currentTimeMillis()
                val remainingMillis = thirtyMinutesLater - now

                if (remainingMillis <= 0) {
                    _remainingTimeFormatted.value = "00:00"
                    _requestState.value = false
                    break
                }

                val minutes = remainingMillis / (60 * 1000)
                val seconds = (remainingMillis % (60 * 1000)) / 1000
                _remainingTimeFormatted.value = String.format("%02d:%02d", minutes, seconds)

                delay(1000)
            }
        }
    }

    //요청관련 변수들 초기화
    fun clearHomeState() {
        viewModelScope.launch {
            _requestModel.value = null
            _requestState.value = null
            _isMyRequest.value = null
            _isResponse.value = null
            _requesterName.value = ""
            _requestMessage.value = ""
            _remainingTimeFormatted.value = "00:00"
            _requestImageUrl.value = null
            timerJob?.cancel()
        }
    }





    //오늘의 질문 이미지 가져오기 모델가져오기
    private var _questionImageUrl = mutableStateOf<String>("")
    val questionImageUrl: State<String> = _questionImageUrl

    private val _questionModel = mutableStateOf<QuestionListModel?>(null)
    val questionModel:State<QuestionListModel?> = _questionModel

    //오늘의 질문 이미지 가져오기 로딩상태
    private val _isLoadingForGetQuestionImageUrl = mutableStateOf(true)
    val isLoadingForGetQuestionImageUrl: State<Boolean> = _isLoadingForGetQuestionImageUrl

    //그날 그룹에 해당하는 질문가져오기
    fun getQuestionModel() {
        viewModelScope.launch {

            _questionModel.value = homeService.getQuestionModel(friendsApplication.loginUserModel.userGroupDocumentID)

            _questionImageUrl.value = questionModel.value!!.questionImg

            _isLoadingForGetQuestionImageUrl.value = false
        }
    }

    private val _userAnswerModel = mutableStateOf<AnswerModel?>(null)
    val userAnswerModel:State<AnswerModel?> = _userAnswerModel

    private val _getAnswerLoading = mutableStateOf(true)
    val getAnswerLoading: State<Boolean> = _getAnswerLoading

    //해당 유저가 오늘의 질문에 답했는지 가져오기
    fun getUserAnswerState(){
        viewModelScope.launch {
            _userAnswerModel.value = homeService.getUserAnswer(
                friendsApplication.loginUserModel.userDocumentId,
                friendsApplication.loginUserModel.userGroupDocumentID
            )

            if (_userAnswerModel.value == null){
                Log.d("getUserAnswerState","null")
            }else{
                Log.d("getUserAnswerState","not-null")
            }
            _getAnswerLoading.value = false
        }

    }

    //오늘의 질문 관련 변수들 초기화
    fun clearAnswerState() {
        viewModelScope.launch {
            _userAnswerModel.value = null
            _getAnswerLoading.value = true
        }
    }

}