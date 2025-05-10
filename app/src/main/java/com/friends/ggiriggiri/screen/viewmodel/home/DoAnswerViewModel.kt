package com.friends.ggiriggiri.screen.viewmodel.home

import android.widget.Toast
import androidx.annotation.UiContext
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.model.AnswerModel
import com.friends.ggiriggiri.firebase.service.DoAnswerService
import com.google.api.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoAnswerViewModel @Inject constructor(
    @ApplicationContext context: android.content.Context,
    var service: DoAnswerService
) : ViewModel() {
    val friendsApplication = context as FriendsApplication

    //실패 다이얼로그
    private var _showFailDialog = mutableStateOf(false)
    var showFailDialog: State<Boolean> = _showFailDialog
    fun changeShowFailDialog(boolean: Boolean) {
        _showFailDialog.value = boolean
    }

    //답변 다이얼로그
    private var _showConfirmDialog = mutableStateOf(false)
    var showConfirmDialog: State<Boolean> = _showConfirmDialog
    fun changeShowConfirmDialog(boolean: Boolean) {
        _showConfirmDialog.value = boolean
    }

    val answerText = mutableStateOf("")

    private val _answerEnd = mutableStateOf(false)
    val answerEnd: State<Boolean> = _answerEnd

    fun saveAnswerValid(){
        if (answerText.value.isEmpty()){
            changeShowFailDialog(true)
        }else{
            changeShowConfirmDialog(true)
        }
    }

    private val _isLoading = mutableStateOf(false)
    val isLoading:State<Boolean> = _isLoading
    fun saveAnswerProcess(groupDayFromCreate: Int){
        viewModelScope.launch {
            _isLoading.value = true
            val answerModel = AnswerModel(
                userDocumentID = friendsApplication.loginUserModel.userDocumentId,
                groupDocumentID = friendsApplication.loginUserModel.userGroupDocumentID,
                answerContent = answerText.value,
                questionNumber = groupDayFromCreate,
                answerResponseTime = System.currentTimeMillis()
            )
            try {
                service.saveAnswerProcess(answerModel)
            }catch (e: Exception){

            }
            Toast.makeText(friendsApplication,"답변을 완료 했습니다", Toast.LENGTH_SHORT).show()
            _isLoading.value = false
            friendsApplication.navHostController.popBackStack()
        }
    }
}
