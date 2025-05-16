package com.friends.ggiriggiri.firebase.repository

import android.util.Log
import com.friends.ggiriggiri.firebase.model.AnswerDisplayModel
import com.friends.ggiriggiri.firebase.model.QuestionListModel
import com.friends.ggiriggiri.firebase.model.UserModel
import com.friends.ggiriggiri.firebase.vo.AnswerVO
import com.friends.ggiriggiri.firebase.vo.QuestionListVO
import com.friends.ggiriggiri.firebase.vo.UserVO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class ViewOneQuestionRepository @Inject constructor(
    val firestore: FirebaseFirestore,
    val storage: FirebaseStorage
){
    // 오늘의 질문정보를 가져옴
    suspend fun getQuestionListModel(questionNumber:String):QuestionListModel{
        return try {
            val snapshot = firestore.collection("QuestionList")
                .whereEqualTo("questionNumber",questionNumber.toInt())
                .get()
                .await()

            val doc = snapshot.documents.firstOrNull()
            doc!!.toObject(QuestionListVO::class.java)!!.toQuestionListModel()
        }catch (e: Exception){
            Log.d("ViewOneQuestionRepository.getQuestionListModel",e.toString())
        } as QuestionListModel
    }

    //오늘의 질문에 대한 답변을 가져옴
    suspend fun getAnswersDisplayList(
        questionNumber: String,
        groupDocumentID: String
    ): List<AnswerDisplayModel> = coroutineScope {

        val answerSnapshots = firestore.collection("_answers")
            .whereEqualTo("questionNumber", questionNumber.toInt())
            .whereEqualTo("groupDocumentID", groupDocumentID)
            .get()
            .await()

        val answerVOList = answerSnapshots.documents.mapNotNull { doc ->
            val vo = doc.toObject(AnswerVO::class.java)
            vo?.copy(userDocumentID = vo.userDocumentID) // 안전한 복사
        }

        // 10개씩 chunk로 나눠 병렬처리
        val results = answerVOList.chunked(10).map { chunk ->
            async(Dispatchers.IO) {
                chunk.mapNotNull { answerVO ->
                    try {
                        val userSnapshot = firestore.collection("_users")
                            .document(answerVO.userDocumentID)
                            .get()
                            .await()

                        val userVO = userSnapshot.toObject(UserVO::class.java)

                        if (userVO != null) {
                            AnswerDisplayModel(
                                userDocumentID = answerVO.userDocumentID,
                                answerContent = answerVO.answerContent,
                                answerResponseTime = answerVO.answerResponseTime,
                                userName = userVO.userName,
                                userProfileImage = userVO.userProfileImage
                            )
                        } else null
                    } catch (e: Exception) {
                        null // 예외 발생 시 해당 항목은 무시
                    }
                }
            }
        }
        // 모든 async 병렬 작업 결과 병합
        results.flatMap { it.await() }
    }


}