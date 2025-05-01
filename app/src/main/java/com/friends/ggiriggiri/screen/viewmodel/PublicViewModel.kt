package com.friends.ggiriggiri.screen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.friends.ggiriggiri.util.findActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//데이터를 전역모델에 넣어서 돌려쓰기위한 뷰모델
//val pvm: PublicViewModel = hiltViewModel(LocalContext.current.findActivity())

@HiltViewModel
class PublicViewModel @Inject constructor() : ViewModel() {
    //오늘의 질문 이모지 png 전달(다른네비게이션으로)
    private val _questionImageUrl = mutableStateOf("")
    val questionImageUrl: State<String> = _questionImageUrl
    fun setQuestionImageUrl(questionImageUrl:String){
        _questionImageUrl.value = questionImageUrl
    }
    fun deleteQuestionImageUrl(){
        _questionImageUrl.value = ""
    }

    //그룹원 이미지 리스트전달
    private var _memberImageUrls = mutableStateOf<List<String>>(emptyList())
    val memberImageUrls: State<List<String>> = _memberImageUrls
    fun setMemberImageUrls(memberImageUrls:List<String>){
        _memberImageUrls.value = memberImageUrls
    }
    fun deleteMemberImageUrls(){
        _memberImageUrls.value = emptyList()
    }

    //그룹코드 전달(다른네비게이션으로)
    private val _groupCode = mutableStateOf("")
    val groupCode: State<String> = _groupCode
    fun setGroupCode(groupCode:String){
        _groupCode.value = groupCode
    }
    fun deleteGroupCode(){
        _groupCode.value = ""
    }

    //요청 이미지 파일명 전달
    private val _requestImageUrl = mutableStateOf<String?>(null)
    val requestImageUrl: State<String?> = _requestImageUrl
    fun setRequestImageUrl(requestImageUrl:String){
        _requestImageUrl.value = requestImageUrl
    }
    fun deleteRequestImageUrl(){
        _requestImageUrl.value = ""
    }


}