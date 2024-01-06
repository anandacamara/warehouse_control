package org.jw.warehousecontrol.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.jw.warehousecontrol.data.model.VolunteerModel

/**
 * @author Ananda Camara
 */
@Dao
internal abstract class VolunteerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertVolunteer(volunteer: VolunteerModel)

    @Delete
    abstract suspend fun deleteVolunteer(volunteer: VolunteerModel)

    @Query("SELECT * FROM volunteers_table")
    abstract fun getAllVolunteers(): Flow<List<VolunteerModel>>
}