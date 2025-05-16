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
import com.friends.ggiriggiri.firebase.model.ResponseModel
import com.friends.ggiriggiri.firebase.service.ResponseService
import com.friends.ggiriggiri.util.NotificationCategory
import com.friends.ggiriggiri.util.tools.sendPushNotificationToGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoResponseViewModel@Inject constructor(
    @ApplicationContext context: Context,
    val doResponseService: ResponseService
): ViewModel() {
    val friendsApplication = context as FriendsApplication

    //이미지Uri
    val responseImage = mutableStateOf<Uri?>(null)
    //응답 텍스트
    val responseText = mutableStateOf("")

    //요청모델
    val requestModel = mutableStateOf<RequestModel?>(null)
    //요청유저이름
    val requesterName = mutableStateOf("")

    //실패 다이얼로그
    private var _showFailDialog = mutableStateOf(false)
    var showFailDialog: State<Boolean> = _showFailDialog
    fun changeShowFailDialog(boolean: Boolean){
        _showFailDialog.value = boolean
    }

    //응답 다이얼로그
    private var _showConfirmDialog = mutableStateOf(false)
    var showConfirmDialog: State<Boolean> = _showConfirmDialog
    fun changeShowConfirmDialog(boolean: Boolean){
        _showConfirmDialog.value = boolean
    }

    //입력검사
    fun responseValid(){
        if (responseText.value.isNotBlank() && responseImage.value != null){
            changeShowConfirmDialog(true)
        }else{
            changeShowFailDialog(true)
        }
    }

    //로딩중 표시
    val isLoading = mutableStateOf(false)
    //업로드 진행률
    val uploadProgress = mutableStateOf(0)

    fun uploadImageToStorage(context: Context) {
        val uri = responseImage.value ?: return

        viewModelScope.launch {
            try {
                isLoading.value = true
                uploadProgress.value = 0

                val downloadUrl = doResponseService.uploadImage(
                    context = context,
                    uri = uri,
                    onProgress = { progress -> uploadProgress.value = progress }
                )

                uploadResponseToFirebase(context,downloadUrl)

                Log.d("Storage", "업로드 성공: $downloadUrl")
            } catch (e: Exception) {
                Log.e("Storage", "업로드 실패", e)
            }
        }
    }

    fun uploadResponseToFirebase(context: Context,downloadUrl:String) {
        viewModelScope.launch {
            try {
                val responseModel = ResponseModel(
                    responseRequestDocumentId = requestModel.value?.requestDocumentId.toString(),
                    responseImage = downloadUrl,
                    responseMessage = responseText.value,
                    responseUserDocumentID = friendsApplication.loginUserModel.userDocumentId
                )
                doResponseService.uploadNewResponse(responseModel)

            }catch (e: Exception){
                Log.e("UploadRequest", "응답 업로드 실패", e)
            }finally {
                Log.d("UploadRequest", "응답 업로드 종료")
                Toast.makeText(context,"응답완료", Toast.LENGTH_SHORT).show()
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
                val groupFCMList = doResponseService.getUserFcmList(
                    friendsApplication.loginUserModel.userGroupDocumentID,
                    friendsApplication.loginUserModel.userDocumentId
                )
                Log.d("FCM", "groupFCMList size: ${groupFCMList.size}")

                if (groupFCMList.isNotEmpty()) {
                    sendPushNotificationToGroup(
                        groupFCMList,
                        title = "${requestModel.value?.requestMessage}에대한 ${friendsApplication.loginUserModel.userName}의 응답 도착!",
                        body = responseText.value,
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


    fun pop(){
        friendsApplication.navHostController.popBackStack()
    }

}