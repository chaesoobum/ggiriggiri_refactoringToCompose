package com.friends.ggiriggiri.firebase.service

import android.content.Context
import android.net.Uri
import com.friends.ggiriggiri.firebase.repository.MyPageRepository
import javax.inject.Inject

class MyPageService@Inject constructor(
    val myPageRepository: MyPageRepository
) {
    suspend fun getProfileImage(userDocumentId: String): String {
        return myPageRepository.getProfileImage(userDocumentId)
    }

    //그룹명을 가져온다
    suspend fun gettingGroupName(groupDocumentId: String): String {
        return myPageRepository.gettingGroupName(groupDocumentId)
    }

    //그룹명을 변경한다
    suspend fun changeGroupName(newGroupName:String,groupDocumentId:String, onSuccess:() -> Unit,onFailure: (Exception) -> Unit = {}){
        myPageRepository.changeGroupName(newGroupName,groupDocumentId,onSuccess,onFailure)
    }

    //이미지업로드
    suspend fun uploadImage(
        context: Context,
        uri: Uri,
        onProgress: (Int) -> Unit,
    ): String {
        return myPageRepository.uploadImageToStorage(context, uri, onProgress)
    }

    //바뀐프로필이미지를 데이터베이스에 적용한다
    suspend fun updateUserProfileImage(userDocumentId:String, downloadUrl:String,onSuccess:() -> Unit,onFailure: (Exception) -> Unit = {}) {
        myPageRepository.updateUserProfileImage(userDocumentId,downloadUrl,onSuccess,onFailure)
    }
}