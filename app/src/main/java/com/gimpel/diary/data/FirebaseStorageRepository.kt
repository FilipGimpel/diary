package com.gimpel.diary.data

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class FirebaseStorageRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) {
    suspend fun uploadBitmapAndReturnUri(bitmap: Bitmap, storagePath: String): Result<String> {
        return try {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val storageRef = firebaseStorage.reference.child(storagePath)
            storageRef.putBytes(data).await() // Upload the image
            val downloadUri = storageRef.downloadUrl.await().toString() // Get the download URI

            Result.success(downloadUri) // Return the URI as a success
        } catch (e: Exception) {
            Result.failure(e) // Return the exception as a failure
        }
    }
}