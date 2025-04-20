package com.friends.ggiriggiri.component


import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.github.penfeizhou.animation.apng.APNGDrawable
import com.github.penfeizhou.animation.io.ByteBufferReader
import com.github.penfeizhou.animation.loader.ByteBufferLoader
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.nio.ByteBuffer

@Composable
fun ApngImageFromUrl(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var apngDrawable by remember { mutableStateOf<APNGDrawable?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.View)

    LaunchedEffect(imageUrl) {
        withContext(Dispatchers.IO) {
            try {
                val inputStream = URL(imageUrl).openStream()
                val byteArray = inputStream.readBytes()
                val byteBuffer = ByteBuffer.wrap(byteArray)
                apngDrawable = APNGDrawable(CustomByteBufferLoader(byteBuffer))
                isLoading = false
            } catch (e: Exception) {
                e.printStackTrace()
                isLoading = false
            }
        }
    }

    Box(modifier = modifier) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .shimmer(shimmerInstance)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Gray.copy(alpha = 0.8f),
                                Color.LightGray.copy(alpha = 0.2f)
                            )
                        )
                    )
            )
        } else {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    ImageView(context).apply {
                        apngDrawable?.let {
                            this.setImageDrawable(it)
                            it.start()
                        }
                    }
                },
                update = {
                    apngDrawable?.let { drawable ->
                        it.setImageDrawable(drawable)
                        drawable.start()
                    }
                }
            )
        }
    }
}
class CustomByteBufferLoader(private val byteBuffer: ByteBuffer) : ByteBufferLoader() {
    override fun obtain(): ByteBufferReader {
        return ByteBufferReader(byteBuffer)
    }

    override fun getByteBuffer(): ByteBuffer {
        return byteBuffer
    }
}

@Preview(showBackground = true)
@Composable
fun preview(){
    val imageUrl =
        "https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Confounded%20Face.png"
    ApngImageFromUrl(imageUrl,modifier = Modifier.fillMaxSize())
}

