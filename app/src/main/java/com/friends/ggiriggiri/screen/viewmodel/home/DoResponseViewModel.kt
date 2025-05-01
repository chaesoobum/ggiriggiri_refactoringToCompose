package com.friends.ggiriggiri.screen.viewmodel.home

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.service.ResponseService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoResponseViewModel@Inject constructor(
    @ApplicationContext context: Context,
    val doResponseService: ResponseService
): ViewModel() {
    val friendsApplication = context as FriendsApplication

    //이미지 Url 로드
    var requestImageUrl = mutableStateOf<String?>(null)
    fun getRequestImage(fileName: String){
        viewModelScope.launch {
            requestImageUrl.value = doResponseService.getRequestImage(fileName)
        }
    }



}