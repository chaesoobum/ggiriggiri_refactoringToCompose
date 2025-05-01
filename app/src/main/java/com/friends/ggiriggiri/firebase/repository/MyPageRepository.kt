package com.friends.ggiriggiri.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class MyPageRepository @Inject constructor(

) {
    private val storage: FirebaseStorage = Firebase.storage

    suspend fun getProfileImage(userDocumentId: String): String {
        val db = FirebaseFirestore.getInstance()
        val document = db.collection("_users")
            .document(userDocumentId)
            .get()
            .await()

        return if (document.exists()) {
            document.getString("userProfileImage") ?: ""
        } else {
            ""
        }
    }
}