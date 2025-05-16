package com.friends.ggiriggiri.firebase.service

import com.friends.ggiriggiri.firebase.model.AnswerDisplayModel
import com.friends.ggiriggiri.firebase.model.QuestionListModel
import com.friends.ggiriggiri.firebase.repository.ViewOneQuestionRepository
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class ViewOneQuestionService@Inject constructor(
    val viewOneQuestionRepository: ViewOneQuestionRepository
) {
    // 오늘의 질문정보를 가져옴
    suspend fun getQuestionListModel(questionNumber:String):QuestionListModel{
        return viewOneQuestionRepository.getQuestionListModel(questionNumber)
    }
    //오늘의 질문에 대한 답변을 가져옴
    suspend fun getAnswersDisplayList(
        questionNumber: String,
        groupDocumentID: String
    ): List<AnswerDisplayModel>{
        return viewOneQuestionRepository.getAnswersDisplayList(questionNumber,groupDocumentID)
    }


}