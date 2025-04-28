package com.friends.ggiriggiri.firebase.repository

import android.util.Log
import com.friends.ggiriggiri.firebase.model.UserModel
import com.friends.ggiriggiri.firebase.vo.UserVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class LoginAndRegisterRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun loginOrRegister(userModel: UserModel): UserModel {
        try {
            val db = FirebaseFirestore.getInstance()
            val userCollection = db.collection("_users")

            val querySnapshot = userCollection
                .whereEqualTo("userId", userModel.userId)
                .get()
                .await()
            return if (querySnapshot.isEmpty) {
                // 회원이 없으면 새로 등록
                val newUserVO = userModel.toUserVO()
                val documentRef = userCollection.add(newUserVO).await()

                Log.e("LoginOrRegister", "신규 회원 등록 성공")
                newUserVO.toUserModel(documentRef.id) // 생성된 문서 ID를 넘겨서 UserModel 변환
            } else {
                // 이미 회원이면 첫 번째 문서를 UserVO로 변환해서 반환
                val document = querySnapshot.documents.first()
                val userVO = document.toObject(UserVO::class.java)!!
                Log.e("LoginOrRegister", "이미 회원 존재")
                userVO.toUserModel(document.id) // 기존 문서 ID를 넘겨서 UserModel 변환
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
}