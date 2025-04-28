package com.friends.ggiriggiri.repository

import android.util.Log
import com.friends.ggiriggiri.dataclass.model.GroupModel
import com.friends.ggiriggiri.dataclass.vo.GroupVO
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class JoinGroupRepository@Inject constructor(
    private val firestore: FirebaseFirestore
) {
    //그룹코드와 비밀번호에 맞는 그룹모델을 가져온다 없으면 null이다
    suspend fun getGroupModel(groupCode: String, groupPw: String): GroupModel? {
        return try {
            val querySnapshot = firestore.collection("_groups")
                .whereEqualTo("groupCode", groupCode)
                .whereEqualTo("groupPw", groupPw)
                .limit(1)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Log.e("JoinGroupRepository", "그룹을 찾을 수 없음: groupCode=$groupCode, groupPw=$groupPw")
                null
            } else {
                val document = querySnapshot.documents.first()
                val groupVO = document.toObject(GroupVO::class.java)!!
                Log.d("JoinGroupRepository", "그룹 찾음! Document ID: ${document.id}")
                groupVO.toGroupModel(document.id)
            }
        } catch (e: Exception) {
            Log.e("JoinGroupRepository", "그룹 조회 실패", e)
            null
        }
    }

    //그룹문서에 유저아이디를 추가한다
    suspend fun addUserToGroup(groupDocumentId: String, userDocumentId: String) {
        try {
            val groupRef = firestore.collection("_groups").document(groupDocumentId)

            // 배열에 userDocumentId 추가
            groupRef.update(
                "groupUserDocumentID", FieldValue.arrayUnion(userDocumentId)
            ).await()

            Log.d("JoinGroupRepository", "유저 추가 성공: userDocumentId=$userDocumentId 그룹=$groupDocumentId")
        } catch (e: Exception) {
            Log.e("JoinGroupRepository", "유저 추가 실패", e)
            throw e
        }
    }

    //유저문서에 그룹아이디를 추가한다
    suspend fun updateUserGroupDocumentId(userDocumentId: String, groupDocumentId: String) {
        try {
            val userRef = firestore.collection("_users").document(userDocumentId)

            // userGroupDocumentID 필드 업데이트
            userRef.update(
                "userGroupDocumentID", groupDocumentId
            ).await()

            Log.d("JoinGroupRepository", "유저 문서 업데이트 성공: userDocumentId=$userDocumentId, groupDocumentId=$groupDocumentId")
        } catch (e: Exception) {
            Log.e("JoinGroupRepository", "유저 문서 업데이트 실패", e)
            throw e
        }
    }


}