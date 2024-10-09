package com.outthinking.audioextractor.data.disk

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.outthinking.audioextractor.otps.OneTimeProductPurchaseStatus

@Database(entities = [OneTimeProductPurchaseStatus::class], version = 1)
abstract  class OneTimePurchasesDatabase  :RoomDatabase(){

    abstract fun oneTimeProductStatusDao(): OneTimeProductPurchaseStatusDao

    companion object {

        @Volatile
        private var INSTANCE: OneTimePurchasesDatabase? = null

        @VisibleForTesting
        private val DATABASE_NAME = "otps-db"

        fun getInstance(context: Context): OneTimePurchasesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also {
                    INSTANCE = it
                }
            }

        /**
         * Set up the database configuration.
         * The SQLite database is only created when it's accessed for the first time.
         */
        private fun buildDatabase(appContext: Context): OneTimePurchasesDatabase {
            return Room.databaseBuilder(
                appContext,
                OneTimePurchasesDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}