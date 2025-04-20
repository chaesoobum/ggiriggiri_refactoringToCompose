package com.friends.ggiriggiri.repository

import android.app.Activity
import android.util.Log
import com.friends.ggiriggiri.dataclass.NaverUserInfo
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class NaverLoginRepository @Inject constructor(
    private val prefManager: PreferenceManager
) {
    private val client = OkHttpClient()

    fun loginWithNaver(
        activity: Activity,
        onSuccess: (token: String, userInfo: NaverUserInfo) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        // 테스트용 로그아웃 (나중에 제거 가능)
        NaverIdLoginSDK.logout()

        NaverIdLoginSDK.authenticate(activity, object : OAuthLoginCallback {
            override fun onSuccess() {
                val accessToken = NaverIdLoginSDK.getAccessToken()
                Log.d("NaverLogin", "네이버 로그인 성공, Access Token: $accessToken")

                if (accessToken != null) {
                    fetchNaverUserInfo(accessToken, onSuccess, onFailure)
                } else {
                    onFailure(Throwable("AccessToken이 null입니다."))
                }
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e("NaverLogin", "네이버 로그인 실패: $message")
                onFailure(Throwable("네이버 로그인 실패: $message"))
            }

            override fun onError(errorCode: Int, message: String) {
                Log.e("NaverLogin", "네이버 로그인 에러: $message")
                onFailure(Throwable("네이버 로그인 에러: $message"))
            }
        })
    }

    private fun fetchNaverUserInfo(
        accessToken: String,
        onSuccess: (token: String, userInfo: NaverUserInfo) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val request = Request.Builder()
            .url("https://openapi.naver.com/v1/nid/me")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("NaverLogin", "사용자 정보 요청 실패", e)
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    if (body != null) {
                        try {
                            val json = JSONObject(body)
                            val userJson = json.getJSONObject("response")
                            val userInfo = NaverUserInfo.from(userJson)

                            val email = userInfo.email ?: "unknown"
                            saveLoginInfo(email, accessToken)

                            onSuccess(accessToken, userInfo)
                        } catch (e: Exception) {
                            Log.e("NaverLogin", "JSON 파싱 오류", e)
                            onFailure(e)
                        }
                    } else {
                        onFailure(Throwable("응답 본문이 비어있습니다."))
                    }
                } else {
                    onFailure(Throwable("응답 실패: ${response.message}"))
                }
            }
        })
    }

    private fun saveLoginInfo(userId: String, accessToken: String?) {
        prefManager.saveLoginInfo("naver", userId, accessToken)
    }

    fun logout(onComplete: () -> Unit = {}) {
        NaverIdLoginSDK.logout()
        prefManager.clearLoginInfo()
        onComplete()
    }
}
