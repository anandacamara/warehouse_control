package org.jw.warehousecontrol.framework

import android.content.Context
import androidx.room.Room
import org.jw.warehousecontrol.data.database.WarehouseControlDatabase
import org.jw.warehousecontrol.data.repository.BorrowedItemsRepositoryImpl
import org.jw.warehousecontrol.data.repository.LendItemsRepositoryImpl
import org.jw.warehousecontrol.data.repository.ReturnItemRepositoryImpl
import org.jw.warehousecontrol.data.repository.SavedItemsRepositoryImpl
import org.jw.warehousecontrol.domain.repository.BorrowedItemsRepository
import org.jw.warehousecontrol.domain.repository.LendItemsRepository
import org.jw.warehousecontrol.domain.repository.ReturnItemRepository
import org.jw.warehousecontrol.domain.repository.SavedItemsRepository
import org.jw.warehousecontrol.presentation.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.math.sin

/**
 * @author Ananda Camara
 */
fun createModule(context: Context) = module {
    single {
        Room.databaseBuilder(
            context,
            WarehouseControlDatabase::class.java,
            WarehouseControlDatabase.DATABASE_NAME
        ).build()
    }
    single { get<WarehouseControlDatabase>().borrowedItemDao() }
    single { get<WarehouseControlDatabase>().itemDao() }
    single { get<WarehouseControlDatabase>().volunteerDao() }
    single<SavedItemsRepository> { SavedItemsRepositoryImpl(get(), get()) }
    single<BorrowedItemsRepository> { BorrowedItemsRepositoryImpl(get()) }
    single<ReturnItemRepository> { ReturnItemRepositoryImpl(get()) }
    single<LendItemsRepository> { LendItemsRepositoryImpl(get(), get(), get()) }

    viewModel { ListViewModel(get()) }
    viewModel { ItemDetailViewModel(get(), get(), get()) }
    viewModel { VolunteerDetailsViewModel(get(), get(), get()) }
    viewModel { LendViewModel(get(), get()) }
    viewModel { ItemRegisterViewModel() }
    viewModel { MenuViewModel(get()) }
}