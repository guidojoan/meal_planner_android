package com.astutify.mealplanner.coreui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class ImagePickerUtils @Inject constructor(
    private val context: Context
) {

    fun isValidImageType(imageUri: Uri): Boolean {
        return when (context.contentResolver.getType(imageUri)) {
            "image/jpg", "image/jpeg" -> true
            else -> false
        }
    }

    fun getMultipartImageBody(imageUri: Uri): MultipartBody.Part? {
        return getImage(imageUri)?.let {
            MultipartBody.Part.createFormData(
                REQUEST_BODY_TAG,
                IMAGE_NAME + it.extension,
                it.requestBody
            )
        }
    }

    private fun getImage(imageUri: Uri): ImageDescriptor? {
        return try {
            val bitmap = getBitmap(imageUri)
            val imageCompressed = compressJPEGImage(bitmap)
            val extension = getExtension(imageUri)

            ImageDescriptor(
                imageCompressed.toRequestBody(IMAGE_MEDIA_TYPE.toMediaTypeOrNull()),
                extension!!
            )
        } catch (error: Throwable) {
            null
        }
    }

    private fun getInputStream(imageUri: Uri): InputStream {
        return context.contentResolver.openInputStream(imageUri)!!
    }

    private fun getExtension(imageUri: Uri): String? {
        return context.contentResolver.getType(imageUri)?.substringAfter("/")
    }

    private fun compressJPEGImage(original: Bitmap): ByteArray {
        val resizedImage = resizeBitmap(original)
        val out = ByteArrayOutputStream()
        resizedImage.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, out)
        return out.toByteArray()
    }

    private fun getBitmap(imageUri: Uri): Bitmap {
        val ei = ExifInterface(getInputStream(imageUri))
        val bitmap = BitmapFactory.decodeStream(getInputStream(imageUri))
        return when (
            ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        ) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var newWidth = -1
        var newHeight = -1
        val multFactor: Float
        when {
            originalHeight > originalWidth -> {
                newHeight = SCALED_SIZE
                multFactor = originalWidth.toFloat() / originalHeight.toFloat()
                newWidth = (newHeight * multFactor).toInt()
            }
            originalWidth > originalHeight -> {
                newWidth = SCALED_SIZE
                multFactor = originalHeight.toFloat() / originalWidth.toFloat()
                newHeight = (newWidth * multFactor).toInt()
            }
            originalHeight == originalWidth -> {
                newHeight = SCALED_SIZE
                newWidth = SCALED_SIZE
            }
        }
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false)
    }

    private data class ImageDescriptor(
        val requestBody: RequestBody,
        val extension: String
    )

    companion object {
        private const val IMAGE_MEDIA_TYPE = "image/*"
        private const val REQUEST_BODY_TAG = "recipeImage"
        private const val IMAGE_NAME = "recipe."
        private const val SCALED_SIZE = 2048
        private const val IMAGE_QUALITY = 60
    }
}
