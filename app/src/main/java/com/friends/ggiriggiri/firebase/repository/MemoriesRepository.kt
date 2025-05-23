package com.friends.ggiriggiri.firebase.repository

import android.util.Log
import androidx.compose.material.darkColors
import com.friends.ggiriggiri.firebase.model.QuestionInfo
import com.friends.ggiriggiri.firebase.model.QuestionListModel
import com.friends.ggiriggiri.firebase.model.RequestInfo
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.vo.QuestionListVO
import com.friends.ggiriggiri.firebase.vo.RequestVO
import com.friends.ggiriggiri.firebase.vo.UserVO
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MemoriesRepository @Inject constructor(
    val firestore: FirebaseFirestore,
    val storage: FirebaseStorage
) {

    suspend fun getRequestInfoPage(
        groupDocumentId: String,
        lastVisibleDocument: DocumentSnapshot? = null,
        pageSize: Int = 10
    ): Pair<List<RequestInfo>, DocumentSnapshot?> = coroutineScope {
        try {
            val query = firestore.collection("_requests")
                .whereEqualTo("requestGroupDocumentID", groupDocumentId)
                .orderBy("requestTime")
                .limit(pageSize.toLong())
                .let { if (lastVisibleDocument != null) it.startAfter(lastVisibleDocument) else it }

            val snapshot = query.get().await()
            val documents = snapshot.documents

            val deferredList = documents.map { document ->
                async {
                    try {
                        val requestUserId = document.getString("requestUserDocumentID") ?: return@async null
                        val requestMessage = document.getString("requestMessage") ?: ""
                        val requestTime = document.get("requestTime")?.toString() ?: ""
                        val requestDocumentId = document.id

                        val userSnapshot = firestore.collection("_users")
                            .document(requestUserId)
                            .get()
                            .await()

                        val userName = userSnapshot.getString("userName") ?: "알 수 없음"

                        RequestInfo(userName, requestMessage, requestTime, requestDocumentId)
                    } catch (e: Exception) {
                        Log.e("getRequestInfoPage", "사용자 정보 조회 실패: ${e.message}")
                        null
                    }
                }
            }

            val requestList = deferredList.mapNotNull { it.await() }
            val lastDoc = documents.lastOrNull()

            Pair(requestList, lastDoc)
        } catch (e: Exception) {
            Log.e("getRequestInfoPage", "요청 정보 페이지 조회 실패: ${e.message}")
            Pair(emptyList(), null)
        }
    }


    suspend fun getQuestionListPage(
        startAfterNumber: Long? = null,
        pageSize: Int = 10
    ): Pair<List<QuestionInfo>, Long?> = try {
        val query = firestore.collection("QuestionList")
            .orderBy("questionNumber") // 정렬 기준
            .limit(pageSize.toLong())
            .let { if (startAfterNumber != null) it.startAfter(startAfterNumber) else it }

        val snapshot = query.get().await()

        val questions = snapshot.documents.mapNotNull { doc ->
            val number = doc.getLong("questionNumber")?.toString() ?: return@mapNotNull null
            val content = doc.getString("questionContent") ?: ""
            QuestionInfo(number, content)
        }

        // 다음 페이지를 위한 기준값: 마지막 문서의 questionNumber
        val nextStartAfter = snapshot.documents.lastOrNull()
            ?.getLong("questionNumber")

        Pair(questions, nextStartAfter)
    } catch (e: Exception) {
        Log.e("getQuestionListPage", "질문 페이지 가져오기 실패: ${e.message}")
        Pair(emptyList(), null)
    }

    suspend fun getGroupDayFromCreate(groupDocumentId:String): String{
        try {
            val snapshot = firestore.collection("_groups")
                .document(groupDocumentId)
                .get()
                .await()

            val groupDayFromCreate = snapshot.get("groupDayFromCreate").toString()

            return groupDayFromCreate
        }catch (e: Exception){
            Log.e("getGroupDayFromCreate","오류 : ${e}")
            null
        }
        return "오류"
    }
}