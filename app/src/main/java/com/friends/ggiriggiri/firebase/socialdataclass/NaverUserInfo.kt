package com.friends.ggiriggiri.firebase.socialdataclass

import org.json.JSONObject
import java.net.URLDecoder

data class NaverUserInfo(
    val userId: String,
    val email: String,
    val name: String,
    val nickname: String,
    val profileImageUrl: String,
    val age: String,
    val gender: String,
    val birthday: String,
    val birthyear: String,
    val mobile: String,
    val mobileE164: String
) {
    companion object {
        fun from(response: JSONObject): NaverUserInfo {
            val userId = response.getString("id")
            val email = response.optString("email", "이메일 없음")
            val rawName = response.optString("name", "이름 없음")
            val nickname = response.optString("nickname", "닉네임 없음")
            val profileImage = response.optString("profile_image", "")
            val age = response.optString("age", "")
            val gender = response.optString("gender", "")
            val birthday = response.optString("birthday", "")
            val birthyear = response.optString("birthyear", "")
            val mobile = response.optString("mobile", "")
            val mobileE164 = response.optString("mobile_e164", "")
            val name = URLDecoder.decode(rawName, "UTF-8")

            return NaverUserInfo(
                userId = userId,
                email = email,
                name = name,
                nickname = nickname,
                profileImageUrl = profileImage,
                age = age,
                gender = gender,
                birthday = birthday,
                birthyear = birthyear,
                mobile = mobile,
                mobileE164 = mobileE164
            )
        }
    }
}
