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
                        title = "끼리끼리 요청!",
                        body = requestText.value
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
