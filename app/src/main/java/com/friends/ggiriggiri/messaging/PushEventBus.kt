package com.friends.ggiriggiri.messaging

import kotlinx.coroutines.flow.MutableSharedFlow

object PushEventBus {
    val refreshHomeEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
}
