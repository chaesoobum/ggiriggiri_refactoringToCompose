package com.friends.ggiriggiri.util

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

object tools {


    fun createImageUri(context: Context): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "profile_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )!!
    }

    fun correctImageOrientation(context: Context, uri: Uri): Uri {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return uri
        val exif = ExifInterface(inputStream)
        val rotation = when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        inputStream.close()

        if (rotation == 0f) return uri

        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val matrix = Matrix().apply { postRotate(rotation) }
        val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        val outUri = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            rotated,
            "corrected_${System.currentTimeMillis()}",
            null
        )
        return Uri.parse(outUri)
    }

    @Composable
    fun rememberDefaultShimmer() = com.friends.ggiriggiri.util.rememberDefaultShimmer()

    //푸시알림 보내는 예제
    fun sendPushNotification(
        token: String,
        title: String,
        body: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val json = JSONObject().apply {
            put("title", title)
            put("body", body)
            put("token", token)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://asia-northeast3-ggiriggiri-c33b2.cloudfunctions.net/sendNotification")
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "전송 실패", e)
                onResult(false, e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: ""
                Log.d("FCM", "응답: $body")
                onResult(response.isSuccessful, body)
            }
        })
    }

    //알림 권한 받는 함수
    //requestNotificationPermissionIfNeeded(this)
    // import android.Manifest
    fun requestNotificationPermissionIfNeeded(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            )

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1010
                )
            }
        }
    }

    //남은 시간
    fun minutesAndSeconds(pastTimeMillis: Long): Pair<Long, Long> {
        val thirtyMinutesLater = pastTimeMillis + (30 * 60 * 1000)
        val now = System.currentTimeMillis()
        val remainingMillis = thirtyMinutesLater - now

        return if (remainingMillis > 0) {
            val minutes = remainingMillis / (60 * 1000)
            val seconds = (remainingMillis % (60 * 1000)) / 1000
            Pair(minutes, seconds)
        } else {
            Pair(0, 0)
        }
    }

}