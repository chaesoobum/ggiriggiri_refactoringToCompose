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
}