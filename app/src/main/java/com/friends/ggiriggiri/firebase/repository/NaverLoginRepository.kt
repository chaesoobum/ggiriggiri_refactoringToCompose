package com.friends.ggiriggiri.firebase.repository

import android.app.Activity
import android.util.Log
import com.friends.ggiriggiri.firebase.socialdataclass.NaverUserInfo
import com.friends.ggiriggiri.internaldata.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
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
    val prefManager: PreferenceManager,
    val firestore: FirebaseFirestore,
    val storage: FirebaseStorage
) {
    private val client = OkHttpClient()

    fun loginWithNaver(
        activity: Activity,
        onSuccess: (token: String, userInfo: NaverUserInfo) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        // 테스트용 로그아웃 (나중에 제거 가능)
        NaverIdLoginSDK.logout()

        logout{
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
                            val profileImageUrl = userInfo.profileImageUrl ?: "unknown"
                            val name = userInfo.name ?: "unknown"
                            val nickname = userInfo.nickname ?: "unknown"

                            Log.d("naver",email) //이메일
                            Log.d("naver",profileImageUrl) //profileImageUrl
                            Log.d("naver",name) //성명

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

    //onComplete: () -> Unit	함수가 하나의 "파라미터"로 전달된다. 이 파라미터는 아무 인자도 받지 않고(Unit), 아무 값도 리턴하지 않는(Unit) 함수다.
    //= {}	기본값으로 아무것도 안 하는 빈 함수를 준다. (옵션이다)
    fun logout(onComplete: () -> Unit = {}) {
        NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
            override fun onSuccess() {
                Log.d("NaverLogout", "서버 토큰 삭제 성공")
                prefManager.clearLoginInfo()
                onComplete()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e("NaverLogout", "서버 토큰 삭제 실패: $message")
                prefManager.clearLoginInfo()
                onComplete()
            }

            override fun onError(errorCode: Int, message: String) {
                Log.e("NaverLogout", "서버 토큰 삭제 에러: $message")
                prefManager.clearLoginInfo()
                onComplete()
            }
        })
    }
}
