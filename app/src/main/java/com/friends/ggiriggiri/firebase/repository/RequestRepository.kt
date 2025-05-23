package com.friends.ggiriggiri.firebase.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.vo.UserVO
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class RequestRepository @Inject constructor(
    val firestore: FirebaseFirestore,
    val storage: FirebaseStorage
) {
    //요청이미지업로드
    suspend fun uploadImageToStorage(
        context: Context,
        uri: Uri,
        onProgress: (Int) -> Unit,
    ): String {
        // Uri -> File
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "request_image_${System.currentTimeMillis()}_${System.nanoTime()}.jpg")
        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input?.copyTo(output)
            }
        }

        // Firebase Storage 참조
        val imageRef = storage.reference.child("request/${tempFile.name}")
        val uploadTask = imageRef.putFile(Uri.fromFile(tempFile))

        // 진행률 콜백
        uploadTask.addOnProgressListener { snapshot ->
            val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
            onProgress(progress)
        }

        // 업로드 완료 대기
        uploadTask.await()

        // 다운로드 URL
        //imageRef.downloadUrl.await().toString()

        return imageRef.downloadUrl.await().toString()
    }

    //요청vo업로드
    suspend fun uploadNewRequest(requestModel: RequestModel) {
        try {
            val userCollection = firestore.collection("_requests")

            val newRequestVO = requestModel.toRequestVO()
            val docRef = userCollection.add(newRequestVO).await()
            val requestDocumentId = docRef.id

            // 30분 후 상태 변경 Cloud Task 예약
            //scheduleRequestStateUpdate(requestDocumentId)

        } catch (e: Exception) {
            if (e is CancellationException) throw e else throw e
        }
    }
    //30분 예약후 상태변경
    suspend fun scheduleRequestStateUpdate(requestDocumentId: String) {
        val client = OkHttpClient()
        val url = "https://us-central1-ggiriggiri-c33b2.cloudfunctions.net/scheduleRequestUpdateTask"

        val jsonBody = JSONObject().apply {
            put("requestDocumentId", requestDocumentId)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonBody.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        Log.d("CloudTask", "예약 성공: ${response.body?.string()}")
                    } else {
                        Log.e("CloudTask", "예약 실패: HTTP ${response.code}")
                    }
                }
            } catch (e: Exception) {
                Log.e("CloudTask", "예외 발생", e)
            }
        }
    }

    //같은 그룹내의 유저들의 fcm코드를 리스트로가져온다
    suspend fun getUserFcmList(groupDocumentId: String, userDocumentId: String): List<String> {
        val snapshot = firestore
            .collection("_users")
            .whereEqualTo("userGroupDocumentID", groupDocumentId)
            .get()
            .await()

        val allFcmTokens = mutableListOf<String>()

        for (doc in snapshot.documents) {
            // 나 자신의 문서인지 확인해서 제외
            if (doc.id == userDocumentId) continue

            val fcmCodes = doc.getString("userFcmCode")
            allFcmTokens.add(fcmCodes.toString())
        }

        return allFcmTokens.filter { it.isNotBlank() }.distinct()
    }




}
