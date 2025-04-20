package com.friends.ggiriggiri.repository

import android.app.Activity
import android.util.Log
import com.friends.ggiriggiri.dataclass.KakaoUserInfo
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject

class KakaoLoginRepository @Inject constructor(
    private val prefManager: PreferenceManager
) {
    fun loginWithKakaoTalk(
        activity: Activity,
        onSuccess: (token: String, userInfo: KakaoUserInfo) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        //임시적으로 매번 로그아웃되게함
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("kakaologout", "로그아웃 실패. SDK에서 토큰 폐기됨", error)
            }
            else {
                Log.i("kakaologout", "로그아웃 성공. SDK에서 토큰 폐기됨")
            }
        }
        //

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
            UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                if (error != null) {
                    Log.e("KakaoLogin", "카카오톡 로그인 실패, 계정 로그인 시도", error)
                    UserApiClient.instance.loginWithKakaoAccount(
                        activity,
                        prompts = listOf(Prompt.LOGIN)
                    ) { accountToken, accountError ->
                        handleLoginResult(activity,accountToken, accountError, onSuccess, onFailure)
                    }
                } else {
                    handleLoginResult(activity,token, error, onSuccess, onFailure)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(activity) { token, error ->
                handleLoginResult(activity,token, error, onSuccess, onFailure)
            }
        }
    }

    private fun handleLoginResult(
        activity: Activity,
        token: OAuthToken?,
        error: Throwable?,
        onSuccess: (token: String, userInfo: KakaoUserInfo) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        if (error != null) {
            Log.e("KakaoLogin", "로그인 실패", error)
            onFailure(error)
        } else if (token != null) {
            Log.d("KakaoLogin", "로그인 성공: ${token.accessToken}")
            fetchKakaoUserInfo(activity,token.accessToken, onSuccess, onFailure)
        }
    }

    private fun fetchKakaoUserInfo(
        activity: Activity,
        accessToken: String,
        onSuccess: (token: String, userInfo: KakaoUserInfo) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        // 사용자 정보를 불러오는 메서드
        UserApiClient.instance.me { user, error ->
            if (error != null || user == null) {
                Log.e("KakaoUserInfo", "사용자 정보 요청 실패", error)
                onFailure(error ?: Exception("사용자 정보가 null입니다."))
                return@me
            }

            val userInfo = KakaoUserInfo.from(user)

            //자동로그인 저장

            val email = userInfo.email ?: "unknown"
            saveLoginInfo(email,accessToken)
            onSuccess(accessToken, userInfo)
        }
    }

    // 쉐어드 프리퍼런스 저장
    fun saveLoginInfo(userId: String, accessToken: String? = null) {
        prefManager.saveLoginInfo("kakao", userId, accessToken)
    }

    //로그아웃
    fun logout(onComplete: () -> Unit = {}) {
        UserApiClient.instance.logout { error ->
            prefManager.clearLoginInfo()
            if (error != null) {
                Log.e("KakaoLogout", "로그아웃 실패", error)
            } else {
                Log.d("KakaoLogout", "로그아웃 성공")
            }
            onComplete()
        }
    }
}


