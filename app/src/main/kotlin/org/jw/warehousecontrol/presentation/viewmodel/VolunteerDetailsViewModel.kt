package org.jw.warehousecontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.domain.model.ItemEntity
import org.jw.warehousecontrol.domain.repository.BorrowedItemsRepository
import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.VolunteerModel
import org.jw.warehousecontrol.presentation.model.state.VolunteerDetailState
import org.jw.warehousecontrol.presentation.model.toEntity
import org.jw.warehousecontrol.presentation.model.toModel

/**
 * @author Ananda Camara
 */
internal class VolunteerDetailsViewModel(
    private val itemsRepository: BorrowedItemsRepository,
): ViewModel() {
    private val databaseRef: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child(ITEMS_DATABASE_REFERENCE)

    private val _stateFlow =
        MutableStateFlow<VolunteerDetailState>(VolunteerDetailState.None)

    val stateFlow: StateFlow<VolunteerDetailState>
        get() = _stateFlow

    fun getRegisteredItems() = viewModelScope.launch {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = mutableListOf<ItemModel>()

                dataSnapshot.children.forEach {
                    val entity = it.getValue(ItemEntity::class.java)
                    entity?.let { item -> items.add(item.toModel()) }
                }

                _stateFlow.value = VolunteerDetailState.Success(items)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                _stateFlow.value = VolunteerDetailState.Failure
            }
        })
    }

    fun lendItem(item: ItemModel, volunteer: VolunteerModel) = viewModelScope.launch {
        itemsRepository.lendItems(item.toEntity(), to = volunteer.toEntity())
        _stateFlow.value = VolunteerDetailState.ShowSuccessMessage(LEND_ITEM_SUCCESS)
        _stateFlow.value = VolunteerDetailState.None
    }

    fun returnItem(item: ItemModel, volunteer: VolunteerModel) = viewModelScope.launch {
        itemsRepository.returnItem(item = item.toEntity(), from = volunteer.toEntity())
        _stateFlow.value = VolunteerDetailState.ShowSuccessMessage(RETURN_ITEM_SUCCESS)
        _stateFlow.value = VolunteerDetailState.None
    }

    companion object {
        private const val ITEMS_DATABASE_REFERENCE = "items"
        private const val LEND_ITEM_SUCCESS = "Retirada de item registrada com sucesso"
        private const val RETURN_ITEM_SUCCESS = "Item devolvido com sucesso"
    }
}