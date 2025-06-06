package com.friends.ggiriggiri.firebase.repository

import android.app.Activity
import android.content.Intent
import com.friends.ggiriggiri.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import javax.inject.Inject

class GoogleLoginRepository @Inject constructor() {

    fun getGoogleClient(activity: Activity): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.google_login_client_id)) // ID 토큰 요청
            .requestEmail() // 이메일 요청
            .build()


        return GoogleSignIn.getClient(activity, googleSignInOption)
    }

    //임시적으로 매번 로그아웃되게함
    fun signOutAndGetIntent(activity: Activity): Intent {
        val client = getGoogleClient(activity)
        client.signOut() // ← 자동 로그인 방지: 이전 계정 정보 제거

        return client.signInIntent
    }
}