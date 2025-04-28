package com.friends.ggiriggiri.screen.viewmodel.userlogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.friends.ggiriggiri.FriendsApplication
import com.friends.ggiriggiri.dataclass.GoogleUserInfo
import com.friends.ggiriggiri.dataclass.model.UserModel
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.friends.ggiriggiri.service.GoogleLoginService
import com.friends.ggiriggiri.service.KakaoLoginService
import com.friends.ggiriggiri.service.LoginAndRegisterService
import com.friends.ggiriggiri.service.NaverLoginService
import com.friends.ggiriggiri.util.MainScreenName
import com.friends.ggiriggiri.util.UserSocialLoginState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject
import kotlin.math.log

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
                    Log.d(
                        "kakaoLogin",
                        "사용자 정보: ${userInfo.nickname}, ${userInfo.email}, ${userInfo.profileImageUrl}"
                    )
                    Log.d("kakaoLogin", "사용자 정보: ${userInfo}")
                    // TODO: 이후 사용자 정보를 서버로 보내거나 앱 내부에 저장
                    val userModel = UserModel()
                    userModel.userId = userInfo.email ?: "unknown"
                    userModel.userName = userInfo.nickname ?: "unknown"
                    userModel.userProfileImage = userInfo.profileImageUrl ?: "unknown"
                    userModel.userSocialLogin = UserSocialLoginState.KAKAO



                    viewModelScope.launch {
                        //로그인이나 회원가입을 하고 데이터를 가져온다
                        val userModelFromDB = loginAndRegisterService.loginOrRegister(userModel)

                        //자동로그인 저장
                        preferenceManager.saveLoginInfo(
                            UserSocialLoginState.KAKAO.name,
                            userInfo.email.toString(),
                            token
                        )

                        //로그인 유저 저장
                        friendsApplication.loginUserModel = userModelFromDB

                        //유저가 그룹에 가입되어있지않다면 SCREEN_USER_GROUP으로간다
                        if (userModelFromDB.userGroupDocumentID.isEmpty()) {
                            friendsApplication.navHostController.apply {
                                popBackStack(MainScreenName.SCREEN_USER_LOGIN.name, true)
                                navigate(MainScreenName.SCREEN_USER_GROUP.name)
                                isLoading.value = false
                            }
                        }
                        //유저가 그룹에 가입되어있다면 SCREEN_USER_MAIN으로간다
                        else {
                            friendsApplication.navHostController.apply {
                                preferenceManager.changeIsGroupInTrue()
                                popBackStack(MainScreenName.SCREEN_USER_LOGIN.name, true)
                                navigate(MainScreenName.SCREEN_USER_MAIN.name)
                                isLoading.value = false
                            }
                        }
                    }
                },
                onFailure = { error ->
                    Log.e("kakaoLogin", "로그인 실패", error)
                }
            )
            isLoading.value = false


        }
    }

    fun onNaverLoginClicked(activity: Activity) {
        viewModelScope.launch {
            isLoading.value = true
            naverLoginService.loginWithNaver(
                activity = activity,
                onSuccess = { token, userInfo ->
                    Log.d("naverLogin", "토큰: $token")
                    Log.d(
                        "naverLogin",
                        "사용자 정보: ${userInfo.nickname}, ${userInfo.email}, ${userInfo.profileImageUrl}"
                    )
                    Log.d("naverLogin", "사용자 정보: ${userInfo.name}")

                    val userModel = UserModel()
                    userModel.userId = userInfo.email ?: "unknown"
                    userModel.userName = userInfo.name ?: "unknown"
                    userModel.userProfileImage = userInfo.profileImageUrl ?: "unknown"
                    userModel.userSocialLogin = UserSocialLoginState.NAVER

                    viewModelScope.launch {
                        //로그인이나 회원가입을 하고 데이터를 가져온다
                        val userModelFromDB = loginAndRegisterService.loginOrRegister(userModel)

                        //자동로그인 저장
                        preferenceManager.saveLoginInfo(
                            UserSocialLoginState.NAVER.name,
                            userInfo.email,
                            token
                        )

                        //로그인 유저 저장
                        friendsApplication.loginUserModel = userModelFromDB

                        //유저가 그룹에 가입되어있지않다면 SCREEN_USER_GROUP으로간다
                        if (userModelFromDB.userGroupDocumentID.isEmpty()) {
                            friendsApplication.navHostController.apply {
                                popBackStack(MainScreenName.SCREEN_USER_LOGIN.name, true)
                                navigate(MainScreenName.SCREEN_USER_GROUP.name)
                                isLoading.value = false
                            }
                        }
                        //유저가 그룹에 가입되어있다면 SCREEN_USER_MAIN으로간다
                        else {
                            friendsApplication.navHostController.apply {
                                preferenceManager.changeIsGroupInTrue()
                                popBackStack(MainScreenName.SCREEN_USER_LOGIN.name, true)
                                navigate(MainScreenName.SCREEN_USER_MAIN.name)
                                isLoading.value = false
                            }
                        }
                    }

                },
                onFailure = { error ->
                    Log.e("naverLogin", "로그인 실패", error)
                }
            )
            //isLoading.value = false
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
        if (task.isSuccessful) {
            val account = task.result
            val googleUserInfo = GoogleUserInfo.Companion.from(account)

            Log.d("google", googleUserInfo.name)
            Log.d("google", googleUserInfo.profileImageUrl)
            Log.d("google", googleUserInfo.email)

            val userModel = UserModel()
            userModel.userId = googleUserInfo.email ?: "unknown"
            userModel.userName = googleUserInfo.name ?: "unknown"
            userModel.userProfileImage = googleUserInfo.profileImageUrl ?: "unknown"
            userModel.userSocialLogin = UserSocialLoginState.GOOGLE

            viewModelScope.launch {
                //로그인이나 회원가입을 하고 데이터를 가져온다
                val userModelFromDB = loginAndRegisterService.loginOrRegister(userModel)

                //자동로그인 저장
                preferenceManager.saveLoginInfo(
                    UserSocialLoginState.GOOGLE.name,
                    googleUserInfo.email,
                    account.idToken
                )

                //로그인 유저 저장
                friendsApplication.loginUserModel = userModelFromDB

                //유저가 그룹에 가입되어있지않다면 SCREEN_USER_GROUP으로간다
                if (userModelFromDB.userGroupDocumentID.isEmpty()) {
                    friendsApplication.navHostController.apply {
                        popBackStack(MainScreenName.SCREEN_USER_LOGIN.name, true)
                        navigate(MainScreenName.SCREEN_USER_GROUP.name)
                        isLoading.value = false
                    }
                }
                //유저가 그룹에 가입되어있다면 SCREEN_USER_MAIN으로간다
                else {
                    friendsApplication.navHostController.apply {
                        preferenceManager.changeIsGroupInTrue()
                        popBackStack(MainScreenName.SCREEN_USER_LOGIN.name, true)
                        navigate(MainScreenName.SCREEN_USER_MAIN.name)
                        isLoading.value = false
                    }
                }
            }


            //println(googleUserInfo.toString())
        } else {
            Log.e("GoogleLogin", "로그인 실패: ${task.exception?.message}")
        }
    }

    fun navigateToGroup(activity: Activity) {
        activity.runOnUiThread {
            friendsApplication.navHostController.apply {
                popBackStack(MainScreenName.SCREEN_USER_LOGIN.name, true)
                navigate(MainScreenName.SCREEN_USER_GROUP.name)
            }
        }
    }

}