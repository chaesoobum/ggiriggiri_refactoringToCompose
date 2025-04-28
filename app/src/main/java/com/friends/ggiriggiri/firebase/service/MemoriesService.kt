package com.friends.ggiriggiri.firebase.service

import com.friends.ggiriggiri.firebase.repository.MemoriesRepository
import javax.inject.Inject

class MemoriesService@Inject constructor(
    val memoriesRepository: MemoriesRepository
){
    fun takeInformationForRequestsListScreen():List<List<String>>{
        return memoriesRepository.takeInformationForRequestsListScreen()
    }
}