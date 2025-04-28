package com.friends.ggiriggiri.screen.viewmodel.mypage

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.model.UserModel
import com.friends.ggiriggiri.firebase.service.GroupService
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.friends.ggiriggiri.util.MainScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val preferenceManager: PreferenceManager,
    private val groupService: GroupService
) : ViewModel() {
    val friendsApplication = context as FriendsApplication

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _showLogoutDialog = mutableStateOf(false)
    val showLogoutDialog: State<Boolean> get() = _showLogoutDialog

    private val _showLeaveTheGroupDialog = mutableStateOf(false)
    val showLeaveTheGroupDialog: State<Boolean> get() = _showLeaveTheGroupDialog

    fun showLogoutDialogTrue() { _showLogoutDialog.value = true }
    fun showLogoutDialogFalse() { _showLogoutDialog.value = false }

    fun showLeaveGroupDialogTrue() { _showLeaveTheGroupDialog.value = true }
    fun showLeaveGroupDialogFalse() { _showLeaveTheGroupDialog.value = false }

    private fun setLoading(isLoading: Boolean) { _isLoading.value = isLoading }

    fun logout() {
        viewModelScope.launch {
            setLoading(true)
            preferenceManager.clearLoginInfo()
            friendsApplication.loginUserModel = UserModel()

            friendsApplication.navHostController.apply {
                popBackStack(0, true) // 전체 스택 삭제
                navigate(MainScreenName.SCREEN_USER_LOGIN.name)
            }
            setLoading(false)
        }
    }

    fun leaveTheGroup() {
        viewModelScope.launch {
            setLoading(true)
            preferenceManager.changeIsGroupInFalse()

            groupService.removeGroupDocumentIdFromUser(friendsApplication.loginUserModel.userDocumentId)
            groupService.removeUserFromGroup(
                friendsApplication.loginUserModel.userGroupDocumentID,
                friendsApplication.loginUserModel.userDocumentId
            )

            friendsApplication.loginUserModel.userGroupDocumentID = ""

            friendsApplication.navHostController.apply {
                popBackStack(0, true)
                navigate(MainScreenName.SCREEN_USER_GROUP.name)
            }
            setLoading(false)
        }
    }
}
