package com.friends.ggiriggiri.firebase.vo

import com.friends.ggiriggiri.firebase.model.QuestionListModel

data class QuestionListVO(
    val questionColor: String = "",
    val questionContent: String = "",
    val questionImg: String = "",
    val questionNumber: Int = 0
) {
    fun toQuestionListModel(): QuestionListModel {
        val questionListModel = QuestionListModel(
            questionColor = questionColor,
            questionContent = questionContent,
            questionImg = questionImg,
            questionNumber = questionNumber,
            )
        return questionListModel
    }
}