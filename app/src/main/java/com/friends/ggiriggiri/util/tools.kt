package com.friends.ggiriggiri.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore

object tools{


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
        val rotation = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
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


}