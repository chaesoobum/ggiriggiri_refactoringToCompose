package com.friends.ggiriggiri.screen.viewmodel.home

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.service.RequestService
import com.friends.ggiriggiri.util.NotificationCategory
import com.friends.ggiriggiri.util.tools.sendPushNotification
import com.friends.ggiriggiri.util.tools.sendPushNotificationToGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoRequestViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val requestService: RequestService
) : ViewModel() {
    //app객체
    val friendsApplication = context as FriendsApplication

    //이미지Uri
    val requestImage = mutableStateOf<Uri?>(null)

    //요청 텍스트
    val requestText = mutableStateOf("")

    //실패 다이얼로그
    private var _showFailDialog = mutableStateOf(false)
    var showFailDialog: State<Boolean> = _showFailDialog
    fun changeShowFailDialog(boolean: Boolean) {
        _showFailDialog.value = boolean
    }

    //테스트용 30개 삽입
    fun test() {
        viewModelScope.launch {
            val testMessages = listOf(
                "오늘 뭐 먹을까?",
                "심심해.. 뭐 하고 놀지?",
                "하루 어땠어?",
                "요즘 재밌는 거 있어?",
                "카페 추천 좀!",
                "헬스 끊을까 말까 고민됨",
                "이번 주말에 뭐 할 거야?",
                "너 요즘 빠진 거 있음?",
                "요즘 드라마 뭐 봐?",
                "갑자기 여행 가고 싶다",
                "오늘 날씨 어때?",
                "배고파.. 야식 추천해줘",
                "지금 시간에 뭐 보면 좋을까?",
                "영화 추천 좀!",
                "오늘 기분 어때?",
                "이번 주 목표 뭐야?",
                "어제 꾼 꿈 진짜 이상했어",
                "카톡 답장 늦으면 뭐해?",
                "좋아하는 계절은?",
                "스트레스 푸는 법 뭐야?",
                "시험 끝나면 뭐 할 거야?",
                "새벽 감성 터지는 중",
                "요즘 최애 음식 뭐야?",
                "친구한테 고마운 점 말해보자",
                "요즘 따라 시간 진짜 빨리 가",
                "요즘 듣는 노래 뭐야?",
                "우리 언제 한번 보자~",
                "주말에 아무것도 안 하고 싶은 날",
                "빨리 여름휴가 왔으면 좋겠다",
                "하루에 가장 행복한 순간은?"
            )

            val userIds = listOf(
                "4WjwMH93q3I1RQG6hx2n",
                "OdUikyWt0LzCoYzlpWB2",
                "uUxeGftIzjjuBUGA4ONY",
                "ub1i8eFM4XsWjjgq0XW0"
            )

            val calendar = java.util.Calendar.getInstance().apply {
                set(2025, 2, 1, 8, 0, 0) // 2025년 3월 1일 오전 8시 (월은 0부터 시작하므로 2가 3월)
            }

            for (i in testMessages.indices) {
                val fakeTime = calendar.timeInMillis

                val requestModel = RequestModel(
                    requestUserDocumentID = userIds[i % userIds.size],
                    requestMessage = testMessages[i],
                    requestImage = "테스트",
                    requestGroupDocumentID = "SeeYBZuWUalPjuZML0yb",
                    requestState = 2,
                    requestTime = fakeTime
                )

                requestService.uploadNewRequest(requestModel)

                // 시간 1일 + 1시간씩 증가
                calendar.add(java.util.Calendar.HOUR_OF_DAY, 26)
            }
        }
    }


    //요청 다이얼로그
    private var _showConfirmDialog = mutableStateOf(false)
    var showConfirmDialog: State<Boolean> = _showConfirmDialog
    fun changeShowConfirmDialog(boolean: Boolean) {
        _showConfirmDialog.value = boolean
    }

    //입력검사
    fun requestValid() {
        if (requestText.value.isNotBlank() && requestImage.value != null) {
            changeShowConfirmDialog(true)
        } else {
            changeShowFailDialog(true)
        }
    }

    //로딩중 표시
    val isLoading = mutableStateOf(false)

    //업로드 진행률
    val uploadProgress = mutableStateOf(0)

    fun uploadImageToStorage(context: Context) {
        val uri = requestImage.value ?: return
        viewModelScope.launch {
            try {
                isLoading.value = true
                uploadProgress.value = 0

                val downloadUrl = requestService.uploadImage(
                    context = context,
                    uri = uri,
                    onProgress = { progress -> uploadProgress.value = progress }
                )

                uploadRequestToFirebase(context, downloadUrl)

                Log.d("Storage", "업로드 성공: $downloadUrl")
            } catch (e: Exception) {
                Log.e("Storage", "업로드 실패", e)
            }
        }
    }


    private val _requestEnd = mutableStateOf(false)
    val requestEnd: State<Boolean> = _requestEnd

    fun uploadRequestToFirebase(context: Context, downloadUrl: String) {
        viewModelScope.launch {
            try {
                val requestModel = RequestModel(
                    requestUserDocumentID = friendsApplication.loginUserModel.userDocumentId,
                    requestMessage = requestText.value,
                    requestImage = downloadUrl,
                    requestGroupDocumentID = friendsApplication.loginUserModel.userGroupDocumentID
                )

                requestService.uploadNewRequest(requestModel)

            } catch (e: Exception) {
                Log.e("UploadRequest", "요청 업로드 실패", e)
            } finally {
                Log.d("UploadRequest", "요청 업로드 종료")
                Toast.makeText(context, "요청완료", Toast.LENGTH_SHORT).show()
                sendNotificationToGroup()
                pop()
                isLoading.value = false
            }
        }
    }

    //그룹원에게 알림을 보낸다
    fun sendNotificationToGroup() {
        viewModelScope.launch {
            try {
                val groupFCMList = requestService.getUserFcmList(
                    friendsApplication.loginUserModel.userGroupDocumentID,
                    friendsApplication.loginUserModel.userDocumentId
                )
                Log.d("FCM", "groupFCMList size: ${groupFCMList.size}")

                if (groupFCMList.isNotEmpty()) {
                    sendPushNotificationToGroup(
                        groupFCMList,
                        title = "${friendsApplication.loginUserModel.userName}의 요청!",
                        body = requestText.value,
                        category = NotificationCategory.REQUEST.str
                    )
                } else {
                    Log.d("FCM", "FCM 리스트가 비어 있음, 알림 전송하지 않음")
                }
            } catch (e: Exception) {
                Log.e("FCM", "알림 전송 중 예외 발생", e)
            }
        }
    }

    // 스크린을 닫는다
    fun pop() {
        friendsApplication.navHostController.popBackStack()
    }

}
