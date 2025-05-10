package com.friends.ggiriggiri.firebase.model

import com.friends.ggiriggiri.firebase.vo.ResponseVO

data class ResponseModel(
    var responseDocumentId:String = "", //응답 문서 아이디
    var responseRequestDocumentId:String = "", //응답에 해당 하는 요청 문서 아이디
    var responseTime: Long = System.currentTimeMillis(), // 응답 생성 시간
    var responseImage: String = "", // 응답 이미지 URL
    var responseMessage: String = "", // 응답 메시지
    var responseUserDocumentID: String = "", // 응답한 사용자 ID
    var requestLikeCount:Int? = null
){
    fun toResponseVO(): ResponseVO{
        val responseVO = ResponseVO()

        responseVO.responseRequestDocumentId = responseRequestDocumentId
        responseVO.responseTime = responseTime
        responseVO.responseImage = responseImage
        responseVO.responseMessage = responseMessage
        responseVO.responseUserDocumentID = responseUserDocumentID
        responseVO.requestLikeCount = requestLikeCount

        return responseVO
    }
}