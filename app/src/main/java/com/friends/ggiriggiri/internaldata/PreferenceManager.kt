package com.friends.ggiriggiri.internaldata

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PLATFORM = "LOGIN_PLATFORM"
        private const val KEY_USER_ID = "USER_ID"
        private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val KEY_IS_LOGGED_IN = "IS_LOGGED_IN"
    }

    /* 로그인 정보 저장 */
    fun saveLoginInfo(platform: String, userId: String, accessToken: String? = null) {
        with(prefs.edit()) {
            putString(KEY_PLATFORM, platform)
            putString(KEY_USER_ID, userId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            accessToken?.let { putString(KEY_ACCESS_TOKEN, it) }
            apply()
        }
    }

    /* 토큰만 따로 저장 */
    fun saveToken(token: String) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    /* 로그인 여부 확인 */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /* 로그인한 플랫폼 (kakao, google, naver) */
    fun getLoginPlatform(): String? {
        return prefs.getString(KEY_PLATFORM, null)
    }

    /* 사용자 ID */
    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    /* 액세스 토큰 */
    fun getToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    /* 모든 로그인 정보 제거 */
    fun clearLoginInfo() {
        prefs.edit().clear().apply()
    }
}
