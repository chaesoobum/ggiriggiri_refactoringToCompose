package com.friends.ggiriggiri.firebase.repository

import android.util.Log
import com.friends.ggiriggiri.firebase.model.UserModel
import com.friends.ggiriggiri.firebase.vo.UserVO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class LoginAndRegisterRepository @Inject constructor(
    val firestore: FirebaseFirestore,
    val storage: FirebaseStorage
) {
    //로그인하거나 가입하거나
    suspend fun loginOrRegister(userModel: UserModel): UserModel {
        try {
            val userCollection = firestore.collection("_users")

            val querySnapshot = userCollection
                .whereEqualTo("userId", userModel.userId)
                .get()
                .await()
            return if (querySnapshot.isEmpty) {
                //fcm토큰을 추가
                val fcmToken = FirebaseMessaging.getInstance().token.await()
                userModel.userFcmCode = mutableListOf(fcmToken)

                // 회원이 없으면 새로 등록
                val newUserVO = userModel.toUserVO()
                val documentRef = userCollection.add(newUserVO).await()

                Log.e("LoginOrRegister", "신규 회원 등록 성공")
                Log.e("LoginOrRegister", documentRef.id)
                newUserVO.toUserModel(documentRef.id) // 생성된 문서 ID를 넘겨서 UserModel 변환
            } else {
                // 이미 회원이면 첫 번째 문서 가져오기
                val document = querySnapshot.documents.first()
                val documentId = document.id
                // 현재 로그인 기기의 FCM 토큰 받아오기
                val currentFcmToken = FirebaseMessaging.getInstance().token.await()
                // 기존 userFcmCode 배열 가져오기 (nullable 방지)
                val existingFcmTokens = document.get("userFcmCode") as? List<String> ?: emptyList()
                // FCM 토큰이 없으면 추가
                if (!existingFcmTokens.contains(currentFcmToken)) {
                    val updatedTokens = existingFcmTokens.toMutableList().apply {
                        add(currentFcmToken)
                    }

                    // firestore 업데이트 먼저 수행
                    userCollection.document(documentId)
                        .update("userFcmCode", updatedTokens)
                        .await() // ← 중요: 먼저 업데이트 후에 다시 가져오기
                    Log.d("LoginOrRegister", "FCM 토큰 추가 완료")
                } else {
                    Log.d("LoginOrRegister", "FCM 토큰 이미 존재")
                }

                // firestore 문서 최신 상태로 다시 가져와서 모델 변환
                val updatedDocument = userCollection.document(documentId).get().await()
                val updatedUserVO = updatedDocument.toObject(UserVO::class.java)!!
                return updatedUserVO.toUserModel(documentId)
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            } else {
                Log.e("LoginOrRegister", "로그인 또는 회원가입 실패", e)
                throw e
            }
        }
    }

    //유저문서아이디로 유저모델을 가져온다
    suspend fun getUserModelByDocumentId(userDocumentId: String): UserModel {
        try {
            val userCollection = firestore.collection("_users")
            val userDocRef = userCollection.document(userDocumentId)

            val documentSnapshot = userDocRef.get().await()

            if (documentSnapshot.exists()) {
                val userVO = documentSnapshot.toObject(UserVO::class.java)!!
                val currentFcmToken = FirebaseMessaging.getInstance().token.await()

                // 기존 FCM 토큰 리스트 확인
                val existingFcmTokens = documentSnapshot.get("userFcmCode") as? List<String> ?: emptyList()

                if (!existingFcmTokens.contains(currentFcmToken)) {
                    val updatedTokens = existingFcmTokens.toMutableList().apply { add(currentFcmToken) }
                    userDocRef.update("userFcmCode", updatedTokens).await()
                    Log.d("GetUserModel", "FCM 토큰 자동로그인 중 추가됨")
                } else {
                    Log.d("GetUserModel", "FCM 토큰 이미 존재")
                }

                // 최신 상태에서 다시 한 번 로드해도 되고, 기존 userVO를 써도 무방
                return userVO.toUserModel(documentSnapshot.id)
            } else {
                throw IllegalArgumentException("해당 userDocumentId에 해당하는 문서가 없습니다: $userDocumentId")
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e("GetUserModel", "유저 조회 실패", e)
            throw e
        }
    }


}