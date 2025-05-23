package com.friends.ggiriggiri.firebase.repository

import android.content.Context
import android.net.Uri
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.model.ResponseModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ResponseRepository@Inject constructor(
    val firestore: FirebaseFirestore,
    val storage: FirebaseStorage
) {
    //응답이미지업로드
    suspend fun uploadImageToStorage(
        context: Context,
        uri: Uri,
        onProgress: (Int) -> Unit,
    ): String {
        // Uri -> File
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "response_image_${System.currentTimeMillis()}_${System.nanoTime()}.jpg")
        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input?.copyTo(output)
            }
        }

        // Firebase Storage 참조
        val imageRef = storage.reference.child("response/${tempFile.name}")
        val uploadTask = imageRef.putFile(Uri.fromFile(tempFile))

        // 진행률 콜백
        uploadTask.addOnProgressListener { snapshot ->
            val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
            onProgress(progress)
        }

        // 업로드 완료 대기
        uploadTask.await()

        return imageRef.downloadUrl.await().toString()
    }

    //응답vo업로드
    suspend fun uploadNewResponse(responseModel: ResponseModel) {
        try {
            val userCollection = firestore.collection("_responses")

            val newResponseVO = responseModel.toResponseVO()
            val docRef = userCollection.add(newResponseVO).await()
            val responseDocumentId = docRef.id

        } catch (e: Exception) {
            if (e is CancellationException) throw e else throw e
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