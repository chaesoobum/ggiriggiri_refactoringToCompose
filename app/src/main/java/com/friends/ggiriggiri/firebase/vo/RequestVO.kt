package com.friends.ggiriggiri.firebase.vo

import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.model.ResponseModel
import com.friends.ggiriggiri.util.RequestState

data class RequestVO(
    var requestTime: Long = System.currentTimeMillis(),
    var requestState: Int = 1, // 1: 활성화, 2: 비활성화 (기본값: 1)
    var requestUserDocumentID: String = "",
    var requestMessage: String = "",
    var requestImage: String = "",
    var responseList: List<ResponseModel> = emptyList(),
    var requestGroupDocumentID: String = "",
    var scheduledUpdate: Long? = System.currentTimeMillis() + (30 * 60 * 1000)
){
    fun toRequestModel(requestDocumentId: String): RequestModel {
        return RequestModel(
            requestDocumentId = requestDocumentId,
            requestTime = this.requestTime,
            requestState = RequestState.fromValue(this.requestState).value,
            requestUserDocumentID = this.requestUserDocumentID,
            requestMessage = this.requestMessage,
            requestImage = this.requestImage,
            responseList = this.responseList,
            requestGroupDocumentID = this.requestGroupDocumentID,
            scheduledUpdate = this.scheduledUpdate
        )
    }
}