package com.friends.ggiriggiri.service

import com.friends.ggiriggiri.repository.MemoriesRepository
import javax.inject.Inject

class MemoriesService@Inject constructor(
    val memoriesRepository: MemoriesRepository
){
    fun takeInformationForRequestsListScreen():List<List<String>>{
        return memoriesRepository.takeInformationForRequestsListScreen()
    }
}