package com.friends.ggiriggiri.firebase.model

data class AnswerDisplayModel(
    val userDocumentID: String,
    val answerContent: String,
    val answerResponseTime: Long,
    val userName: String,
    val userProfileImage: String
)