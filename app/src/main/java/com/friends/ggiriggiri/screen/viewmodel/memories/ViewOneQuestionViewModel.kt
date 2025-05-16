package com.friends.ggiriggiri.screen.viewmodel.memories

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.model.AnswerDisplayModel
import com.friends.ggiriggiri.firebase.model.AnswerModel
import com.friends.ggiriggiri.firebase.model.QuestionListModel
import com.friends.ggiriggiri.firebase.service.ViewOneQuestionService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewOneQuestionViewModel@Inject constructor(
    @ApplicationContext context: Context,
    val viewOneQuestionService: ViewOneQuestionService
): ViewModel() {
    val friendsApplication = context as FriendsApplication

    private val _questionListModel = mutableStateOf<QuestionListModel?>(null)
    val questionListModel: State<QuestionListModel?> = _questionListModel

    private val _answerDisplayModelList = mutableStateOf<List<AnswerDisplayModel?>>(emptyList())
    val answerDisplayModelList: State<List<AnswerDisplayModel?>> = _answerDisplayModelList

    //하나의 오늘의 질문과 그에대한 그룹원들의 답변을 가져온다
    fun getOneQuestionAndAnswers(questionNumber:String){
        viewModelScope.launch {
            val groupDocumentID = friendsApplication.loginUserModel.userGroupDocumentID
            val getQuestionListModel = async(Dispatchers.IO) {
                viewOneQuestionService.getQuestionListModel(questionNumber)
            }
            val getAnswersList = async(Dispatchers.IO) {
                viewOneQuestionService.getAnswersDisplayList(questionNumber,groupDocumentID)
            }
            _questionListModel.value = getQuestionListModel.await()
            _answerDisplayModelList.value = getAnswersList.await()
        }
    }

}