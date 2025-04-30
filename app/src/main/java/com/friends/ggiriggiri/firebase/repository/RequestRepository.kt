package com.friends.ggiriggiri.firebase.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class RequestRepository @Inject constructor() {
    private val storage: FirebaseStorage = Firebase.storage
    //이미지업로드
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

        return tempFile.name
    }

    //요청vo업로드
    suspend fun uploadNewRequest(requestModel: RequestModel){
        try {
            val db = FirebaseFirestore.getInstance()
            val userCollection = db.collection("_requests")

            val newRequestVO = requestModel.toRequestVO()
            userCollection.add(newRequestVO).await()


        }catch (e: Exception){
            if (e is CancellationException) {
                throw e
            } else {
                throw e
            }
        }
    }
}
