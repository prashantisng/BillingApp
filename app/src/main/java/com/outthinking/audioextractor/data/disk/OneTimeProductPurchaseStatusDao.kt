package com.outthinking.audioextractor.data.disk

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.outthinking.audioextractor.otps.OneTimeProductPurchaseStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OneTimeProductPurchaseStatusDao {
    @Query("SELECT * FROM oneTimeProductPurchases")
    fun getAll(): Flow<List<OneTimeProductPurchaseStatus>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(oneTimeProducts: List<OneTimeProductPurchaseStatus>)

   /* @Query("DELETE FROM oneTimeProductPurchases")
    suspend fun deleteAll()*/
}