package org.jw.warehousecontrol.presentation.model.state

internal sealed class ItemRegisterState {
    data class UploadImageSuccess(val url: String) : ItemRegisterState()
    data class UploadItemSuccess(val message: String) : ItemRegisterState()
    data class Failure(val message: String) : ItemRegisterState()

    object None: ItemRegisterState()
}