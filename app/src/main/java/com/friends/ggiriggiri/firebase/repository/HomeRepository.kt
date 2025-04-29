package com.friends.ggiriggiri.firebase.repository

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepository@Inject constructor(
    val firestore: FirebaseFirestore
) {
    //그룹원들의 이미지들을 가져오는 함수
    suspend fun gettingUserProfileImage(groupDocumentId: String): List<String> = coroutineScope {
        try {
            val firestore = FirebaseFirestore.getInstance()

            val groupSnapshot = firestore.collection("_groups")
                .document(groupDocumentId)
                .get()
                .await()

            if (!groupSnapshot.exists()) {
                return@coroutineScope emptyList()
            }

            val groupUserDocumentIDs = groupSnapshot.get("groupUserDocumentID") as? List<String> ?: emptyList()
            if (groupUserDocumentIDs.isEmpty()) {
                return@coroutineScope emptyList()
            }

            val chunks = groupUserDocumentIDs.chunked(10)

            // 🔥 병렬 처리
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
            val firestore = FirebaseFirestore.getInstance()

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





}