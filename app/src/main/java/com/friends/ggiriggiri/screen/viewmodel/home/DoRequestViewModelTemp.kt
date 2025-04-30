package com.friends.ggiriggiri.screen.viewmodel.home

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoRequestViewModelTemp@Inject constructor(

): ViewModel() {
    //요청텍스트
    val requestText = mutableStateOf("")
    //요청사진
    val requestImage = mutableStateOf<Uri?>(null)


    private var _isDone = mutableStateOf(false)
    val isDone: State<Boolean> = _isDone
    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    fun requestProcess(){
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000)
            _isDone.value = true
            _isLoading.value = false
        }
    }

    private var _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog
    fun settingShowDialogToTrue(){
        _showDialog.value = true
    }
    fun settingShowDialogToFalse(){
        _showDialog.value = false
    }

    private var _showFailDialog = mutableStateOf(false)
    val showFailDialog: State<Boolean> = _showFailDialog
    fun settingShowFailDialogToTrue(){
        _showFailDialog.value = true
    }
    fun settingShowFailDialogToFalse(){
        _showFailDialog.value = false
    }



}