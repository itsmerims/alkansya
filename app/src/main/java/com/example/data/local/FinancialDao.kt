package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialDao {

    // Accounts
    @Query("SELECT * FROM financial_accounts ORDER BY id ASC")
    fun getAllAccounts(): Flow<List<FinancialAccount>>

    @Query("SELECT * FROM financial_accounts WHERE id = :id LIMIT 1")
    suspend fun getAccountById(id: Long): FinancialAccount?

    @Query("SELECT * FROM financial_accounts WHERE providerName = :providerName")
    suspend fun getAccountsByProvider(providerName: String): List<FinancialAccount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: FinancialAccount): Long

    @Update
    suspend fun updateAccount(account: FinancialAccount)

    @Delete
    suspend fun deleteAccount(account: FinancialAccount)

    // Transactions
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE accountId = :accountId ORDER BY timestamp DESC")
    fun getTransactionsForAccount(accountId: Long): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @androidx.room.Transaction
    suspend fun addTransactionAndUpdateBalance(transaction: Transaction) {
        val account = getAccountById(transaction.accountId) ?: return
        
        val newBalance = when (transaction.transactionType) {
            "INCOME" -> account.currentBalance + transaction.amount
            "EXPENSE" -> account.currentBalance - transaction.amount
            else -> account.currentBalance // For simple transfer implementation, adjust as needed
        }
        
        val updatedAccount = account.copy(currentBalance = newBalance)
        updateAccount(updatedAccount)
        insertTransaction(transaction)
    }

    // Savings Vaults
    @Query("SELECT * FROM savings_vaults ORDER BY id ASC")
    fun getAllVaults(): Flow<List<SavingsVault>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVault(vault: SavingsVault): Long

    @Update
    suspend fun updateVault(vault: SavingsVault)

    @Delete
    suspend fun deleteVault(vault: SavingsVault)
}
