package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.FinancialAccount
import com.example.data.local.FinancialDatabase
import com.example.data.local.Transaction
import com.example.data.local.SavingsVault
import com.example.data.repository.FinancialRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FinancialViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FinancialRepository
    private val prefs = application.getSharedPreferences("alkansya_prefs", android.content.Context.MODE_PRIVATE)

    // Reactive states for Language & First Run
    private val _selectedLanguage = kotlinx.coroutines.flow.MutableStateFlow(prefs.getString("selected_lang", "EN") ?: "EN")
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    private val _showLanguagePrompt = kotlinx.coroutines.flow.MutableStateFlow(prefs.getBoolean("is_first_run", true))
    val showLanguagePrompt: StateFlow<Boolean> = _showLanguagePrompt

    fun setLanguage(lang: String) {
        prefs.edit().putString("selected_lang", lang).apply()
        _selectedLanguage.value = lang
    }

    fun completeFirstRun() {
        prefs.edit().putBoolean("is_first_run", false).apply()
        _showLanguagePrompt.value = false
    }

    // Preferred base currency: by default "PHP", customizable to "USD", "EUR", etc.
    private val _preferredBaseCurrency = kotlinx.coroutines.flow.MutableStateFlow(prefs.getString("base_currency", "PHP") ?: "PHP")
    val preferredBaseCurrency: StateFlow<String> = _preferredBaseCurrency

    fun setPreferredBaseCurrency(currency: String) {
        prefs.edit().putString("base_currency", currency).apply()
        _preferredBaseCurrency.value = currency
    }

    // Theme Mode: "DARK", "LIGHT"
    private val _themeMode = kotlinx.coroutines.flow.MutableStateFlow(prefs.getString("theme_mode", "DARK") ?: "DARK")
    val themeMode: StateFlow<String> = _themeMode

    fun setThemeMode(mode: String) {
        prefs.edit().putString("theme_mode", mode).apply()
        _themeMode.value = mode
    }

    // User Nickname for personalized greetings
    private val _userNickname = kotlinx.coroutines.flow.MutableStateFlow(prefs.getString("user_nickname", "") ?: "")
    val userNickname: StateFlow<String> = _userNickname

    fun setUserNickname(nickname: String) {
        prefs.edit().putString("user_nickname", nickname).apply()
        _userNickname.value = nickname
    }

    val exchangeRates = mapOf(
        "PHP" to 1.0,
        "USD" to 58.5,
        "EUR" to 63.5,
        "GBP" to 74.5,
        "SGD" to 43.5
    )

    init {
        val database = FinancialDatabase.getDatabase(application)
        repository = FinancialRepository(database.financialDao())
    }

    // Reactively observe all accounts
    val accounts: StateFlow<List<FinancialAccount>> = repository.allAccounts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val vaults: StateFlow<List<SavingsVault>> = repository.allVaults
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Reactively observe all transactions
    val transactions: StateFlow<List<Transaction>> = repository.allTransactions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Reactively derive total net worth converting child accounts to target base currency
    val totalNetWorth: StateFlow<Double> = kotlinx.coroutines.flow.combine(accounts, preferredBaseCurrency) { list, baseCur ->
        val totalInPhp = list.sumOf { acc ->
            val rateToPhp = exchangeRates[acc.currency] ?: 1.0
            acc.currentBalance * rateToPhp
        }
        val rateFromPhpToBase = exchangeRates[baseCur] ?: 1.0
        if (rateFromPhpToBase != 0.0) {
            totalInPhp / rateFromPhpToBase
        } else {
            totalInPhp
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    // Notification channel for transaction simulations
    private val _simulationEvent = MutableSharedFlow<String>()
    val simulationEvent: SharedFlow<String> = _simulationEvent

    fun addAccount(name: String, providerId: String, typeString: String, startingBalance: Double, currency: String = "PHP") {
        viewModelScope.launch {
            val account = FinancialAccount(
                name = name,
                type = typeString,
                providerName = providerId,
                startingBalance = startingBalance,
                currentBalance = startingBalance,
                currency = currency
            )
            repository.insertAccount(account)
        }
    }

    fun addTransactionManually(accountId: Long, amount: Double, typeString: String, merchant: String, category: String = "Others") {
        viewModelScope.launch {
            val transaction = Transaction(
                accountId = accountId,
                amount = amount,
                transactionType = typeString,
                merchantName = merchant,
                timestamp = System.currentTimeMillis(),
                isAutomatedFromSMS = false,
                category = category
            )
            repository.addTransactionAndUpdateBalance(transaction)
        }
    }

    fun deleteAccount(account: FinancialAccount) {
        viewModelScope.launch {
            repository.deleteAccount(account)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    // Savings Vault Methods
    fun addSavingsVault(name: String, targetAmount: Double, fundingAccountId: Long) {
        viewModelScope.launch {
            val vault = SavingsVault(
                name = name,
                targetAmount = targetAmount,
                currentSaved = 0.0,
                fundingAccountId = fundingAccountId
            )
            repository.insertVault(vault)
        }
    }

    fun deleteSavingsVault(vault: SavingsVault) {
        viewModelScope.launch {
            repository.deleteVault(vault)
        }
    }

    fun depositToSavingsVault(vault: SavingsVault, amount: Double, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.depositToVault(vault, amount)
            onResult(success)
        }
    }

    fun withdrawFromSavingsVault(vault: SavingsVault, amount: Double, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.withdrawFromVault(vault, amount)
            onResult(success)
        }
    }

    fun updateAccountCardSettings(account: FinancialAccount, limit: Double, isFrozen: Boolean, colorIndex: Int) {
        viewModelScope.launch {
            val updated = account.copy(
                cardLimit = limit,
                isFrozen = isFrozen,
                cardColorIndex = colorIndex
            )
            repository.updateAccount(updated)
        }
    }

    /**
     * For developer demonstration and testing:
     * Simulates the receipt of an incoming SMS transaction alert
     * without requiring an actual cellular signal.
     */
    fun simulateSmsReceived(sender: String, messageText: String) {
        viewModelScope.launch {
            val parsedResult = repository.processIncomingSMS(sender, messageText)
            if (parsedResult) {
                _simulationEvent.emit("Successfully parsed SMSalert! Added transaction for $sender.")
            } else {
                _simulationEvent.emit("SMS simulation failed: did not match any regex group pattern.")
            }
        }
    }
}
