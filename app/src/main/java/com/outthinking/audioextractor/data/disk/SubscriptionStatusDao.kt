package com.outthinking.audioextractor.data.disk

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionStatusDao {
    @Query("SELECT * FROM subscriptions")
    fun getAll(): Flow<List<SubscriptionStatus>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(subscriptions: List<SubscriptionStatus>)

/*    @Query("DELETE FROM subscriptions")
    suspend fun deleteAll()*/
}