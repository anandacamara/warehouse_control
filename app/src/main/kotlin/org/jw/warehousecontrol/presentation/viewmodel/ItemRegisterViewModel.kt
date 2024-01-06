package org.jw.warehousecontrol.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.state.ItemRegisterState
import org.jw.warehousecontrol.presentation.model.toEntity
import org.jw.warehousecontrol.presentation.util.toDatabaseLabel

/**
 * @author Ananda Camara
 */
internal class ItemRegisterViewModel : ViewModel() {
    private val storageRef: StorageReference =
        FirebaseStorage.getInstance().let {
            it.maxUploadRetryTimeMillis = TWO_SECONDS
            it.reference.child(ITEMS_DATABASE_REFERENCE)
        }

    private val databaseRef: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child(ITEMS_DATABASE_REFERENCE)

    private val _stateFlow =
        MutableStateFlow<ItemRegisterState>(ItemRegisterState.None)

    val stateFlow: StateFlow<ItemRegisterState>
        get() = _stateFlow

    fun uploadImageToStorage(imageName: String, uri: Uri) {
        val dataBaseImageName = imageName.toDatabaseLabel()
        val imageRef: StorageReference =
            storageRef.child(STORAGE_IMAGE_PATH.format(dataBaseImageName))

        imageRef.putFile(uri).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl: String = uri.toString()
                _stateFlow.value = ItemRegisterState.UploadImageSuccess(imageUrl)
            }
        }.addOnFailureListener {
            _stateFlow.value = ItemRegisterState.Failure(UPLOAD_IMAGE_ERROR_MESSAGE)
        }
    }

    fun uploadItemToDatabase(item: ItemModel) {
        val entity = item.toEntity()

        val key = databaseRef.push().key
        key?.let {
            databaseRef.child(it).setValue(entity).addOnSuccessListener {
                _stateFlow.value = ItemRegisterState.UploadItemSuccess(UPLOAD_SUCCESS_MESSAGE)
                _stateFlow.value = ItemRegisterState.None
            }.addOnFailureListener {
                _stateFlow.value = ItemRegisterState.Failure(UPLOAD_ITEM_ERROR_MESSAGE)
                _stateFlow.value = ItemRegisterState.None
            }
        }
    }

    companion object {
        private const val TWO_SECONDS = 5000L
        private const val ITEMS_DATABASE_REFERENCE = "items"
        private const val STORAGE_IMAGE_PATH = "%s.jpg"
        private const val UPLOAD_SUCCESS_MESSAGE = "Item cadastrado com sucesso!"
        private const val UPLOAD_IMAGE_ERROR_MESSAGE =
            "Ocorreu um erro ao fazer o upload da imagem. Por favor, contate o administrador do sistema."
        private const val UPLOAD_ITEM_ERROR_MESSAGE =
            "Ocorreu um erro ao fazer o upload do item. Por favor, contate o administrador do sistema."
    }
}