package com.friends.ggiriggiri.screen.viewmodel.notification

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.room.database.NotificationDatabase
import com.friends.ggiriggiri.room.entity.NotificationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel@Inject constructor(
    @ApplicationContext context: Context,
): ViewModel() {
    val friendsApplication = context as FriendsApplication

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _notificationList = mutableStateOf<List<NotificationEntity>>(emptyList())
    val notificationList: State<List<NotificationEntity>> = _notificationList

    fun getNotificationList(){
        viewModelScope.launch {
            val db = NotificationDatabase.getInstance(friendsApplication)
            _notificationList.value = db!!.notificationDao().getAll()

            _isLoading.value =false
        }

    }
}