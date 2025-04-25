package com.friends.ggiriggiri.screen.viewmodel.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DoAnswerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val questionImageUrl: String = savedStateHandle["imageUrl"] ?: ""
}
