package com.friends.ggiriggiri.firebase.service

import android.content.Context
import android.net.Uri
import com.friends.ggiriggiri.firebase.model.ResponseModel
import com.friends.ggiriggiri.firebase.repository.ResponseRepository
import javax.inject.Inject

class ResponseService@Inject constructor(
    val responseRepository: ResponseRepository
) {
    //응답이미지업로드
    suspend fun uploadImage(
        context: Context,
        uri: Uri,
        onProgress: (Int) -> Unit,
    ): String {
        return responseRepository.uploadImageToStorage(context,uri,onProgress)
    }

    //응답vo업로드
    suspend fun uploadNewResponse(responseModel: ResponseModel) {
        responseRepository.uploadNewResponse(responseModel)
    }
}