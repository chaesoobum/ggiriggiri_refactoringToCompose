package com.friends.ggiriggiri.firebase.socialdataclass

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

data class GoogleUserInfo(
    val userId: String,
    val email: String,
    val name: String,
    val givenName: String,
    val familyName: String,
    val profileImageUrl: String
) {
    companion object {
        fun from(account: GoogleSignInAccount): GoogleUserInfo {
            return GoogleUserInfo(
                userId = account.id ?: "",
                email = account.email ?: "",
                name = account.displayName ?: "",
                givenName = account.givenName ?: "",
                familyName = account.familyName ?: "",
                profileImageUrl = account.photoUrl?.toString() ?: ""
            )
        }
    }
}
