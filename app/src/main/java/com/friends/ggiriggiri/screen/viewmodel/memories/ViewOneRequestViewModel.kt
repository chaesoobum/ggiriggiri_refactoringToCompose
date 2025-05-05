package com.friends.ggiriggiri.screen.viewmodel.memories

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.service.ViewOneRequestService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewOneRequestViewModel@Inject constructor(
    @ApplicationContext context: Context,
    val viewOneRequestService: ViewOneRequestService
): ViewModel() {
    val friendsApplication = context as FriendsApplication

    //하나의 요청과 그에대한 응답을 불러오는로딩
    private val _isRequestInfoLoading = mutableStateOf(true)
    val isRequestInfoLoading:State<Boolean> = _isRequestInfoLoading


    private val _requestMap = mutableStateOf<Map<String,String>>(emptyMap())
    val requestMap: State<Map<String,String>> = _requestMap
    //하나의 요청과
    fun getRequest(requestDocumentId:String){
        viewModelScope.launch {
            _requestMap.value = viewOneRequestService.getRequest(requestDocumentId)
            getResponses(requestDocumentId)
        }
    }

    private val _responsesMapList = mutableStateOf<List<Map<String,String>>>(emptyList())
    val responsesMapList: State<List<Map<String,String>>> = _responsesMapList
    //그에대한 응답을 불러온다
    fun getResponses(requestDocumentId: String){
        viewModelScope.launch {
            _responsesMapList.value = viewOneRequestService.getResponses(requestDocumentId)
            _isRequestInfoLoading.value = false
        }
    }
}