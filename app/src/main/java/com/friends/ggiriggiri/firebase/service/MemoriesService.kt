package com.friends.ggiriggiri.firebase.service

import com.friends.ggiriggiri.firebase.model.QuestionListModel
import com.friends.ggiriggiri.firebase.model.RequestModel
import com.friends.ggiriggiri.firebase.repository.MemoriesRepository
import javax.inject.Inject

class MemoriesService @Inject constructor(
    val memoriesRepository: MemoriesRepository
) {

    suspend fun getRequestInfoWithGroupDocumentID(groupDocumentId: String): List<List<String>> {
        return memoriesRepository.getRequestInfoWithGroupDocumentID(groupDocumentId)
    }

    suspend fun getQuestionsInfoWithGroupName(groupDocumentID: String): List<List<String>> {
        return memoriesRepository.getQuestionsInfoWithGroupName(groupDocumentID)
    }
}

