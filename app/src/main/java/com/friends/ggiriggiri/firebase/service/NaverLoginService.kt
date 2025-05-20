package com.friends.ggiriggiri.firebase.service

import android.app.Activity
import com.friends.ggiriggiri.firebase.socialdataclass.NaverUserInfo
import com.friends.ggiriggiri.firebase.repository.NaverLoginRepository
import com.friends.ggiriggiri.firebase.socialdataclass.KakaoUserInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class NaverLoginService @Inject constructor(
    val naverLoginRepository: NaverLoginRepository
){
    fun loginWithNaver(
        activity: Activity,
        onSuccess: (String, NaverUserInfo) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        naverLoginRepository.loginWithNaver(activity, onSuccess, onFailure)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun loginWithNaverTalkSuspend(activity: Activity): Pair<String, NaverUserInfo> =
        suspendCancellableCoroutine { continuation -> // 중단 지점을 재개 시킴
            naverLoginRepository.loginWithNaver(
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