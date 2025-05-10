package com.friends.ggiriggiri.firebase.model

import com.friends.ggiriggiri.firebase.vo.QuestionListVO

data class QuestionListModel(
    val questionColor: String,
    val questionContent: String,
    val questionImg: String,
    val questionNumber: Int
) {
    fun toQuestionListVO(): QuestionListVO {
        val questionListVO = QuestionListVO(
            questionColor = questionColor,
            questionContent = questionContent,
            questionImg = questionImg,
            questionNumber = questionNumber,
            )
        return questionListVO
    }
}