package com.friends.ggiriggiri.screen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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

    //그룹코드 전달(다른네비게이션으로)
    private val _groupCode = mutableStateOf("")
    val groupCode: State<String> = _groupCode
    fun setGroupCode(groupCode:String){
        _groupCode.value = groupCode
    }
    fun deleteGroupCode(){
        _groupCode.value = ""
    }






}