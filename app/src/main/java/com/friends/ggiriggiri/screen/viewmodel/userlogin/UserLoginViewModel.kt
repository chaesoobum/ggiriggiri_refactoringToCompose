package com.friends.ggiriggiri.screen.viewmodel.userlogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.firebase.socialdataclass.GoogleUserInfo
import com.friends.ggiriggiri.firebase.model.UserModel
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.friends.ggiriggiri.firebase.service.GoogleLoginService
import com.friends.ggiriggiri.firebase.service.KakaoLoginService
import com.friends.ggiriggiri.firebase.service.LoginAndRegisterService
import com.friends.ggiriggiri.firebase.service.NaverLoginService
import com.friends.ggiriggiri.firebase.socialdataclass.KakaoUserInfo
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.UserSocialLoginState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val kakaoLoginService: KakaoLoginService,
    val naverLoginService: NaverLoginService,
    val googleLoginService: GoogleLoginService,
    val loginAndRegisterService: LoginAndRegisterService,
    val preferenceManager: PreferenceManager,
) : ViewModel() {
    val friendsApplication = context as FriendsApplication

    // 프로그래스바
    val isLoading = MutableStateFlow(false) // 로딩 상태

    // 로그인 실패 다이얼로그 표시
    val showLoginFailDialog = mutableStateOf(false)

    // 화면이동
    sealed class LoginNavigationEvent {
        object NavigateToGroup : LoginNavigationEvent()
        object NavigateToMain : LoginNavigationEvent()
    }

    val loginNavigationEvent = MutableSharedFlow<LoginNavigationEvent>()

    //로그인 유저 불러오기
    suspend fun loadUserModel(userDocumentId: String): UserModel {
        return loginAndRegisterService.getUserModelByDocumentId(userDocumentId)
    }

    fun kakaoLogin(activity: Activity) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val (token, kakaoUserInfo) = kakaoLoginService.loginWithKakaoTalkSuspend(activity)
                val userModel = UserModel().apply {
                    userId = kakaoUserInfo.email ?: "unknown"
                    userName = kakaoUserInfo.nickname ?: "unknown"
                    userProfileImage = kakaoUserInfo.profileImageUrl ?: "unknown"
                    userSocialLogin = UserSocialLoginState.KAKAO
                }
                val userModelFromDB = loginAndRegisterService.userExistCheck(userModel)

                // 로그인 성공
                if (userModelFromDB != null) {

                    onLoginSuccess(
                        userModelFromDB,
                        UserSocialLoginState.KAKAO,
                        token.toString()
                    )

                } else {// 유저 없음 - 알림 표시
                    showLoginFailDialog.value = true
                    isLoading.value = false
                }

            } catch (e: Exception) {
                Log.e("kakaoLogin", "로그인 실패", e)
                isLoading.value = false
            }
        }
    }

    fun NaverLogin(activity: Activity) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val (token, naverUserInfo) = naverLoginService.loginWithNaverTalkSuspend(activity)
                val userModel = UserModel()
                userModel.userId = naverUserInfo.email ?: "unknown"
                userModel.userName = naverUserInfo.name ?: "unknown"
                userModel.userProfileImage = naverUserInfo.profileImageUrl ?: "unknown"
                userModel.userSocialLogin = UserSocialLoginState.NAVER

                Log.d("NaverLogin",userModel.userId)
                Log.d("NaverLogin",userModel.userName)
                Log.d("NaverLogin",userModel.userProfileImage)
                Log.d("NaverLogin",userModel.userSocialLogin.str)

                val userModelFromDB = loginAndRegisterService.userExistCheck(userModel)

                // 로그인 성공
                if (userModelFromDB != null) {

                    onLoginSuccess(
                        userModelFromDB,
                        UserSocialLoginState.NAVER,
                        token.toString()
                    )

                } else {// 유저 없음 - 알림 표시
                    showLoginFailDialog.value = true
                    isLoading.value = false
                }

            } catch (e: Exception) {
                Log.e("NaverLogin", "로그인 실패", e)
                isLoading.value = false
            }
        }
    }

    private val _googleLoginIntent = mutableStateOf<Intent?>(null)
    val googleLoginIntent: State<Intent?> = _googleLoginIntent

    fun onGoogleLoginClicked(activity: Activity) {
        viewModelScope.launch {
            //_googleLoginIntent.value = googleLoginService.getGoogleClient(activity).signInIntent
            _googleLoginIntent.value = googleLoginService.signOutAndGetIntent(activity)
        }
    }

    fun handleGoogleSignInResult(result: Intent?) {
        isLoading.value = true
        val task = GoogleSignIn.getSignedInAccountFromIntent(result)
        try {
            if (task.isSuccessful) {
                val account = task.result
                val googleUserInfo = GoogleUserInfo.from(account)

                Log.d("google", googleUserInfo.name)
                Log.d("google", googleUserInfo.profileImageUrl)
                Log.d("google", googleUserInfo.email)

                val userModel = UserModel()
                userModel.userId = googleUserInfo.email ?: "unknown"
                userModel.userName = googleUserInfo.name ?: "unknown"
                userModel.userProfileImage = googleUserInfo.profileImageUrl ?: "unknown"
                userModel.userSocialLogin = UserSocialLoginState.GOOGLE


                viewModelScope.launch {

                    val userModelFromDB = loginAndRegisterService.userExistCheck(userModel)

                    // 로그인 성공
                    if (userModelFromDB != null) {

                        onLoginSuccess(
                            userModelFromDB,
                            UserSocialLoginState.GOOGLE,
                            account.idToken.toString()
                        )

                    } else { // 유저 없음 - 알림 표시
                        showLoginFailDialog.value = true
                        isLoading.value = false
                    }
                }
            } else {
                Log.e("GoogleLogin", "로그인 실패: ${task.exception?.message}")
                isLoading.value = false
            }
        } catch (e: Exception) {
            Log.e("GoogleLogin", "로그인 실패", e)
        }
    }

    private suspend fun onLoginSuccess(
        userModelFromDB: UserModel,
        loginState: UserSocialLoginState,
        token: String
    ) {
        preferenceManager.saveLoginInfo(
            loginState.name,
            userModelFromDB.userId,
            userModelFromDB.userDocumentId,
            token
        )
        friendsApplication.loginUserModel = userModelFromDB

        if (userModelFromDB.userGroupDocumentID.isEmpty()) {
            loginNavigationEvent.emit(LoginNavigationEvent.NavigateToGroup)
        } else {
            preferenceManager.changeIsGroupInTrue()
            loginNavigationEvent.emit(LoginNavigationEvent.NavigateToMain)
        }
    }
}