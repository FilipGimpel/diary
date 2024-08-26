package com.gimpel.diary.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun addOrUpdateDiaryEntry(diaryEntry: DiaryEntry): Result<Void?> { // todo do i need the return type?
        return try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
                ?: throw Exception("User not authenticated")

            // if id is empty we are adding a new entry, otherwise we are updating an existing one
            if (diaryEntry.id.isEmpty()) {
                val documentReference =
                    firestore.collection("users").document(userId).collection("diaryEntries")
                        .document()
                val firestoreDiaryEntry = diaryEntry.toFirestoreDiaryEntry(documentReference.id)
                documentReference.set(firestoreDiaryEntry).await()
            } else {
                val documentReference =
                    firestore.collection("users").document(userId).collection("diaryEntries")
                        .document(diaryEntry.id)

                val diaryEntryId = diaryEntry.id.ifEmpty { documentReference.id }

                val firestoreDiaryEntry = diaryEntry.toFirestoreDiaryEntry(diaryEntryId)

                documentReference.set(firestoreDiaryEntry).await()
            }

            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addDiaryEntries(diaryEntries: List<DiaryEntry>) {
        val userId =
            FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("User not authenticated")
        val batch = firestore.batch()

        diaryEntries.forEach { diaryEntry ->
            val documentReference =
                firestore.collection("users").document(userId).collection("diaryEntries").document()
            val firestoreDiaryEntry = diaryEntry.toFirestoreDiaryEntry(documentReference.id)
            batch.set(documentReference, firestoreDiaryEntry)
        }

        batch.commit().await()
    }

    suspend fun getDiaryEntry(id: String): DiaryEntry {
        val userId =
            FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("User not authenticated")
        val documentSnapshot =
            firestore.collection("users").document(userId).collection("diaryEntries").document(id)
                .get().await()

        return documentSnapshot.toObject(FirestoreDiaryEntry::class.java)?.toDiaryEntry() ?: throw Exception("Diary entry not found")
    }

    fun getDiaryEntriesFlow(): Flow<List<DiaryEntry>> {
        val userId =
            FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("User not authenticated")
        val collectionRef =
            firestore.collection("users").document(userId).collection("diaryEntries")

        return collectionRef.snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { document ->
                document.toObject(FirestoreDiaryEntry::class.java)?.toDiaryEntry()
            }.sortedBy { it.date }
        }
    }
}