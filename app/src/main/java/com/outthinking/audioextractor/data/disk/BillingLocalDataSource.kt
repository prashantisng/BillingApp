package com.outthinking.audioextractor.data.disk

import com.outthinking.audioextractor.otps.OneTimeProductPurchaseStatus
import kotlinx.coroutines.flow.Flow

class BillingLocalDataSource private constructor(
    private val subscriptionStatusDao: SubscriptionStatusDao,
    private val oneTimeProductPurchaseStatusDao: OneTimeProductPurchaseStatusDao
)  {
    /**
     * Get the list of subscriptions from the DAO.
     */
    fun getSubscriptions(): Flow<List<SubscriptionStatus>> = subscriptionStatusDao.getAll()

    /**
     * Get the list of one-time product purchases from the DAO.
     */
    fun getOneTimeProducts(): Flow<List<OneTimeProductPurchaseStatus>> =
        oneTimeProductPurchaseStatusDao.getAll()

    /**
     * Update the list of subscriptions in the DAO.
     */
    suspend fun updateSubscriptions(subscriptions: List<SubscriptionStatus>) {
        // Delete existing subscriptions.
       // subscriptionStatusDao.deleteAll()
        // Put new subscriptions data into localDataSource.
        subscriptionStatusDao.insertAll(subscriptions)
    }

    /**
     * Update the list of one-time product purchases in the DAO.
     */
    suspend fun updateOneTimeProductPurchases(
        oneTimeProductPurchases: List<OneTimeProductPurchaseStatus>
    ) {
        // Delete existing one time products.
        //oneTimeProductPurchaseStatusDao.deleteAll()
        // Put new one time products data into localDataSource.
        oneTimeProductPurchaseStatusDao.insertAll(oneTimeProductPurchases)
    }

    /**
     * Delete local user data when the user signs out.
     */
    suspend fun deleteLocalUserSubscriptionsData() = updateSubscriptions(listOf())
    suspend fun deleteLocalUserOneTimeProductPurchases() = updateOneTimeProductPurchases(listOf())

    companion object {

        @Volatile
        private var INSTANCE: BillingLocalDataSource? = null

        fun getInstance(
            subscriptionStatusDao: SubscriptionStatusDao,
            oneTimeProductPurchaseStatusDao: OneTimeProductPurchaseStatusDao
        ): BillingLocalDataSource =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BillingLocalDataSource(
                    subscriptionStatusDao, oneTimeProductPurchaseStatusDao
                ).also { INSTANCE = it }
            }
    }

}