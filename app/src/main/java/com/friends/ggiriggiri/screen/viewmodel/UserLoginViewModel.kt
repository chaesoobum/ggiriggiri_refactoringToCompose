package com.friends.ggiriggiri.screen.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.dataclass.GoogleUserInfo
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.friends.ggiriggiri.service.GoogleLoginService
import com.friends.ggiriggiri.service.KakaoLoginService
import com.friends.ggiriggiri.service.NaverLoginService
import com.friends.ggiriggiri.util.MainScreenName
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val kakaoLoginService: KakaoLoginService,
    val naverLoginService: NaverLoginService,
    val googleLoginService: GoogleLoginService,
    val preferenceManager: PreferenceManager,
) : ViewModel() {
    val friendsApplication = context as FriendsApplication

    // 아이디 입력 요소
    val textFieldUserLoginIdValue = mutableStateOf("")
    // 비밀번호 입력 요소
    val textFieldUserLoginPasswordValue = mutableStateOf("")

    // 프로그래스바
    val isLoading = MutableStateFlow(false) // 로딩 상태

    fun onKakaoLoginClicked(activity: Activity) {
        viewModelScope.launch {
            isLoading.value = true

            kakaoLoginService.loginWithKakaoTalk(
                activity = activity,
                onSuccess = { token, userInfo ->
                    Log.d("kakaoLogin", "토큰: $token")
                    Log.d("kakaoLogin", "사용자 정보: ${userInfo.nickname}, ${userInfo.email}, ${userInfo.profileImageUrl}")
                    Log.d("kakaoLogin", "사용자 정보: ${userInfo}")
                    // TODO: 이후 사용자 정보를 서버로 보내거나 앱 내부에 저장
                },
                onFailure = { error ->
                    Log.e("kakaoLogin", "로그인 실패", error)
                }
            )
            isLoading.value = false

            friendsApplication.navHostController.apply {
                popBackStack(MainScreenName.SCREEN_USER_LOGIN.name,true)
                navigate(MainScreenName.SCREEN_USER_GROUP.name)
            }


        }
    }
    fun onNaverLoginClicked(activity: Activity) {
        viewModelScope.launch {
            isLoading.value = true

            naverLoginService.loginWithNaver(
                activity = activity,
                onSuccess = { token, userInfo ->
                    Log.d("naverLogin", "토큰: $token")
                    Log.d("naverLogin", "사용자 정보: ${userInfo.nickname}, ${userInfo.email}, ${userInfo.profileImageUrl}")
                    Log.d("naverLogin", "사용자 정보: ${userInfo.name}")
                    // TODO: 이후 사용자 정보를 서버로 보내거나 앱 내부에 저장
                },
                onFailure = { error ->
                    Log.e("naverLogin", "로그인 실패", error)
                }
            )
            isLoading.value = false

            friendsApplication.navHostController.apply {
                popBackStack(MainScreenName.SCREEN_USER_LOGIN.name,true)
                navigate(MainScreenName.SCREEN_USER_GROUP.name)
            }
        }
    }

    private val _googleLoginIntent = mutableStateOf<Intent?>(null)
    val googleLoginIntent: State<Intent?> = _googleLoginIntent

    fun onGoogleLoginClicked(activity: Activity) {
        viewModelScope.launch {
            isLoading.value = true
            //_googleLoginIntent.value = googleLoginService.getGoogleClient(activity).signInIntent
            _googleLoginIntent.value = googleLoginService.signOutAndGetIntent(activity)
            isLoading.value = false

            friendsApplication.navHostController.apply {
                popBackStack(MainScreenName.SCREEN_USER_LOGIN.name,true)
                navigate(MainScreenName.SCREEN_USER_GROUP.name)
            }
        }
    }

    fun handleGoogleSignInResult(result: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result)
        if (task.isSuccessful) {
            val account = task.result
            val googleUserInfo = GoogleUserInfo.from(account)

            //자동로그인 저장
            preferenceManager.saveLoginInfo("google", googleUserInfo.email, account.idToken)

            //println(googleUserInfo.toString())
        } else {
            Log.e("GoogleLogin", "로그인 실패: ${task.exception?.message}")
        }
    }

}