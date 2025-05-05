package com.friends.ggiriggiri.screen.ui.memories.request.viewonerequest

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.friends.ggiriggiri.screen.viewmodel.memories.ViewOneRequestViewModel
import com.friends.ggiriggiri.util.tools.formatMillisToDateTime
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestItem(
    modifierItem: Modifier,
    viewModel: ViewOneRequestViewModel,
    requestComponent: Boolean,
    responseIndex:Int? = null
) {

    Column(
        modifier = modifierItem
    ) {
        if (requestComponent){
            // 사용자 정보 헤더
            RequestHeader(
                imageUrl = viewModel.requestMap.value["requesterProfileImage"].toString(),
                nickname = viewModel.requestMap.value["requesterName"].toString(),
                date = viewModel.requestMap.value["requestTime"]
                    ?.toLongOrNull()
                    ?.let { formatMillisToDateTime(it.toString()) }
                    .orEmpty(),
                isLoading = viewModel.isRequestInfoLoading.value,
            )

            // 본문 이미지 (비율 유지 & shimmer)
            RequestImage(
                imageUrl = viewModel.requestMap.value["requestImage"].toString()
            )
            //요청내용
            RequestText(
                text = viewModel.requestMap.value["requestMessage"].toString(),
                isLoading = viewModel.isRequestInfoLoading.value
            )
        }else{
            val responseMap = responseIndex?.let {
                viewModel.responsesMapList.value.getOrNull(it)
            }
            // 사용자 정보 헤더
            RequestHeader(
                imageUrl = responseMap?.get("userProfileImage").orEmpty(),
                nickname = responseMap?.get("userName").orEmpty(),
                date = responseMap?.get("responseTime")?.let { formatMillisToDateTime(it) }.orEmpty(),
                isLoading = viewModel.isRequestInfoLoading.value,
            )
            // 본문 이미지 (비율 유지 & shimmer)
            RequestImage(
                imageUrl = responseMap?.get("responseImage").orEmpty()
            )
            //응답내용
            RequestText(
                text = responseMap?.get("responseMessage").orEmpty(),
                isLoading = viewModel.isRequestInfoLoading.value
            )
        }
    }
}

@Preview
@Composable
fun PreviewRequestItem(){
    //RequestItem(Modifier)
}

