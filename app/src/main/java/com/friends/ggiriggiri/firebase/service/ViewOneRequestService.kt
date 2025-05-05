package com.friends.ggiriggiri.firebase.service

import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.repository.ViewOneRequestRepository
import javax.inject.Inject


class ViewOneRequestService@Inject constructor(
    val viewOneRequestRepository: ViewOneRequestRepository
) {
    suspend fun getRequest(requestDocumentId:String):Map<String, String> {
        return viewOneRequestRepository.getRequest(requestDocumentId)
    }

    suspend fun getResponses(requestDocumentId: String):List<Map<String,String>>{
        return viewOneRequestRepository.getResponses(requestDocumentId)
    }
}