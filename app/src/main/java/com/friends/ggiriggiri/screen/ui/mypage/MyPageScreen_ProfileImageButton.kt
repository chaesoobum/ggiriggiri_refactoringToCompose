package com.friends.ggiriggiri.screen.ui.mypage

import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.component.CustomButton
import com.friends.ggiriggiri.util.tools.correctImageOrientation
import com.friends.ggiriggiri.util.tools.createImageUri
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileImageButton(
    imageUrl: String?,
    onImageSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    // 빈 파일 Uri 생성용 remember
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    // 갤러리 런처
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { onImageSelected(it) } }

    // 카메라 런처 (Uri 저장 방식)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        cameraImageUri?.let { uri ->
            if (success) {
                val correctedUri = correctImageOrientation(context, uri)
                onImageSelected(correctedUri)
            }
        }
    }

    val shimmerInstance = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = ShimmerTheme(
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 500,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            blendMode = androidx.compose.ui.graphics.BlendMode.SrcOver,
            rotation = 0f, // 또는 20f로 기울기 효과
            shaderColors = listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.3f),
                Color.LightGray.copy(alpha = 0.6f)
            ),
            shaderColorStops = null, // 자동 분포
            shimmerWidth = 200.dp // shimmer wave 넓이
        )
    )

    val model = imageUrl
        ?: "android.resource://${context.packageName}/${R.drawable.ic_default_profile}"

    Row(modifier = Modifier.padding(top = 20.dp)) {
        Box(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
        ) {
            SubcomposeAsyncImage(
                model = model,
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clickable {
                        showSheet = true
                    },
                contentScale = ContentScale.Crop
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmer(shimmerInstance)
                                .background(Color.LightGray, CircleShape)
                        )
                    }

                    is AsyncImagePainter.State.Error -> {
                        Image(
                            painter = painterResource(id = R.drawable.ic_default_profile),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }
        }
        Box(modifier = Modifier.weight(1f))
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "프로필 이미지 변경",
                    fontFamily = FontFamily(Font(R.font.nanumsquareextrabold)),
                    //fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                CustomButton(
                    text = "갤러리",
                    onClick = {
                        showSheet = false
                        galleryLauncher.launch("image/*")
                    },
                    paddingTop = 20.dp
                )

                CustomButton(
                    text = "카메라",
                    onClick = {
                        showSheet = false
                        val uri = createImageUri(context)
                        cameraImageUri = uri
                        cameraLauncher.launch(uri)
                    },
                    paddingTop = 5.dp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun preImage() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imageUrl = imageUri?.toString()
        ?: "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26"

    ProfileImageButton(
        imageUrl = imageUrl,
        onImageSelected = { uri -> imageUri = uri }
    )
}