package com.friends.ggiriggiri.firebase.repository

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ResponseRepository@Inject constructor(

) {
    suspend fun getRequestImage(fileName: String): String {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("request/$fileName")

        return imageRef.downloadUrl.await().toString()
    }

}