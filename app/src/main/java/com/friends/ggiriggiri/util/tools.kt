package com.friends.ggiriggiri.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
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
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
        //onResult: (Boolean, String) -> Unit
    ) {
        val json = JSONObject().apply {
            put("title", title)
            put("body", body)
            put("token", token)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://sendnotificationtogroup-ly57f6pi7q-du.a.run.app")
            .post(requestBody)
            .build()


        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "전송 실패", e)
                //onResult(false, e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: ""
                Log.d("FCM", "응답: $body")
                //onResult(response.isSuccessful, body)
            }
        })
    }

    fun sendPushNotificationToGroup(tokens: List<String>, title: String, body: String) {
        Log.d("FCM", "푸시 전송 시작: 토큰 ${tokens.size}개")

        val json = JSONObject().apply {
            put("title", title)
            put("body", body)
            put("tokens", JSONArray(tokens))
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://asia-northeast3-ggiriggiri-c33b2.cloudfunctions.net/sendNotificationToGroup")
            .post(requestBody)
            .build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("FCM", "전송 실패", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body?.string()
                Log.d("FCM", "응답: $responseText")
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

    fun showNotificationPermissionDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle("알림 권한이 필요합니다")
            .setMessage("앱에서 알림을 받기 위해 설정에서 알림 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                }
                activity.startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    fun isNotificationPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Android 12 이하는 알림 권한 필요 없음
        }
    }


    //val formatted = formatMillisToDateTime(System.currentTimeMillis())
    //println(formatted) // 출력 예: 2025.03.04 14:57
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatMillisToDateTime(millis: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
            .withZone(ZoneId.of("Asia/Seoul")) // 한국 시간대
        return formatter.format(Instant.ofEpochMilli(millis.toLong()))
    }

    //알림설정화면
    fun openAppNotificationSettings(context: Context) {
        val intent = Intent().apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            // 아래는 일부 기기에서 필요
            putExtra("app_package", context.packageName)
            putExtra("app_uid", context.applicationInfo.uid)
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // 구형 기기 대응: 앱 상세 설정 화면으로 이동
            val fallbackIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            context.startActivity(fallbackIntent)
        }
    }

    //System.currentTimeMillis()의 포맷변환
    fun formatMillisToDateTime(millis: Long): String {
        val sdf = java.text.SimpleDateFormat("yyyy.MM.dd HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(millis))
    }


}