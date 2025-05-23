package com.friends.ggiriggiri.firebase.repository

import android.util.Log
import androidx.compose.material.darkColors
import com.friends.ggiriggiri.firebase.model.QuestionListModel
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.vo.QuestionListVO
import com.friends.ggiriggiri.firebase.vo.RequestVO
import com.friends.ggiriggiri.firebase.vo.UserVO
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import javax.inject.Inject

class MemoriesRepository @Inject constructor(
    val firestore: FirebaseFirestore,
    val storage: FirebaseStorage
) {
    suspend fun getRequestInfoWithGroupDocumentID(groupDocumentId: String): List<List<String>> = coroutineScope {
        val requestSnapshot = firestore.collection("_requests")
            .whereEqualTo("requestGroupDocumentID", groupDocumentId)
            .get()
            .await()

        val allDocuments = requestSnapshot.documents
        val result = mutableListOf<List<String>>()

        // 10개씩 나누어서 처리
        val chunks = allDocuments.chunked(10)

        for (chunk in chunks) {
            val deferredChunk = chunk.map { document ->
                async {
                    val requestUserId = document.getString("requestUserDocumentID") ?: return@async null
                    val requestMessage = document.getString("requestMessage") ?: ""
                    val requestTime = document.get("requestTime")?.toString() ?: ""
                    val requestDocumentId = document.id

                    // 사용자 문서 병렬 요청
                    val userSnapshot = firestore.collection("_users")
                        .document(requestUserId)
                        .get()
                        .await()

                    val userName = userSnapshot.getString("userName") ?: "알 수 없음"

                    listOf(userName, requestMessage, requestTime, requestDocumentId)
                }
            }

            // 각 chunk 내 병렬 처리 결과 수집
            val chunkResults = deferredChunk.mapNotNull { it.await() }
            result.addAll(chunkResults)
        }

        return@coroutineScope result
    }

    //10개씩나눠 병렬처리를함으로서 속도를 개선
    suspend fun getQuestionsInfoWithGroupName(groupDocumentID: String): List<List<String>> = coroutineScope {
        val snapshot = firestore.collection("_groups")
            .document(groupDocumentID)
            .get()
            .await()

        val dateToInt = snapshot.getLong("groupDayFromCreate")?.toInt() ?: return@coroutineScope emptyList()

        val questionNumbers = (1..dateToInt).toList()

        Log.d("getQuestionsInfoWithGroupName",questionNumbers.toString())

        val deferredList = questionNumbers.chunked(10).map { chunk ->
            async {
                val querySnapshot = firestore.collection("QuestionList")
                    .whereIn("questionNumber", chunk)
                    .get()
                    .await()

                querySnapshot.documents.map { doc ->
                    val questionNumber = doc.get("questionNumber")?.toString() ?: ""
                    val questionContent = doc.getString("questionContent") ?: ""
                    listOf(questionNumber, questionContent)
                }
            }
        }

        deferredList.flatMap { it.await() }
    }
}