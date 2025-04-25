package com.friends.ggiriggiri.screen.viewmodel.home

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel(){
    val friendsApplication = context as FriendsApplication

    // 그룹명 가져오기
    private var _isTitleLoading = mutableStateOf(true)
    val isTitleLoading: State<Boolean> = _isTitleLoading
    fun getAppBarTitle(){
        viewModelScope.launch {
            delay(500)
            _isTitleLoading.value = false
        }
    }

    //그룹원 프로필 이미지가져오기
    private var _memberImageUrls = mutableStateOf<List<String>>(emptyList())
    val memberImageUrls: State<List<String>> = _memberImageUrls
    fun getMemberProfileImage(){
        viewModelScope.launch {
            delay(500)
            _memberImageUrls.value = listOf(
                "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
                "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
                "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26"
            )
        }
    }

    //그룹이미지 랜덤 가져오기
    private var _groupImageUrls = mutableStateOf<List<String>>(emptyList())
    val groupImageUrls: State<List<String>> = _groupImageUrls
    fun getImageCarousel(){
        viewModelScope.launch {
            delay(500)
            _groupImageUrls.value = listOf(
                "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
                "https://picsum.photos/id/1016/400/600",
                "https://not-a-real-url/image.jpg", // 실패 테스트
                "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26",
            )
        }
    }

    //오늘의 질문 이미지 가져오기
    private var _questionImageUrl = mutableStateOf<String>("")
    val questionImageUrl: State<String> = _questionImageUrl

    fun getQuestionImageUrl(){
        viewModelScope.launch {
            delay(500)
            _questionImageUrl.value ="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Confounded%20Face.png"
        }
    }
}