package com.friends.ggiriggiri.screen.viewmodel.userlogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.MainActivity
import com.friends.ggiriggiri.firebase.model.UserModel
import com.friends.ggiriggiri.firebase.service.GoogleLoginService
import com.friends.ggiriggiri.firebase.service.KakaoLoginService
import com.friends.ggiriggiri.firebase.service.LoginAndRegisterService
import com.friends.ggiriggiri.firebase.service.NaverLoginService
import com.friends.ggiriggiri.firebase.socialdataclass.GoogleUserInfo
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.friends.ggiriggiri.screen.viewmodel.userlogin.UserLoginViewModel.LoginNavigationEvent
import com.friends.ggiriggiri.util.UserSocialLoginState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterStep2ViewModel @Inject constructor(
    @ApplicationContext context: Context,
    val kakaoLoginService: KakaoLoginService,
    val naverLoginService: NaverLoginService,
    val googleLoginService: GoogleLoginService,
    val loginAndRegisterService: LoginAndRegisterService,
    val preferenceManager: PreferenceManager,
): ViewModel() {
    val friendsApplication = context as FriendsApplication

    // 소셜 로그인 다이얼로그
    val showSocialLoginDialog = mutableStateOf(false)
    // 소셜 로그인 다이얼로그 텍스트
    private val _socialLoginDialogText = mutableStateOf("")
    val socialLoginDialogText: State<String> = _socialLoginDialogText

    //이미존재하는 소셜계정
    val showAccountExistAlready = mutableStateOf(false)

    // 프로그래스바
    val isLoading = MutableStateFlow(false)

    fun resetSocialLoginDialogText(){
        _socialLoginDialogText.value = ""
    }

    // 다음 단계로
    val loginNavigationEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)


    fun connectToKakao(){
        _socialLoginDialogText.value = UserSocialLoginState.KAKAO.str
        showSocialLoginDialog.value = true
    }
    fun connectToNaver(){
        _socialLoginDialogText.value = UserSocialLoginState.NAVER.str
        showSocialLoginDialog.value = true
    }
    fun connectToGoogle(){
        _socialLoginDialogText.value = UserSocialLoginState.GOOGLE.str
        showSocialLoginDialog.value = true
    }

    fun kakaoRegisterProcess(activity: Activity,nickname: String){
        viewModelScope.launch {
            isLoading.value = true
            try {
                val (token, kakaoUserInfo) = kakaoLoginService.loginWithKakaoTalkSuspend(activity)
                val userModel = UserModel().apply {
                    userId = kakaoUserInfo.email ?: "unknown"
                    userName = nickname
                    userProfileImage = kakaoUserInfo.profileImageUrl ?: "unknown"
                    userSocialLogin = UserSocialLoginState.KAKAO
                }
                val newUserModel = loginAndRegisterService.register(userModel)

                //이미존재하는소셜계정
                if (newUserModel == null){
                    showAccountExistAlready.value = true
                    isLoading.value = false
                }else{
                    // 회원가입 후 네비게이션
                    onLoginSuccess(
                        newUserModel,
                        UserSocialLoginState.KAKAO,
                        token.toString()
                    )
                }
            }catch (e: Exception) {
                Log.e("kakaoLogin", "로그인 실패", e)
                isLoading.value = false
            }

        }
    }

    fun naverRegisterProcess(activity: Activity,nickname: String){
        viewModelScope.launch {
            isLoading.value = true
            try {
                val (token, naverUserInfo) = naverLoginService.loginWithNaverTalkSuspend(activity)
                val userModel = UserModel()
                userModel.userId = naverUserInfo.email ?: "unknown"
                userModel.userName = nickname
                userModel.userProfileImage = naverUserInfo.profileImageUrl ?: "unknown"
                userModel.userSocialLogin = UserSocialLoginState.NAVER

                val newUserModel = loginAndRegisterService.register(userModel)

                //이미존재하는소셜계정
                if (newUserModel == null){
                    showAccountExistAlready.value = true
                    isLoading.value = false
                }else{
                    // 회원가입 후 네비게이션
                    onLoginSuccess(
                        newUserModel,
                        UserSocialLoginState.NAVER,
                        token.toString()
                    )
                }
            }catch (e: Exception) {
                Log.e("kakaoLogin", "로그인 실패", e)
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

    fun handleGoogleSignInResult(result: Intent?,nickname: String) {
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
                userModel.userName = nickname
                userModel.userProfileImage = googleUserInfo.profileImageUrl ?: "unknown"
                userModel.userSocialLogin = UserSocialLoginState.GOOGLE


                viewModelScope.launch {
                    val newUserModel = loginAndRegisterService.register(userModel)

                    //이미존재하는소셜계정
                    if (newUserModel == null){
                        showAccountExistAlready.value = true
                        isLoading.value = false
                    }else{
                        // 회원가입 성공
                        onLoginSuccess(
                            newUserModel,
                            UserSocialLoginState.GOOGLE,
                            account.idToken.toString()
                        )
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

        //처음가입유저는 소속그룹이 없기때문에 그룹생성화면으로간다
        if (userModelFromDB.userGroupDocumentID.isEmpty()) {
            loginNavigationEvent.emit(Unit)
        }
    }
}