package com.friends.ggiriggiri.firebase.repository

import android.util.Log
import com.friends.ggiriggiri.firebase.model.ResponseModel
import com.friends.ggiriggiri.firebase.vo.RequestVO
import com.friends.ggiriggiri.firebase.vo.ResponseVO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class ViewOneRequestRepository @Inject constructor(
    val firestore: FirebaseFirestore
) {
    suspend fun getRequest(requestDocumentId: String): Map<String, String> {

        return try {
            val snapshot = firestore.collection("_requests")
                .document(requestDocumentId)
                .get()
                .await()

            val requestVO = snapshot.toObject(RequestVO::class.java)
            if (requestVO == null) {
                Log.e("getRequest", "❌ requestVO is null for documentId: $requestDocumentId")
                return emptyMap() // 또는 throw Exception("Invalid request format")
            }
            val requestModel = requestVO.toRequestModel(snapshot.id)
            Log.d("getRequest", "🟢 requestModel.requestTime = ${requestModel.requestTime}")



            val requestUserDocumentID = snapshot.getString("requestUserDocumentID")
            val userSnapshot = firestore.collection("_users")
                .document(requestUserDocumentID.toString())
                .get()
                .await()

            val userProfileImage = userSnapshot.getString("userProfileImage")
            val userName = userSnapshot.getString("userName")

            Log.d("getRequest",requestModel.requestTime.toString())
            val map = mutableMapOf<String, String>()
            map.put("requesterName",userName.toString())
            map.put("requesterProfileImage",userProfileImage.toString())
            map["requestTime"] = requestModel.requestTime.toString() ?: "0"
            map.put("requestImage",requestModel.requestImage.toString())
            map.put("requestMessage",requestModel.requestMessage.toString())
            map
        }catch (e: Exception){
            val map = mutableMapOf<String, String>()
            map
        }
    }

    suspend fun getResponses(requestDocumentId: String): List<Map<String,String>> {
        return try {
            val snapshot = firestore.collection("_responses")
                .whereEqualTo("responseRequestDocumentId", requestDocumentId)
                .get()
                .await()

            val list = mutableListOf<ResponseModel>()
            if (!snapshot.isEmpty){
                snapshot.forEach {
                    val responseVo = it.toObject(ResponseVO::class.java)
                    list.add(responseVo.toResponseModel(it.id))
                }
            }

            val resultList = mutableListOf<Map<String,String>>()

            list.forEach {
                val userSnapshot = firestore.collection("_users")
                    .document(it.responseUserDocumentID)
                    .get()
                    .await()

                val userName = userSnapshot.getString("userName")
                val userProfileImage = userSnapshot.getString("userProfileImage")

                val tempMap = mutableMapOf<String,String>()

                tempMap.put("userName",userName.toString())
                tempMap.put("userProfileImage",userProfileImage.toString())
                tempMap.put("responseTime",it.responseTime.toString())
                tempMap.put("responseImage",it.responseImage)
                tempMap.put("responseMessage",it.responseMessage)

                resultList.add(tempMap)
            }

            resultList
        }catch (e: Exception) {
            val emptyMap = listOf<Map<String,String>>()
            emptyMap
        }
    }
}