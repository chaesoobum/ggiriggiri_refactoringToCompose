package com.friends.ggiriggiri.firebase.service

import com.friends.ggiriggiri.firebase.model.QuestionInfo
import com.friends.ggiriggiri.firebase.model.QuestionListModel
import com.friends.ggiriggiri.firebase.model.RequestInfo
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.repository.MemoriesRepository
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class MemoriesService @Inject constructor(
    val memoriesRepository: MemoriesRepository
) {
    suspend fun getRequestInfoPage(
        groupDocumentId: String,
        lastVisibleDocument: DocumentSnapshot? = null,
        pageSize: Int = 10
    ): Pair<List<RequestInfo>, DocumentSnapshot?> {
        return memoriesRepository.getRequestInfoPage(groupDocumentId, lastVisibleDocument, pageSize)
    }

    suspend fun getQuestionListPage(
        startAfterNumber: Long? = null,
        pageSize: Int = 10
    ): Pair<List<QuestionInfo>, Long?> {
        return memoriesRepository.getQuestionListPage(startAfterNumber, pageSize)
    }

    suspend fun getGroupDayFromCreate(groupDocumentId:String): String{
        return memoriesRepository.getGroupDayFromCreate(groupDocumentId)
    }


}

