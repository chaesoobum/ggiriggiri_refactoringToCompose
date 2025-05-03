package com.friends.ggiriggiri.firebase.vo

import com.friends.ggiriggiri.firebase.model.ResponseModel

data class ResponseVO (
    var responseRequestDocumentId:String = "", //응답에 해당 하는 요청 문서 아이디
    var responseTime: Long = 0L, // 응답 생성 시간
    var responseImage: String = "", // 응답 이미지 URL
    var responseMessage: String = "", // 응답 메시지
    var responseUserDocumentID: String = "", // 응답한 사용자 ID
)
{
    fun toResponseModel(responseDocumentId:String): ResponseModel{
        val responseModel = ResponseModel()

        responseModel.responseDocumentId = responseDocumentId
        responseModel.responseRequestDocumentId = responseRequestDocumentId
        responseModel.responseTime = responseTime
        responseModel.responseImage = responseImage
        responseModel.responseMessage = responseMessage
        responseModel.responseUserDocumentID = responseUserDocumentID

        return responseModel
    }
}

