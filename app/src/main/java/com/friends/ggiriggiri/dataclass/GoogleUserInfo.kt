package com.friends.ggiriggiri.dataclass

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import org.json.JSONObject
import java.net.URLDecoder

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
