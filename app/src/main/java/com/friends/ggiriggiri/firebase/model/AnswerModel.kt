package com.friends.ggiriggiri.firebase.model

import com.friends.ggiriggiri.firebase.vo.AnswerVO


data class AnswerModel (
    val documentID:String ="",
    var userDocumentID: String,
    var groupDocumentID: String,
    var answerContent:String,
    var questionNumber: Int?,
    var answerResponseTime: Long
){
    fun toAnswerVO(): AnswerVO{
        return AnswerVO(
            userDocumentID = this.userDocumentID,
            groupDocumentID = this.groupDocumentID,
            answerContent = this.answerContent,
            questionNumber = this.questionNumber,
            answerResponseTime = this.answerResponseTime
        )
    }
}