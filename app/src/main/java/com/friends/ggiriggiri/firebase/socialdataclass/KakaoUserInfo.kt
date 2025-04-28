package com.friends.ggiriggiri.firebase.socialdataclass

import com.kakao.sdk.user.model.User

data class KakaoUserInfo(
    val email: String?,
    val nickname: String?,
    val gender: String?,
    val ageRange: String?,
    val birthday: String?,
    val profileImageUrl: String? = null
) {
    companion object {
        fun from(user: User): KakaoUserInfo {
            val account = user.kakaoAccount
            val profile = account?.profile

            return KakaoUserInfo(
                email = account?.email,
                nickname = profile?.nickname,
                gender = account?.gender?.name,
                ageRange = account?.ageRange?.name,
                birthday = account?.birthday,
                profileImageUrl = profile?.thumbnailImageUrl
            )
        }
    }
}
