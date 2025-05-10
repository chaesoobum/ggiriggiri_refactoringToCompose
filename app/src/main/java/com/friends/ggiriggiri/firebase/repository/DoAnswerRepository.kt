package com.friends.ggiriggiri.firebase.repository

import android.util.Log
import com.friends.ggiriggiri.firebase.model.AnswerModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class DoAnswerRepository @Inject constructor(
    val firestore: FirebaseFirestore
) {

    //오늘의 질문 답변을 저장한다
    suspend fun saveAnswerProcess(answerModel: AnswerModel){
        try {
            firestore.collection("_answers")
                .add(answerModel.toAnswerVO())
                .await()

        }catch (e: Exception){
            Log.d("DoAnswerRepository","저장실패 ${e}")
        }
    }
}