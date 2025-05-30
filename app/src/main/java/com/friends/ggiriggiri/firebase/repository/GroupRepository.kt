package com.friends.ggiriggiri.firebase.repository

import android.util.Log
import com.friends.ggiriggiri.firebase.model.GroupModel
import com.friends.ggiriggiri.firebase.vo.GroupVO
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupRepository@Inject constructor(
    val firestore: FirebaseFirestore
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

    // 그룹 문서에서 유저 아이디를 제거한다
    suspend fun removeUserFromGroup(groupDocumentId: String, userDocumentId: String) {
        try {
            val groupRef = firestore.collection("_groups").document(groupDocumentId)

            // 배열에서 userDocumentId 제거
            groupRef.update(
                "groupUserDocumentID", FieldValue.arrayRemove(userDocumentId)
            ).await()

            Log.d("JoinGroupRepository", "유저 제거 성공: userDocumentId=$userDocumentId 그룹=$groupDocumentId")
        } catch (e: Exception) {
            Log.e("JoinGroupRepository", "유저 제거 실패", e)
            throw e
        }
    }

    // 유저 문서에서 그룹 아이디를 빈 문자열로 설정한다
    suspend fun removeGroupDocumentIdFromUser(userDocumentId: String) {
        try {
            val userRef = firestore.collection("_users").document(userDocumentId)

            // userGroupDocumentID 필드를 빈 문자열로 설정
            userRef.update(
                "userGroupDocumentID", ""
            ).await()

            Log.d("JoinGroupRepository", "유저 문서 그룹 ID 빈 문자열 설정 성공: userDocumentId=$userDocumentId")
        } catch (e: Exception) {
            Log.e("JoinGroupRepository", "유저 문서 그룹 ID 빈 문자열 설정 실패", e)
            throw e
        }
    }

    //그룹문서에서 입력된 그룹코드가있는지 검사한다
    suspend fun checkDuplicationGroupCode(groupCode: String): Boolean {
        return try {
            val querySnapshot = firestore.collection("_groups")
                .whereEqualTo("groupCode", groupCode)
                .limit(1)
                .get()
                .await()

            querySnapshot.isEmpty // <- 이렇게 간결하게도 가능
        } catch (e: Exception) {
            Log.e("JoinGroupRepository", "그룹 조회 실패", e)
            false
        }
    }

    //그룹을 만든다
    suspend fun makeGroup(groupModel: GroupModel): GroupModel {
        val groupVO = groupModel.toGroupVO()

        val documentReference = firestore.collection("_groups")
            .add(groupVO)
            .await()

        val updatedGroupModel = groupVO.toGroupModel(documentReference.id)

        return updatedGroupModel
    }

}