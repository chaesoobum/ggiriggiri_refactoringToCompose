package com.friends.ggiriggiri.screen.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.util.MainScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PublicViewModel @Inject constructor() : ViewModel() {
    private val _questionImageUrl = mutableStateOf("")
    val questionImageUrl: State<String> = _questionImageUrl
    fun setQuestionImageUrl(questionImageUrl:String){
        _questionImageUrl.value = questionImageUrl
    }
    fun deleteQuestionImageUrl(){
        _questionImageUrl.value = ""
    }
}