package com.friends.ggiriggiri.screen.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.friends.ggiriggiri.FriendsApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class UserMainViewModel @Inject constructor(
    @ApplicationContext context: Context,
): ViewModel() {
    val friendsApplication = context as FriendsApplication
}