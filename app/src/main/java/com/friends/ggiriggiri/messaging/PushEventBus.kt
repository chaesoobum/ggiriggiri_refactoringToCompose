package com.friends.ggiriggiri.messaging

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

object PushEventBus {
    val refreshRequestEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val refreshQuestionEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
}

//설정	의미	설명
//replay = 0	과거 이벤트를 collect 시점에 재전송 ❌	새로 collect할 때 오래된 데이터 재처리 안 함
//extraBufferCapacity = 1	emit된 값이 collect 전에 잠깐 대기 가능	수신자가 늦게 collect해도 1개까지는 놓치지 않음