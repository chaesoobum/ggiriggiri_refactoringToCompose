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

    fun takeInformationForRequestsListScreen(): List<List<String>> {

        val list1 = listOf<String>("채수범", "content1", "2025.03.04 00:15")
        val list2 = listOf<String>("채수범", "content2", "2025.03.04 00:15")
        val list3 = listOf<String>("채수범", "content3", "2025.03.04 00:15")
        val list4 = listOf<String>("채수범", "content3", "2025.03.04 00:15")
        val list5 = listOf<String>("채수범", "content3", "2025.03.04 00:15")
        val list6 = listOf<String>("채수범", "content3", "2025.03.04 00:15")
        val list7 = listOf<String>("채수범", "content3", "2025.03.04 00:15")
        val list8 = listOf<String>("채수범", "content3", "2025.03.04 00:15")
        val list9 = listOf<String>("채수범", "content3", "2025.03.04 00:15")
        val list10 = listOf<String>("채수범", "content3", "2025.03.04 00:15")
        val list11 = listOf<String>("채수범", "content3", "2025.03.04 00:15")

        return listOf(list1, list2, list3, list4, list5, list6, list7, list8, list9, list10, list11)
    }

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

            result.add(listOf(userName, requestMessage, requestTime))
        }

        return result
    }



}