package com.friends.ggiriggiri.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.friends.ggiriggiri.R
import com.friends.ggiriggiri.util.tools.correctImageOrientation
import com.friends.ggiriggiri.util.tools.createImageUri


@Composable
fun UploadImage(
    onImageSelected: (Uri) -> Unit
) {

    val context = LocalContext.current

    // 빈 파일 Uri 생성용 remember
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // 카메라 런처 (Uri 저장 방식)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        cameraImageUri?.let { uri ->
            if (success) {
                val correctedUri = correctImageOrientation(context, uri)
                selectedImageUri = correctedUri
                onImageSelected(correctedUri)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .size(400.dp)
    ) {
        if (selectedImageUri == null) { //이미지가 없을때
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(400.dp)            // 박스 크기 지정
                    .drawBehind {
                        // 점선 패턴을 위한 PathEffect 설정 (선길이 10px, 간격 10px 예시)
                        val dashWidth = 4.dp.toPx()
                        val dashGap = 4.dp.toPx()
                        val strokeWidth = 2.dp.toPx()   // 테두리 두께
                        // 점선 효과가 적용된 Stroke 객체 준비
                        val stroke = Stroke(
                            width = strokeWidth,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(dashWidth, dashGap),
                                0f
                            )
                        )
                        // 테두리를 그릴 영역 계산 (stroke의 절반은 내부로 오도록 오프셋)
                        val halfStroke = strokeWidth / 2
                        drawRoundRect(
                            color = Color.Gray,                         // 테두리 색상
                            topLeft = Offset(halfStroke, halfStroke),   // 좌상단 오프셋
                            size = Size(
                                size.width - strokeWidth,
                                size.height - strokeWidth
                            ), // 테두리 크기
                            style = stroke                              // Stroke 스타일 (점선 적용)
                            // cornerRadius = CornerRadius(radiusPx)   // 필요하면 모서리 둥글기 조절
                        )
                    }
                    .padding(50.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize(),
                            painter = painterResource(id = R.drawable.add_photo_alternate_24px),
                            tint = Color.Gray,
                            contentDescription = "기본이미지"
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(.7f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "업로드 된 이미지가 없습니다.",
                            fontFamily = FontFamily(Font(R.font.nanumsquareextrabold)),
                            color = Color.Gray
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp),
                            text = "등록할 이미지를 업로드해주세요.",
                            fontFamily = FontFamily(Font(R.font.nanumsquareregular)),
                            color = Color.Gray
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomButton(
                            text = "이미지 업로드",
                            onClick = {
                                //카메라 런처 실행
                                val uri = createImageUri(context)
                                cameraImageUri = uri
                                cameraLauncher.launch(uri)
                            }
                        )
                    }
                }
            }
        } else { //이미지가 있을때
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(400.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5f)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = selectedImageUri),
                        contentDescription = "선택한 이미지",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    CustomButton(
                        text = "다시 촬영 하기",
                        paddingTop = 20.dp,
                        onClick = {
                            //카메라 런처 실행
                            val uri = createImageUri(context)
                            cameraImageUri = uri
                            cameraLauncher.launch(uri)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUploadImage() {

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imageUrl = imageUri?.toString()
        ?: "https://firebasestorage.googleapis.com/v0/b/ggiriggiri-c33b2.firebasestorage.app/o/request_images%2F1740013756884.jpg?alt=media&token=16229d84-ea3f-4a27-9861-89dd3de97f26"
    UploadImage(
        onImageSelected = { uri -> imageUri = uri }
    )
}