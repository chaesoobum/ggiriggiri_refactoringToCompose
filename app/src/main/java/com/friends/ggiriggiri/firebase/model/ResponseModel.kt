package com.friends.ggiriggiri.firebase.model

import com.friends.ggiriggiri.util.RequestState

data class ResponseModel(
    val responseTime: Long = System.currentTimeMillis(), // 응답 생성 시간
    val responseState: Int = RequestState.ACTIVE.value, // 응답 상태
    val responseImage: String = "", // 응답 이미지 URL
    val responseMessage: String = "", // 응답 메시지
    val responseUserDocumentID: String = "", // 응답한 사용자 ID
)