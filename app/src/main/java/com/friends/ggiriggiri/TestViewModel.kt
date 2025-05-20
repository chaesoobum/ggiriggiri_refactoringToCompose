package com.friends.ggiriggiri

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestViewModel: ViewModel() {

    fun a(){
        CoroutineScope(Dispatchers.IO).launch {


        }

        viewModelScope.launch {

        }
    }

}