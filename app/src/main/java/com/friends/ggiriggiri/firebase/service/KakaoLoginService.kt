package com.friends.ggiriggiri.firebase.service

import android.app.Activity
import com.friends.ggiriggiri.firebase.socialdataclass.KakaoUserInfo
import com.friends.ggiriggiri.firebase.repository.KakaoLoginRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class KakaoLoginService @Inject constructor(
    val kakaoLoginRepository: KakaoLoginRepository
) {
    fun loginWithKakaoTalk(
        activity: Activity,
        onSuccess: (String, KakaoUserInfo) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        kakaoLoginRepository.loginWithKakaoTalk(activity, onSuccess, onFailure)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun loginWithKakaoTalkSuspend(activity: Activity): Pair<String, KakaoUserInfo> =
        suspendCancellableCoroutine { continuation -> // 중단 지점을 재개 시킴
            kakaoLoginRepository.loginWithKakaoTalk(
                activity = activity,
                onSuccess = { token, userInfo ->
                    // resume : 성공
                    continuation.resume(Pair(token, userInfo), null)
                },
                onFailure = { error ->
                    // error : 실패
                    continuation.cancel(error) // 취소로 예외 전달
                }
            )
        }
}