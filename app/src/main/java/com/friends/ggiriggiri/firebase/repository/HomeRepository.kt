package com.friends.ggiriggiri.firebase.repository

import com.friends.ggiriggiri.firebase.model.QuestionListModel
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.vo.QuestionListVO
import com.friends.ggiriggiri.firebase.vo.RequestVO
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepository @Inject constructor(
    val firestore: FirebaseFirestore
) {
    //그룹원들의 이미지들을 가져오는 함수
    suspend fun gettingUserProfileImage(groupDocumentId: String): List<String> = coroutineScope {
        try {

            val groupSnapshot = firestore.collection("_groups")
                .document(groupDocumentId)
                .get()
                .await()

            if (!groupSnapshot.exists()) {
                return@coroutineScope emptyList()
            }

            val groupUserDocumentIDs =
                groupSnapshot.get("groupUserDocumentID") as? List<String> ?: emptyList()
            if (groupUserDocumentIDs.isEmpty()) {
                return@coroutineScope emptyList()
            }

            val chunks = groupUserDocumentIDs.chunked(10)

            // 병렬 처리
            val deferredList = chunks.map { chunk ->
                async {
                    firestore.collection("_users")
                        .whereIn(FieldPath.documentId(), chunk)
                        .get()
                        .await()
                }
            }

            val querySnapshots = deferredList.awaitAll()

            val userProfileImages = mutableListOf<String>()

            for (querySnapshot in querySnapshots) {
                for (document in querySnapshot.documents) {
                    val profileImage = document.getString("userProfileImage")
                    if (!profileImage.isNullOrEmpty()) {
                        userProfileImages.add(profileImage)
                    }
                }
            }

            userProfileImages
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    //그룹명을 가져온다
    suspend fun gettingGroupName(groupDocumentId: String): String {
        return try {

            val groupSnapshot = firestore.collection("_groups")
                .document(groupDocumentId)
                .get()
                .await()

            if (groupSnapshot.exists()) {
                groupSnapshot.getString("groupName") ?: ""
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    //그룹에 요청이있는지 가져온다
    suspend fun getActiveRequestInGroup(groupDocumentId: String): RequestModel? {
        //val firestore = FirebaseFirestore.getInstance()

        val groupSnapshot = firestore.collection("_requests")
            .whereEqualTo("requestGroupDocumentID", groupDocumentId)
            .whereEqualTo("requestState", 1)
            .get()
            .await()

        val document = groupSnapshot.documents.firstOrNull()

        return document?.let {
            val vo = it.toObject(RequestVO::class.java)
            vo?.toRequestModel(it.id)
        }
    }

    //유저아이디로 유저이름을 가져온다
    suspend fun getUserName(userDocumentId: String): String {
        return try {
            val userSnapshot = firestore.collection("_users")
                .document(userDocumentId)
                .get()
                .await()

            if (userSnapshot.exists()) {
                userSnapshot.getString("userName") ?: "이름없음"
            } else {
                "존재하지 않음"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "오류"
        }
    }

    //요청에 내가 응답을 했는지 안했는지
    suspend fun didIResponse(requestDocumentId: String, userDocumentId: String): Boolean {
        return try {
            val snapshot = firestore.collection("_responses")
                .whereEqualTo("responseRequestDocumentId", requestDocumentId)
                .whereEqualTo("responseUserDocumentID", userDocumentId)
                .get()
                .await()
            if (snapshot.isEmpty){
                false
            }else{
                true
            }
        }catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    //그날 그룹에 해당하는 질문가져오기
    suspend fun getQuestionModel(groupDocumentID:String): QuestionListModel?{
        return try {
            val userSnapshot = firestore.collection("_groups")
                .document(groupDocumentID)
                .get()
                .await()

            var groupDayFromCreate = 0
            if (userSnapshot.exists()) {
                groupDayFromCreate = (userSnapshot.getLong("groupDayFromCreate") ?: 0L).toInt()
            }

            val snapshot = firestore.collection("QuestionList")
                .whereEqualTo("questionNumber",groupDayFromCreate)
                .get()
                .await()

            val document = snapshot.documents.firstOrNull()

            val questionListVO = document!!.toObject(QuestionListVO::class.java)

            questionListVO!!.toQuestionListModel()
        }catch (e: Exception){
            null
        }
    }



}