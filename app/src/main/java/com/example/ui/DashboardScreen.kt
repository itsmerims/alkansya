package com.example.ui

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.focusable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.local.FinancialAccount
import com.example.data.local.Transaction
import com.example.data.model.FinancialCatalog
import com.example.data.model.Institution
import com.example.data.model.InstitutionGroup
import com.example.data.model.CardTheme
import com.example.data.model.getCardTheme
import com.example.ui.viewmodel.FinancialViewModel
import com.example.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

// Global Translation Helper
fun getTranslate(key: String, lang: String): String {
    return when (lang) {
        "TL" -> when (key) {
            "NET_WORTH" -> "Kabuuang Pondo"
            "DEBIT" -> "Debit (Bawas)"
            "CREDIT" -> "Credit (Dagdag)"
            "RECENT_TX" -> "Recent Transactions"
            "VIEW_HISTORY" -> "Kasaysayan"
            "SEARCH_PLACEHOLDER" -> "Maghanap ng merchant o account..."
            "NO_TX" -> "Walang transaksyon pa."
            "ADD_ACCOUNT" -> "I-konek ang Account"
            "BALANCE" -> "Kasalukuyang Balanse"
            "STARTING_BALANCE" -> "Paunang Balanse"
            "CONNECTED_ACCOUNTS" -> "Mga Konektadong Account"
            "STABILITY_SMS" -> "Awtomatikong pag-sync gamit ang SMS ay aktibo."
            "SMS_PERMISSION_PROMPT" -> "Paganahin ang SMS permission sa Settings para sa awtomatikong tracking."
            "REARRANGE_MODE" -> "Ayusin ang Pagkakasunod-sunod"
            "DONE" -> "Tapos na"
            "SAVE" -> "I-save"
            "CANCEL" -> "Kanselahin"
            "SETTINGS_HEADER" -> "Mga Setting ng System"
            "PLANNING" -> "Pagpaplano ng Badyet"
            "TOTAL" -> "Kabuuan"
            "STATS" -> "Estadistika ng Paggastos"
            "WALLET_TITLE" -> "Aking Kalupi"
            "HISTORY_TITLE" -> "Log ng Kasaysayan"
            "SETTINGS_TITLE" -> "Mga Setting"
            "BUDGET_PLAN_TITLE" -> "Aking Plano"
            "SELECT_LANG" -> "Pumili ng Wika"
            "MOCK_ALERT" -> "Subukan ang SMS Simulation"
            "INCOME_VS_EXPENSES" -> "Kita laban sa Gastos"
            "TOP_MERCHANTS" -> "Pinakamataas na Pinagkagastusan"
            "CHOOSE_INST_CATEGORY" -> "Pumili ng kategorya upang magsimula"
            "ENTER_ACCOUNT_DETAILS" -> "Ipasok ang mga detalye ng account"
            "E_WALLETS" -> "E-Wallets"
            "DIGITAL_BANKS" -> "Digital Banks"
            "TRADITIONAL_BANKS" -> "Local Banks"
            "FOREIGN_BANKS" -> "Foreign Banks"
            "WALANG_KONEKTADONG_BANKS" -> "Walang nakakonektang account sa kalupi. Magdagdag sa pamamagitan ng pagpindot sa button sa ibaba."
            "STABILITY_BUDGET" -> "Subaybayan ang iyong mga limitasyon sa badyet."
            "NEXT" -> "Susunod"
            "SELECT_THEME" -> "Piliin ang Uri ng Tema"
            "DARK_MODE" -> "Madilim (Gabi)"
            "LIGHT_MODE" -> "Maliwanag (Araw)"
            else -> key
        }
        else -> when (key) { // "EN"
            "NET_WORTH" -> "Total Net Worth"
            "DEBIT" -> "Debit"
            "CREDIT" -> "Credit"
            "RECENT_TX" -> "Recent Transactions"
            "VIEW_HISTORY" -> "View History"
            "SEARCH_PLACEHOLDER" -> "Search merchant or account..."
            "NO_TX" -> "No transactions yet."
            "ADD_ACCOUNT" -> "Connect Account"
            "BALANCE" -> "Current Balance"
            "STARTING_BALANCE" -> "Starting Balance"
            "CONNECTED_ACCOUNTS" -> "Connected Accounts"
            "STABILITY_SMS" -> "Automatic SMS syncing integration is active."
            "SMS_PERMISSION_PROMPT" -> "Enable SMS permissions in Settings for automated tracking."
            "REARRANGE_MODE" -> "Rearrange Order"
            "DONE" -> "Done"
            "SAVE" -> "Save"
            "CANCEL" -> "Cancel"
            "SETTINGS_HEADER" -> "System Settings"
            "PLANNING" -> "Budget Planner"
            "TOTAL" -> "Total"
            "STATS" -> "Expense Dashboard"
            "WALLET_TITLE" -> "Wallet"
            "HISTORY_TITLE" -> "History"
            "SETTINGS_TITLE" -> "Settings"
            "BUDGET_PLAN_TITLE" -> "Budget Plan"
            "SELECT_LANG" -> "Select Language"
            "MOCK_ALERT" -> "Simulate SMS Alerts"
            "INCOME_VS_EXPENSES" -> "Income vs Expenses"
            "TOP_MERCHANTS" -> "Top Expenses by Merchant"
            "CHOOSE_INST_CATEGORY" -> "Choose an institution group to connect"
            "ENTER_ACCOUNT_DETAILS" -> "Enter Account Details"
            "E_WALLETS" -> "E-Wallets"
            "DIGITAL_BANKS" -> "Digital Banks"
            "TRADITIONAL_BANKS" -> "Local Banks"
            "FOREIGN_BANKS" -> "Foreign Banks"
            "WALANG_KONEKTADONG_BANKS" -> "No connected accounts in your Wallet yet. Press the button below to add one."
            "STABILITY_BUDGET" -> "Keep track of your limits and budgeting rules."
            "NEXT" -> "Next"
            "SELECT_THEME" -> "Select Theme Mode"
            "DARK_MODE" -> "Dark Mode"
            "LIGHT_MODE" -> "Light Mode"
            else -> key
        }
    }
}

// Time-based greeting helper functions
fun getGreeting(lang: String, name: String): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val timeGreeting = when (hour) {
        in 0..11 -> if (lang == "TL") "Magandang Umaga" else "Good Morning"
        in 12..17 -> if (lang == "TL") "Magandang Hapon" else "Good Afternoon"
        else -> if (lang == "TL") "Magandang Gabi" else "Good Evening"
    }
    return if (name.isNotBlank()) "$timeGreeting, $name!" else timeGreeting
}

fun getGreetingSubtitle(lang: String, isHome: Boolean): String {
    return if (isHome) {
        if (lang == "TL") "Ito ang iyong pinansyal na buhay ngayon." else "Here's your financial life today."
    } else {
        if (lang == "TL") "Maligayang pagdating sa Alkansya!" else "Welcome to Alkansya!"
    }
}

// Helpers for consistent Material 3 aesthetics in Compose
val MaterialTheme.styleOfMainSectionHeader
    @Composable
    get() = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.sp,
        color = MaterialTheme.colorScheme.onBackground
    )

private typealias TextStyle = androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AlkansyaApp(viewModel: FinancialViewModel) {
    val context = LocalContext.current
    
    // UI State Observability
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val totalNetWorth by viewModel.totalNetWorth.collectAsStateWithLifecycle()
    val simulationEvent by viewModel.simulationEvent.collectAsState(initial = null)
    
    // Reactor States
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val showLanguagePrompt by viewModel.showLanguagePrompt.collectAsStateWithLifecycle()
    val preferredBaseCurrency by viewModel.preferredBaseCurrency.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val userNickname by viewModel.userNickname.collectAsStateWithLifecycle()

    // Form/Interactive navigation state
    var showAddAccountPage by remember { mutableStateOf(false) }
    var showAddTransactionDialog by remember { mutableStateOf<String?>(null) } // "DEBIT" or "CREDIT"
    var showSimulationPanel by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf("HOME") }

    // Account ordering preference
    var accountOrderList by remember { mutableStateOf<List<Long>>(emptyList()) }
    LaunchedEffect(accounts) {
        if (accounts.isNotEmpty()) {
            val currentIds = accounts.map { it.id }
            val existingInOrder = accountOrderList.filter { it in currentIds }
            val newIds = currentIds.filter { it !in accountOrderList }
            accountOrderList = existingInOrder + newIds
        }
    }

    // Dynamic Permission Checker State
    var hasSMSPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val receiveGranted = permissions[Manifest.permission.RECEIVE_SMS] ?: false
        val readGranted = permissions[Manifest.permission.READ_SMS] ?: false
        hasSMSPermission = receiveGranted && readGranted
        if (hasSMSPermission) {
            Toast.makeText(context, "SMS integration is active.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Automated SMS alerts disabled. Grant in Settings.", Toast.LENGTH_LONG).show()
        }
    }

    // Monitor simulations toast
    LaunchedEffect(simulationEvent) {
        simulationEvent?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    // Show Login/Registration screen if no nickname is set
    if (userNickname.isEmpty()) {
        LoginRegistrationScreen(
            lang = selectedLanguage,
            onNicknameSaved = { nickname ->
                viewModel.setUserNickname(nickname)
            }
        )
    } else if (showAddAccountPage) {
        AddAccountPage(
            lang = selectedLanguage,
            onDismiss = { showAddAccountPage = false },
            onSave = { name, providerId, typeString, balance, currency ->
                viewModel.addAccount(name, providerId, typeString, balance, currency)
                showAddAccountPage = false
            }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "PRIVACY SECURED",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Alkansya",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = ".",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF10B981) // Emerald brand dot
                                )
                            }
                        }
                    },
                    actions = {
                        if (currentTab == "SETTINGS") {
                            IconButton(
                                onClick = { showSimulationPanel = !showSimulationPanel },
                                modifier = Modifier.testTag("simulation_toggle")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Build,
                                    contentDescription = "Simulate SMS Alerts",
                                    tint = if (showSimulationPanel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddAccountPage = true },
                    containerColor = Color(0xFF10B981), // Emerald Accent Color
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .testTag("add_account_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Connect Account",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            bottomBar = {
                AlkansyaBottomNavigation(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it },
                    lang = selectedLanguage
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Route between the custom redesign views
                when (currentTab) {
                    "HOME" -> HomeScreenContent(
                        lang = selectedLanguage,
                        accounts = accounts,
                        transactions = transactions,
                        totalNetWorth = totalNetWorth,
                        hasSMSPermission = hasSMSPermission,
                        onGrantPermission = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.RECEIVE_SMS,
                                    Manifest.permission.READ_SMS
                                )
                            )
                        },
                        onViewHistoryClick = { currentTab = "HISTORY" },
                        onDeleteTransaction = { viewModel.deleteTransaction(it) },
                        preferredBaseCurrency = preferredBaseCurrency,
                        userNickname = userNickname
                    )
                    "WALLET" -> WalletScreen(
                        lang = selectedLanguage,
                        accounts = accounts,
                        transactions = transactions,
                        totalNetWorth = totalNetWorth,
                        accountOrderList = accountOrderList,
                        onRearrangeOrderChange = { accountOrderList = it },
                        onAddManualDebit = { showAddTransactionDialog = "DEBIT" },
                        onAddManualCredit = { showAddTransactionDialog = "CREDIT" },
                        onDeleteAccount = { viewModel.deleteAccount(it) },
                        preferredBaseCurrency = preferredBaseCurrency
                    )
                    "HISTORY" -> HistoryScreen(
                        lang = selectedLanguage,
                        accounts = accounts,
                        transactions = transactions,
                        onDeleteTransaction = { viewModel.deleteTransaction(it) }
                    )
                    "PLAN" -> PlanScreen(
                        lang = selectedLanguage,
                        transactions = transactions,
                        accounts = accounts,
                        preferredBaseCurrency = preferredBaseCurrency
                    )
                    "SETTINGS" -> SettingsScreen(
                        lang = selectedLanguage,
                        hasSMSPermission = hasSMSPermission,
                        showSimulationPanel = showSimulationPanel,
                        onSimulateSMS = { sender, text -> viewModel.simulateSmsReceived(sender, text) },
                        onLanguageChange = { viewModel.setLanguage(it) },
                        onToggleSMSPermission = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.RECEIVE_SMS,
                                    Manifest.permission.READ_SMS
                                )
                            )
                        },
                        preferredBaseCurrency = preferredBaseCurrency,
                        onBaseCurrencyChange = { viewModel.setPreferredBaseCurrency(it) },
                        themeMode = themeMode,
                        onThemeModeChange = { viewModel.setThemeMode(it) },
                        userNickname = userNickname,
                        onNicknameChange = { viewModel.setUserNickname(it) }
                    )
                }
            }
        }
    }

    // Add Preset Drag Manual Transaction Dialogue
    if (showAddTransactionDialog != null && accounts.isNotEmpty()) {
        AddManualTransactionDialog(
            lang = selectedLanguage,
            accounts = accounts,
            typePreset = showAddTransactionDialog!!,
            onDismiss = { showAddTransactionDialog = null },
            onSave = { accountId, amount, type, merchant, category ->
                viewModel.addTransactionManually(accountId, amount, type, merchant, category)
                showAddTransactionDialog = null
            }
        )
    }

    // Interactive First Run Welcoming Modal Prompt
    if (showLanguagePrompt) {
        FirstRunLanguageModal(
            onLanguageSelected = { lang ->
                viewModel.setLanguage(lang)
                viewModel.completeFirstRun()
                Toast.makeText(context, if (lang == "TL") "Mabuhay! Mag-enjoy sa Alkansya." else "Welcome to Alkansya! Enjoy managing your finances.", Toast.LENGTH_LONG).show()
            }
        )
    }
}

// Custom First Run Dialogue Welcome
@Composable
fun FirstRunLanguageModal(
    onLanguageSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = {}, // Force selection!
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Language Loader",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(54.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Welcome to Alkansya!",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select your language Preference.\nPiliin ang iyong gustong wika:",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                // English Select Item
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageSelected("EN") }
                        .border(1.dp, Color(0xFF10B981), RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🇺🇸", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "English (US)", 
                                fontWeight = FontWeight.Bold, 
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                            Text(
                                "Default language settings", 
                                fontSize = 11.sp, 
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Tagalog Select Item
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageSelected("TL") }
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🇵🇭", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "Tagalog", 
                                fontWeight = FontWeight.Bold, 
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                            Text(
                                "Wikang Tagalog ng Pilipinas", 
                                fontSize = 11.sp, 
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

// Login/Registration Screen for user nickname entry
@Composable
fun LoginRegistrationScreen(
    lang: String,
    onNicknameSaved: (String) -> Unit
) {
    var nickname by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Premium emerald gradient logo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF10B981),
                                Color(0xFF059669),
                                Color(0xFF047857)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(R.drawable.logo),
                    contentDescription = "Alkansya Logo",
                    modifier = Modifier.size(80.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Dynamic time-based greeting
            Text(
                text = getGreeting(lang, ""),
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = getGreetingSubtitle(lang, false),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Stylized input field
            OutlinedTextField(
                value = nickname,
                onValueChange = { 
                    nickname = it
                    showError = false
                },
                label = { 
                    Text(if (lang == "TL") "Pangalan o Palayaw" else "Name or Nickname")
                },
                placeholder = { 
                    Text(if (lang == "TL") "Ilagay ang iyong pangalan..." else "Enter your name...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Icon",
                        tint = Color(0xFF10B981)
                    )
                },
                trailingIcon = {
                    if (nickname.isNotEmpty()) {
                        IconButton(onClick = { nickname = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                singleLine = true,
                isError = showError,
                supportingText = {
                    if (showError) {
                        Text(
                            if (lang == "TL") "Pakilagay ng kahit isang karakter" else "Please enter at least one character",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("nickname_input"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF10B981),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Modern primary CTA button
            Button(
                onClick = {
                    val trimmed = nickname.trim()
                    if (trimmed.isNotEmpty()) {
                        onNicknameSaved(trimmed)
                    } else {
                        showError = true
                    }
                },
                enabled = nickname.trim().isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("save_nickname_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (lang == "TL") "Simulan" else "Get Started",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Fullscreen Page Replacement for adding manual bank account
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountPage(
    lang: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Double, String) -> Unit
) {
    var accountName by remember { mutableStateOf("") }
    var startingBalanceString by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("PHP") }
    var selectedInstitution by remember { mutableStateOf<Institution?>(null) }
    
    val focusRequester = remember { FocusRequester() }
    val startingBalanceFocusRequester = remember { FocusRequester() }
    val currencyFocusRequester = remember { FocusRequester() }
    var isCurrencyFocused by remember { mutableStateOf(false) }
    var activeFocusedField by remember { mutableStateOf("NICKNAME") }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(selectedInstitution) {
        if (selectedInstitution != null) {
            delay(100)
            try {
                focusRequester.requestFocus()
            } catch (e: Exception) {
                delay(200)
                try {
                    focusRequester.requestFocus()
                } catch (ex: Exception) {
                    // Suppress focus errors gracefully to prevent any crash
                }
            }
        }
    }

    // Intercept hardware Android back click
    BackHandler {
        onDismiss()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(getTranslate("ADD_ACCOUNT", lang), fontWeight = FontWeight.Black, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = getTranslate("CHOOSE_INST_CATEGORY", lang).uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color(0xFF10B981)
                    )
                }

                // 4 Requested Categories Segment lists
                val categories = listOf(
                    Pair(InstitutionGroup.E_WALLET, getTranslate("E_WALLETS", lang)),
                    Pair(InstitutionGroup.DIGITAL_BANK, getTranslate("DIGITAL_BANKS", lang)),
                    Pair(InstitutionGroup.TRADITIONAL_BANK, getTranslate("TRADITIONAL_BANKS", lang)),
                    Pair(InstitutionGroup.FOREIGN_BANK, getTranslate("FOREIGN_BANKS", lang))
                )

                categories.forEach { (catGroup, catLabel) ->
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = catLabel,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                
                                val itemsInGroup = remember { FinancialCatalog.items.filter { it.group == catGroup } }
                                // Render row items of institutions
                                FlowRowLayout(
                                    spacing = 8.dp
                                ) {
                                    itemsInGroup.forEach { inst ->
                                        val isSelected = selectedInstitution == inst
                                        val borderStrokeColor = if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant
                                        val bgGrad = if (isSelected) Color(0x2210B981) else Color.Transparent

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(bgGrad)
                                                .border(
                                                    if (isSelected) 2.dp else 1.dp, 
                                                    borderStrokeColor, 
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .clickable {
                                                    selectedInstitution = inst
                                                    scope.launch {
                                                        listState.animateScrollToItem(5)
                                                        delay(100)
                                                        try {
                                                            focusRequester.requestFocus()
                                                        } catch (e: Exception) {}
                                                    }
                                                }
                                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                InstitutionLogoIcon(
                                                    logoUrl = inst.logoUrl,
                                                    displayName = inst.displayName,
                                                    primaryColorHex = inst.primaryColorHex,
                                                    sizeDim = 20.dp
                                                )
                                                Text(
                                                    text = inst.displayName,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Details Input form Section (Always Visible)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, if (selectedInstitution != null) Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = getTranslate("ENTER_ACCOUNT_DETAILS", lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            OutlinedTextField(
                                value = accountName,
                                onValueChange = { accountName = it },
                                label = { Text("Account Custom Nickname") },
                                placeholder = { Text("e.g. My Savings " + (selectedInstitution?.displayName ?: "")) },
                                singleLine = true,
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        startingBalanceFocusRequester.requestFocus()
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)
                                    .onFocusChanged { if (it.isFocused) activeFocusedField = "NICKNAME" }
                                    .testTag("account_name_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            OutlinedTextField(
                                value = startingBalanceString,
                                onValueChange = { startingBalanceString = it },
                                label = { Text(getTranslate("STARTING_BALANCE", lang)) },
                                placeholder = { Text("0.00") },
                                singleLine = true,
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        currencyFocusRequester.requestFocus()
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(startingBalanceFocusRequester)
                                    .onFocusChanged { if (it.isFocused) activeFocusedField = "BALANCE" }
                                    .testTag("starting_balance_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (lang == "TL") "Piliin ang Currency" else "Select Account Currency",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(currencyFocusRequester)
                                    .onFocusChanged { 
                                        isCurrencyFocused = it.isFocused 
                                        if (it.isFocused) activeFocusedField = "CURRENCY"
                                    }
                                    .focusable()
                                    .border(
                                        width = if (isCurrencyFocused) 2.dp else 0.dp,
                                        color = if (isCurrencyFocused) Color(0xFF10B981) else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(if (isCurrencyFocused) 4.dp else 0.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                             ) {
                                 val currencies = listOf(
                                     "PHP" to "🇵🇭 PHP",
                                     "USD" to "🇺🇸 USD",
                                     "EUR" to "🇪🇺 EUR",
                                     "GBP" to "🇬🇧 GBP",
                                     "SGD" to "🇸🇬 SGD"
                                 )
                                 currencies.forEach { (code, display) ->
                                     val isSel = selectedCurrency == code
                                     Box(
                                         modifier = Modifier
                                             .weight(1f)
                                             .clip(RoundedCornerShape(10.dp))
                                             .background(if (isSel) Color(0xFF10B981) else MaterialTheme.colorScheme.surfaceVariant)
                                             .clickable { selectedCurrency = code }
                                             .padding(vertical = 10.dp),
                                         contentAlignment = Alignment.Center
                                     ) {
                                         Text(
                                             text = display,
                                             fontSize = 11.sp,
                                             fontWeight = FontWeight.Bold,
                                             color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                         )
                                     }
                                 }
                             }
                         }
                     }
                 }

                 item { Spacer(modifier = Modifier.height(16.dp)) }
             }

             // Save/Cancel action buttons outside of LazyColumn to always be visible even above keyboard
             Row(
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(horizontal = 20.dp, vertical = 12.dp)
                     .navigationBarsPadding(),
                 horizontalArrangement = Arrangement.spacedBy(12.dp)
             ) {
                 OutlinedButton(
                     onClick = onDismiss,
                     modifier = Modifier.weight(1f),
                     shape = RoundedCornerShape(50)
                 ) {
                     Text(getTranslate("CANCEL", lang))
                 }

                 Button(
                     onClick = {
                         when (activeFocusedField) {
                             "NICKNAME" -> {
                                 try {
                                     startingBalanceFocusRequester.requestFocus()
                                 } catch (e: Exception) {}
                             }
                             "BALANCE" -> {
                                 try {
                                     currencyFocusRequester.requestFocus()
                                 } catch (e: Exception) {}
                             }
                             else -> {
                                 val prov = selectedInstitution?.id ?: "CUSTOM"
                                 val typ = selectedInstitution?.type?.name ?: "WALLET"
                                 val amt = startingBalanceString.toDoubleOrNull() ?: 0.0
                                 val nm = accountName.ifBlank { selectedInstitution?.displayName ?: "Personal Wallet" }
                                 val cur = selectedCurrency
                                 onSave(nm, prov, typ, amt, cur)
                             }
                         }
                     },
                     enabled = selectedInstitution != null,
                     colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                     modifier = Modifier
                         .weight(1f)
                         .testTag("save_account_button"),
                     shape = RoundedCornerShape(50)
                 ) {
                     val btnText = when (activeFocusedField) {
                         "NICKNAME", "BALANCE" -> getTranslate("NEXT", lang)
                         else -> getTranslate("SAVE", lang)
                     }
                     Text(btnText, fontWeight = FontWeight.Bold)
                 }
             }
         }
     }
 }

// Clean Flow wrapping FlowRow alternative to avoid multiplatform layout dependencies
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowLayout(
    spacing: androidx.compose.ui.unit.Dp = 8.dp,
    content: @Composable () -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
        modifier = Modifier.fillMaxWidth(),
        content = { content() }
    )
}

// Custom Coil Async Image Renderer with fallback initials container
@Composable
fun InstitutionLogoIcon(
    logoUrl: String,
    displayName: String,
    primaryColorHex: String,
    sizeDim: androidx.compose.ui.unit.Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val brandColor = remember(primaryColorHex) {
        try {
            Color(android.graphics.Color.parseColor(primaryColorHex))
        } catch (e: Exception) {
            Color(0xFF10B981)
        }
    }

    Box(
        modifier = modifier
            .size(sizeDim)
            .clip(CircleShape)
            .background(brandColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        // Initials printed behind/under the image
        val initials = displayName.filter { it.isLetter() }.take(2).uppercase()
        Text(
            text = initials,
            fontSize = if (sizeDim < 24.dp) 8.sp else 10.sp,
            fontWeight = FontWeight.Black,
            color = brandColor,
            textAlign = TextAlign.Center
        )

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(logoUrl)
                .crossfade(true)
                .build(),
            contentDescription = displayName,
            modifier = Modifier
                .size(sizeDim)
                .clip(CircleShape),
            error = null,
            fallback = null
        )
    }
}

// HOME TAB CONTENT PAGE: Merging Networth, Stats Summaries, and Top 3 list with Goto-History link
@Composable
fun HomeScreenContent(
    lang: String,
    accounts: List<FinancialAccount>,
    transactions: List<Transaction>,
    totalNetWorth: Double,
    hasSMSPermission: Boolean,
    onGrantPermission: () -> Unit,
    onViewHistoryClick: () -> Unit,
    onDeleteTransaction: (Transaction) -> Unit,
    preferredBaseCurrency: String,
    userNickname: String
) {
    val exchangeRates = remember {
        mapOf(
            "PHP" to 1.0,
            "USD" to 58.5,
            "EUR" to 63.5,
            "GBP" to 74.5,
            "SGD" to 43.5
        )
    }

    val recentTop3 = remember(transactions) {
        transactions.take(3)
    }

    val totalExpenseConverted = remember(transactions, accounts, preferredBaseCurrency) {
        val totalInPhp = transactions.filter { it.transactionType == "EXPENSE" }.sumOf { trans ->
            val account = accounts.find { it.id == trans.accountId }
            val currency = account?.currency ?: "PHP"
            val rateToPhp = exchangeRates[currency] ?: 1.0
            trans.amount * rateToPhp
        }
        val rateFromPhpToBase = exchangeRates[preferredBaseCurrency] ?: 1.0
        if (rateFromPhpToBase != 0.0) totalInPhp / rateFromPhpToBase else totalInPhp
    }

    val totalIncomeConverted = remember(transactions, accounts, preferredBaseCurrency) {
        val totalInPhp = transactions.filter { it.transactionType == "INCOME" }.sumOf { trans ->
            val account = accounts.find { it.id == trans.accountId }
            val currency = account?.currency ?: "PHP"
            val rateToPhp = exchangeRates[currency] ?: 1.0
            trans.amount * rateToPhp
        }
        val rateFromPhpToBase = exchangeRates[preferredBaseCurrency] ?: 1.0
        if (rateFromPhpToBase != 0.0) totalInPhp / rateFromPhpToBase else totalInPhp
    }

    val merchantSumsConverted = remember(transactions, accounts, preferredBaseCurrency) {
        val groupedInPhp = transactions.filter { it.transactionType == "EXPENSE" }
            .groupBy { it.merchantName }
            .mapValues { entry ->
                entry.value.sumOf { trans ->
                    val account = accounts.find { it.id == trans.accountId }
                    val currency = account?.currency ?: "PHP"
                    val rateToPhp = exchangeRates[currency] ?: 1.0
                    trans.amount * rateToPhp
                }
            }
        val rateFromPhpToBase = exchangeRates[preferredBaseCurrency] ?: 1.0
        groupedInPhp.mapValues { if (rateFromPhpToBase != 0.0) it.value / rateFromPhpToBase else it.value }
            .toList()
            .sortedByDescending { it.second }
            .take(3)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Dynamic greeting card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = getGreeting(lang, userNickname),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getGreetingSubtitle(lang, true),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // SMS permissions warning banner
        if (!hasSMSPermission) {
            item {
                SMSPermissionBanner(onGrantPermission, lang)
            }
        }

        // Total Net Wealth Display Overview
        item {
            val isDarkTheme = MaterialTheme.colorScheme.background == Color(0xFF0F172A)
            val netWorthCardBg = if (isDarkTheme) Color(0xFF0F172A) else MaterialTheme.colorScheme.primaryContainer
            val netWorthLabelColor = if (isDarkTheme) Color(0xFF34D399) else MaterialTheme.colorScheme.primary
            val netWorthValueColor = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
            val smsSyncBg = if (isDarkTheme) Color(0x1F34D399) else Color(0x1A10B981)
            val smsSyncTextColor = if (isDarkTheme) Color(0xFF34D399) else MaterialTheme.colorScheme.primary

            val cardBorder = if (isDarkTheme) {
                BorderStroke(1.dp, Color(0xFF1E293B))
            } else {
                BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = netWorthCardBg),
                shape = RoundedCornerShape(24.dp),
                border = cardBorder
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = getTranslate("NET_WORTH", lang).uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = netWorthLabelColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatCurrency(totalNetWorth, preferredBaseCurrency),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = netWorthValueColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = smsSyncBg,
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF10B981))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "SMS Auto-Sync Active",
                                    fontSize = 11.sp,
                                    color = smsSyncTextColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Redesigned Merged Stats Dashboard card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = getTranslate("STATS", lang).uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color(0xFF10B981)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    // Net Flow Breakdown Visual Progress Bars
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Inflow (Income)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = formatCurrency(totalIncomeConverted, preferredBaseCurrency), fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF10B981))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Outflow (Expenses)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = formatCurrency(totalExpenseConverted, preferredBaseCurrency), fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFFF43F5E))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Simple graphical scale indicator line
                    val progressRatio = remember(totalIncomeConverted, totalExpenseConverted) {
                        val sum = totalIncomeConverted + totalExpenseConverted
                        if (sum == 0.0) 0.5f else (totalIncomeConverted / sum).toFloat()
                    }
                    LinearProgressIndicator(
                        progress = progressRatio,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF10B981),
                        trackColor = Color(0xFFF43F5E)
                    )

                    // Top merchant summaries
                    if (merchantSumsConverted.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = getTranslate("TOP_MERCHANTS", lang),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        merchantSumsConverted.forEach { (merchant, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(merchant, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(formatCurrency(value, preferredBaseCurrency), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }
        }

        // Recent transactions List Header and jump button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getTranslate("RECENT_TX", lang),
                    style = MaterialTheme.styleOfMainSectionHeader
                )
                
                TextButton(
                    onClick = onViewHistoryClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF10B981))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(getTranslate("VIEW_HISTORY", lang), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "", modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        if (recentTop3.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "",
                            modifier = Modifier.size(36.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = getTranslate("NO_TX", lang),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(
                items = recentTop3,
                key = { it.id }
            ) { tx ->
                val matchingAccount = accounts.find { it.id == tx.accountId }
                    TransactionItemRow(
                        transaction = tx,
                        accountName = matchingAccount?.name ?: "Unknown Provider",
                        accountColorHex = matchingAccount?.let {
                            FinancialCatalog.findById(it.providerName)?.primaryColorHex
                        } ?: "#757575",
                        onDelete = { onDeleteTransaction(tx) },
                        currency = matchingAccount?.currency ?: "PHP"
                    )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// WALLET SCREEN (Replaces cards screen completely: rearrangable order items, debit/credit toggle buttons)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WalletScreen(
    lang: String,
    accounts: List<FinancialAccount>,
    transactions: List<Transaction>,
    totalNetWorth: Double,
    accountOrderList: List<Long>,
    onRearrangeOrderChange: (List<Long>) -> Unit,
    onAddManualDebit: () -> Unit,
    onAddManualCredit: () -> Unit,
    onDeleteAccount: (FinancialAccount) -> Unit,
    preferredBaseCurrency: String
) {
    var rearrangeMode by remember { mutableStateOf(false) }
    var draggingAccount by remember { mutableStateOf<FinancialAccount?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var activeCardStartBounds by remember { mutableStateOf<Rect?>(null) }
    var isHoveringDeleteZone by remember { mutableStateOf(false) }
    var deleteZoneBounds by remember { mutableStateOf<Rect?>(null) }
    var parentCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val accountBoundsMap = remember { mutableStateMapOf<Long, Rect>() }
    var accountToDelete by remember { mutableStateOf<FinancialAccount?>(null) }

    val sortedAccounts = remember(accounts, accountOrderList) {
        accounts.sortedBy { acc ->
            val idx = accountOrderList.indexOf(acc.id)
            if (idx == -1) 999 else idx
        }
    }

    if (accountToDelete != null) {
        AlertDialog(
            onDismissRequest = { accountToDelete = null },
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to disconnect ${accountToDelete?.name}? All transactional details will be removed permanently.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        accountToDelete?.let { onDeleteAccount(it) }
                        accountToDelete = null
                    }
                ) {
                    Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { accountToDelete = null }) {
                    Text("Keep")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { parentCoordinates = it }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }

            // Wallet header title networth block
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = getTranslate("WALLET_TITLE", lang).uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = Color(0xFF10B981)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                         text = formatCurrency(totalNetWorth, preferredBaseCurrency),
                         fontSize = 32.sp,
                         fontWeight = FontWeight.Black,
                         color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = getTranslate("NET_WORTH", lang),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    // Debit Expense & Credit Income manual action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onAddManualDebit,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF43F5E)), // Rose Color (Expense)
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(getTranslate("DEBIT", lang), fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onAddManualCredit,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)), // Emerald Color (Income)
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(getTranslate("CREDIT", lang), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Main Box frame container of all accounts
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        // Header inside of container box
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = getTranslate("CONNECTED_ACCOUNTS", lang),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            
                            if (accounts.isNotEmpty()) {
                                TextButton(
                                    onClick = { rearrangeMode = !rearrangeMode },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = if (rearrangeMode) MaterialTheme.colorScheme.primary else Color(0xFF10B981)
                                    )
                                ) {
                                    Text(
                                        text = if (rearrangeMode) getTranslate("DONE", lang) else getTranslate("REARRANGE_MODE", lang),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (sortedAccounts.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = getTranslate("WALANG_KONEKTADONG_BANKS", lang),
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        } else {
                            // Arrange mode notification banner
                            if (rearrangeMode) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0x1F10B981),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = Icons.Default.Info, contentDescription = "", tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(if (lang == "TL") "Hilain ang mga card para ayusin o i-delete." else "Drag cards to rearrange or delete.", fontSize = 11.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Display columns box
                            FlowRowLayout(
                                spacing = 10.dp
                            ) {
                                sortedAccounts.forEachIndexed { idx, acc ->
                                    val providerItem = remember(acc.providerName) { FinancialCatalog.findById(acc.providerName) }
                                    val cardTheme = remember(providerItem) {
                                        providerItem?.getCardTheme() ?: CardTheme(Color(0xFF0F172A), Color.White)
                                    }

                                    val isCurrentlyDragging = draggingAccount?.id == acc.id

                                    // Interactive Card with logo, brand background color, drag and drop support
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth(0.48f)
                                            .onGloballyPositioned { coords ->
                                                parentCoordinates?.let { parent ->
                                                    val bounds = parent.localBoundingBoxOf(coords)
                                                    if (!isCurrentlyDragging) {
                                                        accountBoundsMap[acc.id] = bounds
                                                    }
                                                }
                                            }
                                            .offset {
                                                if (isCurrentlyDragging) {
                                                    IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt())
                                                } else {
                                                    IntOffset.Zero
                                                }
                                            }
                                            .graphicsLayer {
                                                if (isCurrentlyDragging) {
                                                    scaleX = 1.1f
                                                    scaleY = 1.1f
                                                    alpha = 0.85f
                                                    shadowElevation = 12.dp.toPx()
                                                }
                                            }
                                            .border(
                                                if (rearrangeMode) 2.dp else 0.5.dp,
                                                if (rearrangeMode) cardTheme.textColor else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                                RoundedCornerShape(16.dp)
                                            )
                                            .pointerInput(rearrangeMode, sortedAccounts) {
                                                detectDragGesturesAfterLongPress(
                                                    onDragStart = { offset ->
                                                        if (rearrangeMode) {
                                                            draggingAccount = acc
                                                            dragOffset = Offset.Zero
                                                            activeCardStartBounds = accountBoundsMap[acc.id]
                                                        }
                                                    },
                                                    onDragEnd = {
                                                        if (rearrangeMode && draggingAccount != null) {
                                                            if (isHoveringDeleteZone) {
                                                                accountToDelete = draggingAccount
                                                            }
                                                            draggingAccount = null
                                                            dragOffset = Offset.Zero
                                                            activeCardStartBounds = null
                                                            isHoveringDeleteZone = false
                                                        }
                                                    },
                                                    onDragCancel = {
                                                        draggingAccount = null
                                                        dragOffset = Offset.Zero
                                                        activeCardStartBounds = null
                                                        isHoveringDeleteZone = false
                                                    },
                                                    onDrag = { change, dragAmount ->
                                                        change.consume()
                                                        dragOffset += dragAmount
                                                        val dragCenter = activeCardStartBounds?.let { it.center + dragOffset }
                                                        if (dragCenter != null) {
                                                            isHoveringDeleteZone = deleteZoneBounds?.contains(dragCenter) ?: false
                                                            
                                                            if (!isHoveringDeleteZone) {
                                                                val hoveredAccount = sortedAccounts.find { other ->
                                                                    other.id != acc.id && accountBoundsMap[other.id]?.contains(dragCenter) == true
                                                                }
                                                                if (hoveredAccount != null) {
                                                                    val currentOrder = accountOrderList.toMutableList()
                                                                    val idxA = currentOrder.indexOf(acc.id)
                                                                    val idxB = currentOrder.indexOf(hoveredAccount.id)
                                                                    if (idxA != -1 && idxB != -1) {
                                                                        currentOrder[idxA] = hoveredAccount.id
                                                                        currentOrder[idxB] = acc.id
                                                                        onRearrangeOrderChange(currentOrder)
                                                                        
                                                                        val boundsA = accountBoundsMap[acc.id]
                                                                        val boundsB = accountBoundsMap[hoveredAccount.id]
                                                                        if (boundsA != null && boundsB != null) {
                                                                            val slotDelta = boundsA.center - boundsB.center
                                                                            dragOffset += slotDelta
                                                                            activeCardStartBounds = boundsB
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                )
                                            },
                                        colors = CardDefaults.cardColors(
                                            containerColor = cardTheme.backgroundColor.copy(alpha = 0.95f)
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(
                                                text = providerItem?.displayName ?: "Digital Provider",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Black,
                                                color = cardTheme.textColor,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = acc.name,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = cardTheme.textColor.copy(alpha = 0.7f),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Spacer(modifier = Modifier.height(16.dp))

                                            Text(
                                                text = formatCurrency(acc.currentBalance, acc.currency),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = cardTheme.textColor
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                CategorySpendingPieChart(
                    transactions = transactions,
                    preferredBaseCurrency = preferredBaseCurrency,
                    lang = lang
                )
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        // Animated overlay for delete target zone at bottom
        AnimatedVisibility(
            visible = rearrangeMode && draggingAccount != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            val isHovered = isHoveringDeleteZone
            Card(
                modifier = Modifier
                    .size(90.dp)
                    .onGloballyPositioned { coords ->
                        parentCoordinates?.let { parent ->
                            deleteZoneBounds = parent.localBoundingBoxOf(coords)
                        }
                    },
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (isHovered) Color(0xFFEF4444) else Color(0xCCEF4444)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Drop to Delete",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "DELETE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// HISTORY TAB CONTENT PAGE: Dedicated transactions logger list with search and filter selections + starting-current trial
@Composable
fun HistoryScreen(
    lang: String,
    accounts: List<FinancialAccount>,
    transactions: List<Transaction>,
    onDeleteTransaction: (Transaction) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDateRange by remember { mutableStateOf("All Time") }
    var selectedAccountFilter by remember { mutableStateOf<Long?>(null) }
    var selectedTypeFilter by remember { mutableStateOf("All Types") }

    val filteredTransactions = remember(transactions, searchQuery, selectedDateRange, selectedAccountFilter, selectedTypeFilter, accounts) {
        transactions.filter { tx ->
            val accountName = accounts.find { it.id == tx.accountId }?.name ?: ""
            val matchesQuery = searchQuery.isBlank() || 
                    tx.merchantName.contains(searchQuery, ignoreCase = true) ||
                    accountName.contains(searchQuery, ignoreCase = true)

            val matchesAccount = selectedAccountFilter == null || tx.accountId == selectedAccountFilter
            val matchesType = selectedTypeFilter == "All Types" || tx.transactionType.equals(selectedTypeFilter, ignoreCase = true)

            val matchesDate = when (selectedDateRange) {
                "Today" -> {
                    val cal = Calendar.getInstance()
                    val startOfDay = cal.apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis
                    tx.timestamp >= startOfDay
                }
                "Last 7 Days" -> {
                    val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)
                    tx.timestamp >= sevenDaysAgo
                }
                "This Month" -> {
                    val cal = Calendar.getInstance()
                    val startOfMonth = cal.apply {
                        set(Calendar.DAY_OF_MONTH, 1)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis
                    tx.timestamp >= startOfMonth
                }
                else -> true
            }

            matchesQuery && matchesAccount && matchesType && matchesDate
        }
    }

    // Historical Running balances audit calculation maps
    val runningBalances = remember(transactions, accounts) {
        val map = mutableMapOf<Long, Double>()
        accounts.forEach { map[it.id] = it.startingBalance }

        val sortedChronologically = transactions.sortedBy { it.timestamp }
        val balancePoints = mutableMapOf<Long, Pair<Double, Double>>()

        sortedChronologically.forEach { tx ->
            val currentRunning = map[tx.accountId] ?: 0.0
            val nextRunning = if (tx.transactionType.uppercase() == "INCOME") {
                currentRunning + tx.amount
            } else {
                currentRunning - tx.amount
            }
            balancePoints[tx.id] = Pair(currentRunning, nextRunning)
            map[tx.accountId] = nextRunning
        }
        balancePoints
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = getTranslate("HISTORY_TITLE", lang).uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = Color(0xFF10B981)
                )
                Text(
                    text = "Analyze Audited Logs",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Search & Filter Box container
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { 
                        Text(
                            text = getTranslate("SEARCH_PLACEHOLDER", lang), 
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            fontSize = 13.sp
                        ) 
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "", tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("transaction_search_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                // Date Filter Chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val dates = listOf("All Time", "Today", "Last 7 Days", "This Month")
                    items(dates) { dateRange ->
                        val isSelected = selectedDateRange == dateRange
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.surface)
                                .border(1.dp, if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50))
                                .clickable { selectedDateRange = dateRange }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(text = dateRange, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                // Type Filter Chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val types = listOf("All Types", "Income", "Expense", "Transfer")
                    items(types) { type ->
                        val isSelected = selectedTypeFilter == type
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.surface)
                                .border(1.dp, if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50))
                                .clickable { selectedTypeFilter = type }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(text = type, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                // Selected account filter chip row
                if (accounts.isNotEmpty()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            val isSelected = selectedAccountFilter == null
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.surface)
                                    .border(1.dp, if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50))
                                    .clickable { selectedAccountFilter = null }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(text = "All Accounts", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        items(accounts) { acc ->
                            val isSelected = selectedAccountFilter == acc.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.surface)
                                    .border(1.dp, if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50))
                                    .clickable { selectedAccountFilter = acc.id }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(text = acc.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // List elements
        if (filteredTransactions.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(modifier = Modifier.padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No transactions match selected filters.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            items(
                items = filteredTransactions,
                key = { it.id }
            ) { tx ->
                val matchingAccount = accounts.find { it.id == tx.accountId }
                val balPoint = runningBalances[tx.id] ?: Pair(0.0, 0.0)
                val txCurrency = matchingAccount?.currency ?: "PHP"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val providerItem = remember(matchingAccount) { matchingAccount?.let { FinancialCatalog.findById(it.providerName) } }
                            InstitutionLogoIcon(
                                logoUrl = providerItem?.logoUrl ?: "",
                                displayName = tx.merchantName,
                                primaryColorHex = providerItem?.primaryColorHex ?: "#757575"
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(tx.merchantName, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("via " + (matchingAccount?.name ?: "Unknown Provider"), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            val isIncome = tx.transactionType.uppercase() == "INCOME"
                            // txCurrency inherited from outer scope
                            Text(
                                text = (if (isIncome) "+" else "-") + formatCurrency(tx.amount, txCurrency),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isIncome) Color(0xFF10B981) else Color(0xFFF43F5E)
                            )

                            IconButton(onClick = { onDeleteTransaction(tx) }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "", tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f), modifier = Modifier.size(16.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(10.dp))

                        // Audited starting -> ending running balances trace trail
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "STARTING: " + formatCurrency(balPoint.first, txCurrency),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "", tint = Color(0xFF10B981), modifier = Modifier.size(12.dp))
                            Text(
                                text = "ENDING: " + formatCurrency(balPoint.second, txCurrency),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF10B981)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// BUDGET PLANNING SCREEN: Sleek progression bar tracking goals by selected critical groups
@Composable
fun PlanScreen(
    lang: String,
    transactions: List<Transaction>,
    accounts: List<FinancialAccount>,
    preferredBaseCurrency: String
) {
    val exchangeRates = remember {
        mapOf(
            "PHP" to 1.0,
            "USD" to 58.5,
            "EUR" to 63.5,
            "GBP" to 74.5,
            "SGD" to 43.5
        )
    }

    // Categories to plan
    val budgetPlans = remember(transactions, accounts, preferredBaseCurrency) {
        val rateFromPhpToBase = exchangeRates[preferredBaseCurrency] ?: 1.0
        
        fun getSpentInBase(filterLambda: (Transaction) -> Boolean): Double {
            val spentPhp = transactions.filter(filterLambda).sumOf { trans ->
                val account = accounts.find { it.id == trans.accountId }
                val currency = account?.currency ?: "PHP"
                val rateToPhp = exchangeRates[currency] ?: 1.0
                trans.amount * rateToPhp
            }
            return if (rateFromPhpToBase != 0.0) spentPhp / rateFromPhpToBase else spentPhp
        }

        listOf(
            Triple("Food & Grocery", 8000.0 / rateFromPhpToBase, getSpentInBase { it.merchantName.lowercase().contains("food") || it.merchantName.lowercase().contains("grocery") || it.merchantName.lowercase().contains("supermarket") }),
            Triple("Shopping & Retail", 6000.0 / rateFromPhpToBase, getSpentInBase { it.merchantName.lowercase().contains("shopee") || it.merchantName.lowercase().contains("lazada") || it.merchantName.lowercase().contains("shopeepay") || it.merchantName.lowercase().contains("grab") }),
            Triple("Utilities & Bills", 12000.0 / rateFromPhpToBase, getSpentInBase { it.merchantName.lowercase().contains("maya") || it.merchantName.lowercase().contains("bdo") || it.merchantName.lowercase().contains("bills") || it.merchantName.lowercase().contains("electric") }),
            Triple("Others", 5000.0 / rateFromPhpToBase, getSpentInBase { !it.merchantName.lowercase().contains("food") && !it.merchantName.lowercase().contains("shopee") && !it.merchantName.lowercase().contains("bills") })
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(12.dp)) }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = getTranslate("BUDGET_PLAN_TITLE", lang).uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = Color(0xFF10B981)
                )
                Text(
                    text = getTranslate("PLANNING", lang),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = getTranslate("STABILITY_BUDGET", lang),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        budgetPlans.forEach { (category, limit, spent) ->
            item {
                val pct = if (limit == 0.0) 0f else (spent / limit).toFloat().coerceIn(0f, 1f)
                val barColor = if (pct > 0.85f) Color(0xFFF43F5E) else Color(0xFF10B981)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(category, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("${(pct * 100).toInt()}% Spent", fontSize = 12.sp, color = barColor, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = pct,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = barColor,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Spent " + formatCurrency(spent, preferredBaseCurrency),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Budget limit: " + formatCurrency(limit, preferredBaseCurrency),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// SYSTEM SETTINGS SCREEN (Replaces vault completely: Language Select card, permissions console toggler)
@Composable
fun SettingsScreen(
    lang: String,
    hasSMSPermission: Boolean,
    showSimulationPanel: Boolean,
    onSimulateSMS: (String, String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onToggleSMSPermission: () -> Unit,
    preferredBaseCurrency: String,
    onBaseCurrencyChange: (String) -> Unit,
    themeMode: String,
    onThemeModeChange: (String) -> Unit,
    userNickname: String,
    onNicknameChange: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(12.dp)) }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = getTranslate("SETTINGS_TITLE", lang).uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = Color(0xFF10B981)
                )
                Text(
                    text = getTranslate("SETTINGS_HEADER", lang),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Profile Card for editing nickname
        item {
            var isEditing by remember { mutableStateOf(false) }
            var editedNickname by remember { mutableStateOf(userNickname) }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (lang == "TL") "PROPILO" else "PROFILE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                        if (!isEditing) {
                            TextButton(
                                onClick = { isEditing = true; editedNickname = userNickname },
                                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF10B981))
                            ) {
                                Text(if (lang == "TL") "I-edit" else "Edit", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (isEditing) {
                        OutlinedTextField(
                            value = editedNickname,
                            onValueChange = { editedNickname = it },
                            label = { Text(if (lang == "TL") "Pangalan o Palayaw" else "Name or Nickname") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF10B981),
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isEditing = false; editedNickname = userNickname },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(getTranslate("CANCEL", lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    val trimmed = editedNickname.trim()
                                    if (trimmed.isNotEmpty()) {
                                        onNicknameChange(trimmed)
                                        isEditing = false
                                    }
                                },
                                enabled = editedNickname.trim().isNotEmpty(),
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(getTranslate("SAVE", lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x1F10B981)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            
                            Column {
                                Text(
                                    text = userNickname,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = if (lang == "TL") "I-edit ang pangalan" else "Tap to edit your name",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Language Select Picker Settings Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = getTranslate("SELECT_LANG", lang).uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // English Card UI Item
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (lang == "EN") Color(0x1F10B981) else Color.Transparent)
                                .border(
                                    if (lang == "EN") 1.5.dp else 1.dp,
                                    if (lang == "EN") Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onLanguageChange("EN") }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                  Text("🇺🇸", fontSize = 24.sp)
                                  Spacer(modifier = Modifier.height(4.dp))
                                  Text("English (US)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Tagalog Card UI Item
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (lang == "TL") Color(0x1F10B981) else Color.Transparent)
                                .border(
                                    if (lang == "TL") 1.5.dp else 1.dp,
                                    if (lang == "TL") Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onLanguageChange("TL") }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                  Text("🇵🇭", fontSize = 24.sp)
                                  Spacer(modifier = Modifier.height(4.dp))
                                  Text("Tagalog", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Theme Mode Settings Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = getTranslate("SELECT_THEME", lang).uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Light Mode Card Item
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (themeMode == "LIGHT") Color(0x1F10B981) else Color.Transparent)
                                .border(
                                    if (themeMode == "LIGHT") 1.5.dp else 1.dp,
                                    if (themeMode == "LIGHT") Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onThemeModeChange("LIGHT") }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                  Text("☀️", fontSize = 24.sp)
                                  Spacer(modifier = Modifier.height(4.dp))
                                  Text(getTranslate("LIGHT_MODE", lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Dark Mode Card Item
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (themeMode == "DARK") Color(0x1F10B981) else Color.Transparent)
                                .border(
                                    if (themeMode == "DARK") 1.5.dp else 1.dp,
                                    if (themeMode == "DARK") Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onThemeModeChange("DARK") }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                  Text("🌙", fontSize = 24.sp)
                                  Spacer(modifier = Modifier.height(4.dp))
                                  Text(getTranslate("DARK_MODE", lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Base Display Currency Settings Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = (if (lang == "TL") "PILIIN ANG BASE CURRENCY" else "SELECT BASE CURRENCY"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val baseCurrencies = listOf(
                            "PHP" to "🇵🇭 PHP",
                            "USD" to "🇺🇸 USD",
                            "EUR" to "🇪🇺 EUR",
                            "GBP" to "🇬🇧 GBP",
                            "SGD" to "🇸🇬 SGD"
                        )
                        baseCurrencies.forEach { (code, display) ->
                            val isSelected = preferredBaseCurrency == code
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Color(0x1F10B981) else Color.Transparent)
                                    .border(
                                        if (isSelected) 1.5.dp else 1.dp,
                                        if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { onBaseCurrencyChange(code) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = display,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // SMS Permission Console Config
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AUTOMATED INTENT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                        Text(
                            text = "SMS Realtime Auto-Sync",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (hasSMSPermission) "Permission is active. Log incoming SMS alerts autonomously." else "Permissions closed. Taps toggle to trigger warning dialog.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = hasSMSPermission,
                        onCheckedChange = { onToggleSMSPermission() },
                        colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF10B981))
                    )
                }
            }
        }

        // Developer Simulation Console Box
        if (showSimulationPanel) {
            item {
                SimulationConsole(onSimulateSMS, lang)
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// Developer Console simulation component
@Composable
fun SimulationConsole(
    onSimulateSMS: (String, String) -> Unit,
    lang: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF34D399)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "💻 SIMULATION DEV-KIT",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF34D399)
            )

            Button(
                onClick = { onSimulateSMS("GCash", "You have sent PHP 150.00 of GrabPay on 2026-06-09.") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Trigger GCash Grab Alert (150 PHP Exp)", fontSize = 11.sp)
            }

            Button(
                onClick = { onSimulateSMS("Maya", "MayaBank: Deposit received PHP 10,000.00 on 2026-06-09.") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Trigger Maya Bank Inbound (10,000 PHP Inc)", fontSize = 11.sp)
            }

            Button(
                onClick = { onSimulateSMS("BDO", "BDO Alert: BDO account ending 1234 debited PHP 4,500.00 to BDO-SM Grocery.") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Trigger BDO Grocery Alert (4,500 PHP Exp)", fontSize = 11.sp)
            }
        }
    }
}

// Bottom Navigation items renderer
@Composable
fun AlkansyaBottomNavigation(
    currentTab: String,
    onTabSelected: (String) -> Unit,
    lang: String
) {
    val isDark = MaterialTheme.colorScheme.background == Color(0xFF0F172A)
    val navBg = if (isDark) Color(0xFF0F172A) else MaterialTheme.colorScheme.surface
    val navBorder = if (isDark) BorderStroke(1.dp, Color(0xFF1E293B)) else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)

    Surface(
        color = navBg,
        tonalElevation = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        border = navBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tabs = listOf(
                Triple("HOME", getTranslate("HOME", lang), Icons.Default.Home),
                Triple("WALLET", getTranslate("WALLET", lang), Icons.Default.Wallet),
                Triple("HISTORY", getTranslate("HISTORY", lang), Icons.Default.History),
                Triple("PLAN", getTranslate("PLAN", lang), Icons.Default.CalendarMonth),
                Triple("SETTINGS", getTranslate("SETTINGS", lang), Icons.Default.Settings)
            )

            tabs.forEach { (tabId, label, icon) ->
                val isSelected = currentTab == tabId
                val iconColor = if (isSelected) {
                    if (isDark) Color(0xFF34D399) else MaterialTheme.colorScheme.primary
                } else {
                    if (isDark) Color(0xFF94A3B8) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                }
                val textColor = if (isSelected) {
                    if (isDark) Color.White else MaterialTheme.colorScheme.primary
                } else {
                    if (isDark) Color(0xFF64748B) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(tabId) }
                        .padding(vertical = 4.dp)
                        .testTag("nav_tab_$tabId"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Color(0x1F10B981) else Color.Transparent)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = label,
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        color = textColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// SMS Perms dialogue banner
@Composable
fun SMSPermissionBanner(
    onGrant: () -> Unit,
    lang: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF34D399)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.MailOutline, contentDescription = "", tint = Color(0xFF10B981), modifier = Modifier.size(20.dp))
                Text("SMS Alerts Tracking", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            Text(
                text = getTranslate("SMS_PERMISSION_PROMPT", lang),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
            Button(
                onClick = onGrant,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(50),
                modifier = Modifier.height(36.dp)
            ) {
                Text("Grant Tracking Permissions", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Dialog to add manual custom debit or credit presets quickly
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddManualTransactionDialog(
    lang: String,
    accounts: List<FinancialAccount>,
    typePreset: String, // "DEBIT" or "CREDIT"
    onDismiss: () -> Unit,
    onSave: (Long, Double, String, String, String) -> Unit
) {
    var selectedAccount by remember { mutableStateOf(accounts.firstOrNull()) }
    var amountString by remember { mutableStateOf("") }
    var merchantName by remember { mutableStateOf("") }
    var expandedMenu by remember { mutableStateOf(false) }

    val categories = if (typePreset == "DEBIT") {
        listOf("Food", "Transport", "Bills", "Shopping", "Entertainment", "Others")
    } else {
        listOf("Salary", "Investment", "Gift", "Others")
    }
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (typePreset == "DEBIT") getTranslate("DEBIT", lang) else getTranslate("CREDIT", lang),
                fontWeight = FontWeight.Black
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Account Link", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                
                ExposedDropdownMenuBox(
                    expanded = expandedMenu,
                    onExpandedChange = { expandedMenu = !expandedMenu }
                ) {
                    OutlinedTextField(
                        value = selectedAccount?.name ?: "No accounts selected",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMenu) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    DropdownMenu(
                        expanded = expandedMenu,
                        onDismissRequest = { expandedMenu = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        accounts.forEach { acc ->
                            DropdownMenuItem(
                                text = { Text(acc.name + " (" + phpFormatMini.format(acc.currentBalance) + ")", fontSize = 12.sp) },
                                onClick = {
                                    selectedAccount = acc
                                    expandedMenu = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = amountString,
                    onValueChange = { amountString = it },
                    label = { Text("Amount (PHP)") },
                    placeholder = { Text("0.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = merchantName,
                    onValueChange = { merchantName = it },
                    label = { Text("Merchant / Description") },
                    placeholder = { Text("e.g. Starbucks or Salary Pay") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Text(if (lang == "TL") "Kategorya" else "Category", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSel = selectedCategory == cat
                        val icon = when (cat) {
                            "Food" -> "🍔"
                            "Transport" -> "🚗"
                            "Bills" -> "💵"
                            "Shopping" -> "🛍️"
                            "Entertainment" -> "🎬"
                            "Salary" -> "💼"
                            "Investment" -> "📈"
                            "Gift" -> "🎁"
                            else -> "📦"
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) Color(0x1F10B981) else MaterialTheme.colorScheme.surfaceVariant)
                                .border(
                                    1.dp,
                                    if (isSel) Color(0xFF10B981) else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(icon, fontSize = 11.sp)
                                Text(cat, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val accId = selectedAccount?.id ?: 0L
                    val amt = amountString.toDoubleOrNull() ?: 0.0
                    val type = if (typePreset == "DEBIT") "EXPENSE" else "INCOME"
                    val merch = merchantName.ifBlank { if (typePreset == "DEBIT") "Expense Trade" else "Deposit Inbound" }
                    onSave(accId, amt, type, merch, selectedCategory)
                },
                enabled = selectedAccount != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
            ) {
                Text(getTranslate("SAVE", lang))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(getTranslate("CANCEL", lang))
            }
        }
    )
}

fun formatCurrency(amount: Double, currency: String): String {
    val locale = when (currency) {
        "USD" -> Locale.US
        "EUR" -> Locale.FRANCE
        "GBP" -> Locale.UK
        "SGD" -> Locale("en", "SG")
        else -> Locale("en", "PH") // Default is PHP
    }
    return try {
        val symbol = when (currency) {
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            "SGD" -> "S$"
            "PHP" -> "₱"
            else -> "₱"
        }
        val decimalFormat = java.text.NumberFormat.getNumberInstance(Locale.US)
        decimalFormat.minimumFractionDigits = 2
        decimalFormat.maximumFractionDigits = 2
        symbol + decimalFormat.format(amount)
    } catch (e: Exception) {
        val decimalFormat = java.text.NumberFormat.getNumberInstance(Locale.US)
        decimalFormat.minimumFractionDigits = 2
        decimalFormat.maximumFractionDigits = 2
        currency + " " + decimalFormat.format(amount)
    }
}

private val phpFormatMini = NumberFormat.getCurrencyInstance(Locale("en", "PH"))

// Simple custom row matching the test assertions and look-and-feel of transactions list elements
@Composable
fun TransactionItemRow(
    transaction: Transaction,
    accountName: String,
    accountColorHex: String,
    onDelete: () -> Unit,
    currency: String = "PHP"
) {
    val formatter = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US) }
    val isIncome = transaction.transactionType == "INCOME"

    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Tanggalin ang Transaksyon") },
            text = { Text("Sigurado ka bang nais mong burahin ang transaksyon na ito?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    }
                ) {
                    Text("Burahin", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("I-cancel")
                }
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .clickable { showDeleteConfirm = true }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isIncome) Color(0x2210B981)
                    else Color(0x22F43F5E)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (transaction.isAutomatedFromSMS) Icons.Default.Email else Icons.Default.Person,
                contentDescription = if (transaction.isAutomatedFromSMS) "SMS" else "Manual",
                tint = if (isIncome) Color(0xFF10B981) else Color(0xFFF43F5E),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = transaction.merchantName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                
                val catIcon = when (transaction.category) {
                    "Food" -> "🍔"
                    "Transport" -> "🚗"
                    "Bills" -> "💵"
                    "Shopping" -> "🛍️"
                    "Entertainment" -> "🎬"
                    "Salary" -> "💼"
                    "Investment" -> "📈"
                    "Gift" -> "🎁"
                    else -> "📦"
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0x1F10B981),
                ) {
                    Text(
                        text = "$catIcon ${transaction.category}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = accountName,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "•",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                Text(
                    text = formatter.format(Date(transaction.timestamp)),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            val amountPrefix = if (isIncome) "+" else "-"
            val amountColor = if (isIncome) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface
            
            Text(
                text = "$amountPrefix${formatCurrency(transaction.amount, currency)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = amountColor
            )
        }
    }
}

@Composable
fun CategorySpendingPieChart(
    transactions: List<Transaction>,
    preferredBaseCurrency: String,
    lang: String
) {
    val expenseTransactions = remember(transactions) {
        transactions.filter { it.transactionType == "EXPENSE" }
    }

    if (expenseTransactions.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = (if (lang == "TL") "ESTADISTIKA NG GASTOS" else "EXPENSE DISTRIBUTION").uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10B981)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (lang == "TL") "Walang nakatalang gastos sa kasalukuyan." else "No expense records found yet to display distribution.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    val categoryExpenses = remember(expenseTransactions) {
        expenseTransactions.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }
    }

    val totalExpense = remember(categoryExpenses) {
        categoryExpenses.sumOf { it.second }
    }

    val colorPalette = listOf(
        Color(0xFFEF4444), // Red
        Color(0xFF3B82F6), // Blue
        Color(0xFFF59E0B), // Amber
        Color(0xFF10B981), // Emerald
        Color(0xFF8B5CF6), // Purple
        Color(0xFFEC4899), // Pink
        Color(0xFF06B6D4), // Cyan
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = (if (lang == "TL") "MGA GASTOS BAWAT KATEGORYA" else "EXPENSE BY CATEGORY").uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF10B981)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Premium Donut Chart Canvas
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        var startAngle = -90f
                        categoryExpenses.forEachIndexed { index, (cat, amount) ->
                            val sweepAngle = ((amount / totalExpense) * 360f).toFloat()
                            val color = colorPalette[index % colorPalette.size]
                            drawArc(
                                color = color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(
                                    width = 16.dp.toPx(),
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                            )
                            startAngle += sweepAngle
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (lang == "TL") "Gastos" else "Expenses",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatCurrency(totalExpense, preferredBaseCurrency),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Legend List with custom indicator colors and percentages
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categoryExpenses.forEachIndexed { index, (cat, amount) ->
                        val pct = (amount / totalExpense) * 100
                        val color = colorPalette[index % colorPalette.size]
                        val icon = when (cat) {
                            "Food" -> "🍔"
                            "Transport" -> "🚗"
                            "Bills" -> "💵"
                            "Shopping" -> "🛍️"
                            "Entertainment" -> "🎬"
                            else -> "📦"
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                                Text(
                                    text = "$icon $cat",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = String.format(Locale.US, "%.1f%%", pct),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
