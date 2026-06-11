package com.example.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "financial_accounts")
data class FinancialAccount(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String, // WALLET, DIGITAL_BANK, TRADITIONAL_BANK, FOREIGN_BANK
    val providerName: String, // Matches the Institution ID (e.g. "GCASH", "BDO")
    val startingBalance: Double,
    val currentBalance: Double,
    val cardLimit: Double = 50000.0,
    val isFrozen: Boolean = false,
    val cardColorIndex: Int = 0,
    val currency: String = "PHP"
)

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = FinancialAccount::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["accountId"])]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val accountId: Long,
    val amount: Double,
    val transactionType: String, // INCOME, EXPENSE, TRANSFER
    val merchantName: String,
    val timestamp: Long,
    val isAutomatedFromSMS: Boolean,
    val category: String = "Others"
)

@Entity(tableName = "savings_vaults")
data class SavingsVault(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentSaved: Double,
    val fundingAccountId: Long
)

