package com.friends.ggiriggiri.screen.viewmodel.mypage

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.friends.ggiriggiri.util.MainScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val preferenceManager: PreferenceManager,
): ViewModel() {
    val friendsApplication = context as FriendsApplication

    private var _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog
    fun settingShowDialogTrue(){
        _showDialog.value=true
    }
    fun settingShowDialogFalse(){
        _showDialog.value=false
    }

    fun logout(){
        //모든 로그인 정보를 지운다
        preferenceManager.clearLoginInfo()

        friendsApplication.navHostController.apply {
            popBackStack(MainScreenName.SCREEN_USER_MAIN.name,true)
            navigate(MainScreenName.SCREEN_USER_LOGIN.name)
        }
    }

}