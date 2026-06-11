package com.example.data.repository

import android.util.Log
import com.example.data.local.FinancialDao
import com.example.data.local.FinancialAccount
import com.example.data.local.Transaction
import com.example.data.local.SavingsVault
import com.example.data.model.FinancialCatalog
import kotlinx.coroutines.flow.Flow
import java.util.regex.Pattern

class FinancialRepository(private val financialDao: FinancialDao) {

    val allAccounts: Flow<List<FinancialAccount>> = financialDao.getAllAccounts()
    val allTransactions: Flow<List<Transaction>> = financialDao.getAllTransactions()
    val allVaults: Flow<List<SavingsVault>> = financialDao.getAllVaults()

    suspend fun insertAccount(account: FinancialAccount): Long {
        return financialDao.insertAccount(account)
    }

    suspend fun getAccountById(id: Long): FinancialAccount? {
        return financialDao.getAccountById(id)
    }

    suspend fun updateAccount(account: FinancialAccount) {
        financialDao.updateAccount(account)
    }

    suspend fun deleteAccount(account: FinancialAccount) {
        financialDao.deleteAccount(account)
    }

    suspend fun insertTransaction(transaction: Transaction): Long {
        return financialDao.insertTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        // Adjust balance back
        val account = financialDao.getAccountById(transaction.accountId)
        if (account != null) {
            val adjustedBalance = when (transaction.transactionType) {
                "INCOME" -> account.currentBalance - transaction.amount
                "EXPENSE" -> account.currentBalance + transaction.amount
                else -> account.currentBalance
            }
            financialDao.updateAccount(account.copy(currentBalance = adjustedBalance))
        }
        financialDao.deleteTransaction(transaction)
    }

    suspend fun addTransactionAndUpdateBalance(transaction: Transaction) {
        financialDao.addTransactionAndUpdateBalance(transaction)
    }

    /**
     * Process an incoming SMS message.
     * Matches standard Philippine transaction patterns for GCash and Maya.
     * Extracts values securely and registers the transaction inside the database.
     */
    suspend fun processIncomingSMS(sender: String, messageBody: String): Boolean {
        Log.d("AlkansyaParser", "Processing SMS from: $sender. Body: $messageBody")
        
        // 1. Identify which institution this belongs to based on the Sender ID
        val institution = FinancialCatalog.findBySenderId(sender) ?: return false
        Log.d("AlkansyaParser", "Matched institution: ${institution.displayName} for sender: $sender")

        // 2. Run regex parsers
        var parsedAmount: Double? = null
        var parsedMerchant: String? = null
        var isExpense = true // Default to expense

        // Pre-compile Regex patterns for safety
        val gcashPayRegex = Pattern.compile(
            "You have paid (?:PHP|Php|php)?\\s*([\\d,]+(?:\\.\\d{1,2})?)\\s+of GCash to\\s+([^.]+?)(?:\\.|\\s*Ref|$)", 
            Pattern.CASE_INSENSITIVE
        )
        val gcashSendRegex = Pattern.compile(
            "You have sent (?:PHP|Php|php)?\\s*([\\d,]+(?:\\.\\d{1,2})?)\\s+to\\s+([^.]+?)(?:\\.|\\s*on|$)", 
            Pattern.CASE_INSENSITIVE
        )
        val mayaSpendRegex = Pattern.compile(
            "You have spent (?:PHP|Php|php)?\\s*([\\d,]+(?:\\.\\d{1,2})?)\\s+at\\s+([^.]+?)(?:\\.|\\s*Ref|$)", 
            Pattern.CASE_INSENSITIVE
        )

        // Try GCash Pay Match
        val gcashPayMatcher = gcashPayRegex.matcher(messageBody)
        if (gcashPayMatcher.find()) {
            parsedAmount = gcashPayMatcher.group(1)?.replace(",", "")?.toDoubleOrNull()
            parsedMerchant = gcashPayMatcher.group(2)?.trim()
            isExpense = true
            Log.d("AlkansyaParser", "Matched GCash Pay: Amount=$parsedAmount, Merchant=$parsedMerchant")
        }

        // Try GCash Send Match if not matched yet
        if (parsedAmount == null) {
            val gcashSendMatcher = gcashSendRegex.matcher(messageBody)
            if (gcashSendMatcher.find()) {
                parsedAmount = gcashSendMatcher.group(1)?.replace(",", "")?.toDoubleOrNull()
                parsedMerchant = gcashSendMatcher.group(2)?.trim()
                isExpense = true
                Log.d("AlkansyaParser", "Matched GCash Send: Amount=$parsedAmount, Recipient=$parsedMerchant")
            }
        }

        // Try Maya Spend Match if not matched yet
        if (parsedAmount == null) {
            val mayaSpendMatcher = mayaSpendRegex.matcher(messageBody)
            if (mayaSpendMatcher.find()) {
                parsedAmount = mayaSpendMatcher.group(1)?.replace(",", "")?.toDoubleOrNull()
                parsedMerchant = mayaSpendMatcher.group(2)?.trim()
                isExpense = true
                Log.d("AlkansyaParser", "Matched Maya Spend: Amount=$parsedAmount, Merchant=$parsedMerchant")
            }
        }

        // If amount couldn't be parsed, stop
        if (parsedAmount == null || parsedAmount <= 0.0) {
            Log.e("AlkansyaParser", "Failed to parse a valid transaction amount from SMS.")
            return false
        }

        val merchantName = parsedMerchant ?: "Unknown Merchant"

        // 3. Find matching declared account in DB
        var accounts = financialDao.getAccountsByProvider(institution.id)
        
        if (accounts.isEmpty()) {
            Log.d("AlkansyaParser", "No active account found for provider ${institution.id}. Provisioning default account.")
            // Auto-provision a default account so that the parsed transaction is not lost
            val defaultAccount = FinancialAccount(
                name = "Automated ${institution.displayName}",
                type = institution.type.name,
                providerName = institution.id,
                startingBalance = 0.0,
                currentBalance = 0.0
            )
            val newAccountId = financialDao.insertAccount(defaultAccount)
            accounts = listOf(defaultAccount.copy(id = newAccountId))
        }

        // Add the transaction to the first matching account
        val targetAccount = accounts.first()
        val transaction = Transaction(
            accountId = targetAccount.id,
            amount = parsedAmount,
            transactionType = if (isExpense) "EXPENSE" else "INCOME",
            merchantName = merchantName,
            timestamp = System.currentTimeMillis(),
            isAutomatedFromSMS = true
        )

        financialDao.addTransactionAndUpdateBalance(transaction)
        Log.d("AlkansyaParser", "Successfully registered SMS transaction of $parsedAmount for account ${targetAccount.name}")
        return true
    }

    // Savings Vault CRUD Methods
    suspend fun insertVault(vault: SavingsVault): Long {
        return financialDao.insertVault(vault)
    }

    suspend fun updateVault(vault: SavingsVault) {
        financialDao.updateVault(vault)
    }

    suspend fun deleteVault(vault: SavingsVault) {
        val account = financialDao.getAccountById(vault.fundingAccountId)
        if (account != null && vault.currentSaved > 0) {
            val updated = account.copy(currentBalance = account.currentBalance + vault.currentSaved)
            financialDao.updateAccount(updated)
            financialDao.insertTransaction(
                Transaction(
                    accountId = account.id,
                    amount = vault.currentSaved,
                    transactionType = "INCOME",
                    merchantName = "Refund: ${vault.name}",
                    timestamp = System.currentTimeMillis(),
                    isAutomatedFromSMS = false
                )
            )
        }
        financialDao.deleteVault(vault)
    }

    suspend fun depositToVault(vault: SavingsVault, amount: Double): Boolean {
        val account = financialDao.getAccountById(vault.fundingAccountId) ?: return false
        if (account.currentBalance < amount) return false
        
        val updatedAccount = account.copy(currentBalance = account.currentBalance - amount)
        val updatedVault = vault.copy(currentSaved = vault.currentSaved + amount)
        
        financialDao.updateAccount(updatedAccount)
        financialDao.updateVault(updatedVault)
        
        financialDao.insertTransaction(
            Transaction(
                accountId = account.id,
                amount = amount,
                transactionType = "EXPENSE",
                merchantName = "Saved in ${vault.name}",
                timestamp = System.currentTimeMillis(),
                isAutomatedFromSMS = false
            )
        )
        return true
    }

    suspend fun withdrawFromVault(vault: SavingsVault, amount: Double): Boolean {
        if (vault.currentSaved < amount) return false
        val account = financialDao.getAccountById(vault.fundingAccountId) ?: return false
        
        val updatedAccount = account.copy(currentBalance = account.currentBalance + amount)
        val updatedVault = vault.copy(currentSaved = vault.currentSaved - amount)
        
        financialDao.updateAccount(updatedAccount)
        financialDao.updateVault(updatedVault)
        
        financialDao.insertTransaction(
            Transaction(
                accountId = account.id,
                amount = amount,
                transactionType = "INCOME",
                merchantName = "Withdrew from ${vault.name}",
                timestamp = System.currentTimeMillis(),
                isAutomatedFromSMS = false
            )
        )
        return true
    }
}
