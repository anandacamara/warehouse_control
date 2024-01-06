package org.jw.warehousecontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.domain.model.ItemEntity
import org.jw.warehousecontrol.domain.repository.SavedItemsRepository
import org.jw.warehousecontrol.presentation.model.state.MenuState

/**
 * @author Ananda Camara
 */
internal class MenuViewModel(
    private val repository: SavedItemsRepository
) : ViewModel() {
    private val databaseRef: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child(ITEMS_DATABASE_REFERENCE)

    private val _stateFlow =
        MutableStateFlow<MenuState>(MenuState.None)

    val stateFlow: StateFlow<MenuState>
        get() = _stateFlow

    fun refreshItems() = viewModelScope.launch {
        repository.cleanSavedItemsDatabase()

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    val entity = it.getValue(ItemEntity::class.java)
                    entity?.let { item -> saveItem(item) }
                }

                _stateFlow.value = MenuState.Success
            }

            override fun onCancelled(databaseError: DatabaseError) {
                _stateFlow.value = MenuState.Failure
            }
        })
    }

    private fun saveItem(item: ItemEntity) = viewModelScope.launch {
        repository.saveItems(item)
    }

    companion object {
        private const val ITEMS_DATABASE_REFERENCE = "items"
    }
}