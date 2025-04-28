package com.friends.ggiriggiri.firebase.service

import android.app.Activity
import android.content.Intent
import com.friends.ggiriggiri.firebase.repository.GoogleLoginRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import javax.inject.Inject

class GoogleLoginService @Inject constructor(
    val googleLoginRepository: GoogleLoginRepository
){
    fun getGoogleClient(activity: Activity): GoogleSignInClient {
        return googleLoginRepository.getGoogleClient(activity)
    }
    fun signOutAndGetIntent(activity: Activity): Intent {
        return googleLoginRepository.signOutAndGetIntent(activity)
    }
}