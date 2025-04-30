package com.friends.ggiriggiri.firebase.model

import com.friends.ggiriggiri.firebase.vo.RequestVO
import com.friends.ggiriggiri.util.RequestState

data class RequestModel(
    var requestDocumentId: String = "",
    var requestTime: Long = System.currentTimeMillis(),
    var requestState: Int = RequestState.ACTIVE.value,
    var requestUserDocumentID: String = "",
    var requestMessage: String = "",
    var requestImage: String = "",
    var responseList: List<ResponseModel> = emptyList(),
    var requestGroupDocumentID: String = "",
    var scheduledUpdate: Long? = System.currentTimeMillis() + (30 * 60 * 1000)
){
    fun toRequestVO(): RequestVO{
        var requestVO = RequestVO()

        requestVO.requestTime = requestTime
        requestVO.requestState = requestState
        requestVO.requestUserDocumentID= requestUserDocumentID
        requestVO.requestMessage =requestMessage
        requestVO.requestImage = requestImage
        requestVO.responseList =responseList
        requestVO.requestGroupDocumentID = requestGroupDocumentID
        requestVO.scheduledUpdate = scheduledUpdate

        return requestVO
    }
}