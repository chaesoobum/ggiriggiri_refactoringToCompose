package com.friends.ggiriggiri.firebase.service

import com.friends.ggiriggiri.firebase.model.AnswerModel
import com.friends.ggiriggiri.firebase.repository.DoAnswerRepository
import javax.inject.Inject

class DoAnswerService@Inject constructor(
    var repository: DoAnswerRepository
) {
    //오늘의 질문 답변을 저장한다
    suspend fun saveAnswerProcess(answerModel: AnswerModel){
        repository.saveAnswerProcess(answerModel)
    }
}