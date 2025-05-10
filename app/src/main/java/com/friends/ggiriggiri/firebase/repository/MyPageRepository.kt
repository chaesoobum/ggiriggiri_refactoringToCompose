package com.friends.ggiriggiri.firebase.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject


class MyPageRepository @Inject constructor(
    val firestore: FirebaseFirestore,
    val storage: FirebaseStorage
) {

    suspend fun getProfileImage(userDocumentId: String): String {
        val document = firestore.collection("_users")
            .document(userDocumentId)
            .get()
            .await()

        return if (document.exists()) {
            document.getString("userProfileImage") ?: ""
        } else {
            ""
        }
    }

    //그룹명을 가져온다
    suspend fun gettingGroupName(groupDocumentId: String): String {
        return try {
            //val firestore = FirebaseFirestore.getInstance()

            val groupSnapshot = firestore.collection("_groups")
                .document(groupDocumentId)
                .get()
                .await()

            if (groupSnapshot.exists()) {
                groupSnapshot.getString("groupName") ?: ""
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    //그룹명을 변경한다
    suspend fun changeGroupName(
        newGroupName: String,
        groupDocumentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit = {}
    ) {
        try {
            FirebaseFirestore.getInstance()
                .collection("_groups")
                .document(groupDocumentId)
                .update("groupName", newGroupName)
                .await()

            onSuccess()
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    //프로필 이미지를 업로드한다
    suspend fun uploadImageToStorage(
        context: Context,
        uri: Uri,
        onProgress: (Int) -> Unit,
    ): String {
        // Uri -> File
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "profile_image_${System.currentTimeMillis()}_${System.nanoTime()}.jpg")
        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input?.copyTo(output)
            }
        }

        // Firebase Storage 참조
        val imageRef = storage.reference.child("profile/${tempFile.name}")
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

    //바뀐프로필이미지를 데이터베이스에 적용한다
    suspend fun updateUserProfileImage(userDocumentId:String, downloadUrl:String,onSuccess:() -> Unit,onFailure: (Exception) -> Unit = {}) {
        try {
            FirebaseFirestore.getInstance()
                .collection("_users")
                .document(userDocumentId)
                .update("userProfileImage", downloadUrl)
                .await()

            onSuccess()
        } catch (e: Exception) {
            onFailure(e)
        }
    }

}