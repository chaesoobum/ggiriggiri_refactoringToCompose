package com.friends.ggiriggiri.firebase.repository

import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.vo.RequestVO
import com.friends.ggiriggiri.firebase.vo.UserVO
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import javax.inject.Inject

class MemoriesRepository @Inject constructor() {

    suspend fun getRequestInfoWithUserName(groupDocumentId: String): List<List<String>> {
        val db = FirebaseFirestore.getInstance()
        val requestSnapshot = db.collection("_requests")
            .whereEqualTo("requestGroupDocumentID", groupDocumentId)
            .get()
            .await()

        val result = mutableListOf<List<String>>()

        for (document in requestSnapshot) {
            val requestUserId = document.getString("requestUserDocumentID") ?: continue
            val requestMessage = document.getString("requestMessage") ?: ""
            val requestTime = document.get("requestTime")?.toString() ?: ""

            // _users 컬렉션에서 userName가져오기
            val userSnapshot = db.collection("_users")
                .document(requestUserId)
                .get()
                .await()

            val userName = userSnapshot.getString("userName") ?: "알 수 없음"

            result.add(listOf(userName, requestMessage, requestTime, document.id))
        }

        return result
    }



}