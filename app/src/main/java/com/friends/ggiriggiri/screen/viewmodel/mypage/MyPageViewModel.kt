package com.friends.ggiriggiri.screen.viewmodel.mypage

import android.content.Context
import android.media.Image
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.model.UserModel
import com.friends.ggiriggiri.firebase.service.GroupService
import com.friends.ggiriggiri.firebase.service.MyPageService
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.friends.ggiriggiri.util.MainScreenName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val preferenceManager: PreferenceManager,
    val groupService: GroupService,
    val myPageService: MyPageService
) : ViewModel() {
    val friendsApplication = context as FriendsApplication

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _showLogoutDialog = mutableStateOf(false)
    val showLogoutDialog: State<Boolean> get() = _showLogoutDialog

    private val _showLeaveTheGroupDialog = mutableStateOf(false)
    val showLeaveTheGroupDialog: State<Boolean> get() = _showLeaveTheGroupDialog

    fun showLogoutDialogTrue() {
        _showLogoutDialog.value = true
    }

    fun showLogoutDialogFalse() {
        _showLogoutDialog.value = false
    }

    fun showLeaveGroupDialogTrue() {
        _showLeaveTheGroupDialog.value = true
    }

    fun showLeaveGroupDialogFalse() {
        _showLeaveTheGroupDialog.value = false
    }

    private fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun logout() {
        viewModelScope.launch {
            setLoading(true)
            preferenceManager.clearLoginInfo()
            friendsApplication.loginUserModel = UserModel()

            friendsApplication.navHostController.apply {
                popBackStack(0, true)
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

    val profileImageUrl = mutableStateOf<String?>(null)

    fun loadProfileImage(userDocumentId: String) {
        viewModelScope.launch {
            val result = myPageService.getProfileImage(userDocumentId)
            profileImageUrl.value = result
        }
    }


    //프로필 이미지 업로딩
    val uploadProfileImage = mutableStateOf(false)

    //업로드 진행률
    val uploadProgress = mutableStateOf(0)

    fun saveProfileImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                uploadProfileImage.value = true
                uploadProgress.value = 0

                val downloadUrl = myPageService.uploadImage(
                    context = context,
                    uri = uri,
                    onProgress = { progress -> uploadProgress.value = progress }
                )

                updateUserprofileImage(context, downloadUrl)

                Log.d("Storage", "업로드 성공")
            } catch (e: Exception) {
                Log.e("Storage", "업로드 실패", e)
            }

        }
    }

    fun updateUserprofileImage(context: Context, downloadUrl: String) {
        viewModelScope.launch {
            try {
                myPageService.updateUserProfileImage(
                    friendsApplication.loginUserModel.userDocumentId,
                    downloadUrl,
                    onSuccess = {
                        Toast.makeText(context,"프로필사진 변경 완료!", Toast.LENGTH_SHORT).show()
                        loadProfileImage(friendsApplication.loginUserModel.userDocumentId)
                    },
                    onFailure = {e->
                        Log.d("updateUserprofileImage",e.toString())
                        Toast.makeText(context,"프로필사진 변경 실패", Toast.LENGTH_SHORT).show()
                    }
                )

            }catch (e:Exception){

            }finally {
                uploadProfileImage.value = false
            }
        }
    }


    private val _groupName = mutableStateOf("")
    val groupName: State<String> = _groupName
    private val _loadingGroupName = mutableStateOf(true)
    val loadingGroupName: State<Boolean> = _loadingGroupName
    fun loadGroupName() {
        val groupDocumentID = friendsApplication.loginUserModel.userGroupDocumentID
        viewModelScope.launch {
            _groupName.value = myPageService.gettingGroupName(groupDocumentID)
            _loadingGroupName.value = false
        }
    }

}

