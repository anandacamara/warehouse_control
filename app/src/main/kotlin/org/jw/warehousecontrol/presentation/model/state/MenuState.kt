package org.jw.warehousecontrol.presentation.model.state

sealed class MenuState {
    object Success : MenuState()
    object Failure : MenuState()
    object None : MenuState()
}
