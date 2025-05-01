package com.friends.ggiriggiri.firebase.service

import com.friends.ggiriggiri.firebase.repository.ResponseRepository
import javax.inject.Inject

class ResponseService@Inject constructor(
    val responseRepository: ResponseRepository
) {
    suspend fun getRequestImage(fileName: String): String {
        return responseRepository.getRequestImage(fileName)
    }
}