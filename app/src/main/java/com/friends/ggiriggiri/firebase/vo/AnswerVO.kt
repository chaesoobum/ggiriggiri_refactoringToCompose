package com.friends.ggiriggiri.firebase.vo

import com.friends.ggiriggiri.firebase.model.AnswerModel


data class AnswerVO (
    var userDocumentID:String = "",
    var groupDocumentID: String = "",
    var answerContent:String = "",
    var questionNumber: Int? = null,
    var answerResponseTime: Long = System.currentTimeMillis()
){
    fun toAnswerModel(documentID: String): AnswerModel{
        return AnswerModel(
            documentID = documentID,
            userDocumentID = this.userDocumentID,
            groupDocumentID = this.groupDocumentID,
            answerContent = this.answerContent,
            questionNumber = this.questionNumber,
            answerResponseTime = this.answerResponseTime
        )
    }

}