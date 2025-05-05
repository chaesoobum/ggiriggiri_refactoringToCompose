package com.friends.ggiriggiri.firebase.service

import android.content.Context
import android.net.Uri
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.repository.RequestRepository
import javax.inject.Inject

class RequestService @Inject constructor(
    private val requestRepository: RequestRepository
) {
    //이미지업로드
    suspend fun uploadImage(
        context: Context,
        uri: Uri,
        onProgress: (Int) -> Unit,
    ): String {
        return requestRepository.uploadImageToStorage(context, uri, onProgress)
    }

    //요청vo업로드
    suspend fun uploadNewRequest(requestModel: RequestModel){
        requestRepository.uploadNewRequest(requestModel)
    }

    //같은 그룹내의 유저들의 fcm코드를 리스트로가져온다
    suspend fun getUserFcmList(groupDocumentId: String,userDocumentId: String): List<String> {
        return requestRepository.getUserFcmList(groupDocumentId,userDocumentId)
    }
}