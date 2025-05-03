package com.friends.ggiriggiri.screen.viewmodel.home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.model.ResponseModel
import com.friends.ggiriggiri.firebase.service.ResponseService
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

                uploadResponseToFirebase(downloadUrl)

                Log.d("Storage", "업로드 성공: $downloadUrl")
            } catch (e: Exception) {
                Log.e("Storage", "업로드 실패", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun uploadResponseToFirebase(downloadUrl:String) {
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
                Log.e("UploadRequest", "요청 업로드 실패", e)
            }finally {
                Log.d("UploadRequest", "요청 업로드 종료")
            }
        }
    }

}