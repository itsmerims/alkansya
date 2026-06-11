package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class AccountType {
    WALLET,
    DIGITAL_BANK,
    TRADITIONAL_BANK,
    FOREIGN_BANK
}

enum class InstitutionGroup(val displayName: String) {
    E_WALLET("E-Wallets"),
    DIGITAL_BANK("Digital Banks"),
    TRADITIONAL_BANK("Local Universal/Commercial Banks"),
    FOREIGN_BANK("Foreign Banks")
}

enum class Institution(
    val displayName: String,
    val senderIds: List<String>,
    val type: AccountType,
    val group: InstitutionGroup,
    val primaryColorHex: String,
    val icon: ImageVector,
    val logoUrl: String
) {
    // E-Wallets
    GCASH(
        "GCash", 
        listOf("GCash", "G-Cash"), 
        AccountType.WALLET, 
        InstitutionGroup.E_WALLET, 
        "#0052FF", 
        Icons.Default.Star,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/GCash_logo.svg/512px-GCash_logo.svg.png"
    ),
    MAYA(
        "Maya", 
        listOf("Maya", "PayMaya"), 
        AccountType.WALLET, 
        InstitutionGroup.E_WALLET, 
        "#00E676", 
        Icons.Default.ShoppingCart,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/PayMaya_Logo.svg/512px-PayMaya_Logo.svg.png"
    ),
    GRABPAY(
        "GrabPay", 
        listOf("Grab", "GrabPay"), 
        AccountType.WALLET, 
        InstitutionGroup.E_WALLET, 
        "#00B0FF", 
        Icons.Default.Share,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Grab_Logo.svg/512px-Grab_Logo.svg.png"
    ),
    SHOPEEPAY(
        "ShopeePay", 
        listOf("ShopeePay", "Shopee"), 
        AccountType.WALLET, 
        InstitutionGroup.E_WALLET, 
        "#FF5722", 
        Icons.Default.ShoppingCart,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/ShopeePay_logo.svg/512px-ShopeePay_logo.svg.png"
    ),

    // Digital Banks
    MAYA_BANK(
        "Maya Bank", 
        listOf("MAYABANK", "MayaBank"), 
        AccountType.DIGITAL_BANK, 
        InstitutionGroup.DIGITAL_BANK, 
        "#00C853", 
        Icons.Default.Star,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/PayMaya_Logo.svg/512px-PayMaya_Logo.svg.png"
    ),
    UNO_BANK(
        "Uno Digital Bank", 
        listOf("UNObank", "UNO_Bank"), 
        AccountType.DIGITAL_BANK, 
        InstitutionGroup.DIGITAL_BANK, 
        "#E040FB", 
        Icons.Default.Build,
        "https://unobank.asia/img/unobank-favicon.png"
    ),
    GOTYME(
        "GoTyme Bank", 
        listOf("GoTyme", "GoTymeBank"), 
        AccountType.DIGITAL_BANK, 
        InstitutionGroup.DIGITAL_BANK, 
        "#FFD600", 
        Icons.Default.Face,
        "https://upload.wikimedia.org/wikipedia/commons/d/df/GoTyme_Bank_logo.png"
    ),
    TONIK(
        "Tonik Bank", 
        listOf("Tonik", "TonikBank"), 
        AccountType.DIGITAL_BANK, 
        InstitutionGroup.DIGITAL_BANK, 
        "#FF4081", 
        Icons.Default.ThumbUp,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Tonik_Bank_Logo.svg/512px-Tonik_Bank_Logo.svg.png"
    ),
    MARIBANK(
        "MariBank", 
        listOf("MariBank", "Mari Bank", "Sea", "SeaMoney"), 
        AccountType.DIGITAL_BANK, 
        InstitutionGroup.DIGITAL_BANK, 
        "#FF6D00", 
        Icons.Default.Face,
        "https://logo.clearbit.com/maribank.com.sg"
    ),
    CIMB(
        "CIMB Bank", 
        listOf("CIMB", "CIMBBank"), 
        AccountType.DIGITAL_BANK, 
        InstitutionGroup.DIGITAL_BANK, 
        "#D50000", 
        Icons.Default.Refresh,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/CIMB_Group_Logo.svg/512px-CIMB_Group_Logo.svg.png"
    ),

    // Traditional Banks
    BDO(
        "BDO", 
        listOf("BDO", "BDO-Alert", "BDO_Alert"), 
        AccountType.TRADITIONAL_BANK, 
        InstitutionGroup.TRADITIONAL_BANK, 
        "#0D47A1", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/BDO_Unibank_logo.svg/512px-BDO_Unibank_logo.svg.png"
    ),
    BPI(
        "BPI", 
        listOf("BPI", "BPI-Alert", "BPI_Alert", "BPIExpress"), 
        AccountType.TRADITIONAL_BANK, 
        InstitutionGroup.TRADITIONAL_BANK, 
        "#B71C1C", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/5/57/Bank_of_the_Philippine_Islands_logo.svg/512px-Bank_of_the_Philippine_Islands_logo.svg.png"
    ),
    METROBANK(
        "Metrobank", 
        listOf("Metrobank", "MB_Alert"), 
        AccountType.TRADITIONAL_BANK, 
        InstitutionGroup.TRADITIONAL_BANK, 
        "#1565C0", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Metrobank_logo.svg/512px-Metrobank_logo.svg.png"
    ),
    UNIONBANK(
        "UnionBank", 
        listOf("UnionBank", "UB", "Union_Bank"), 
        AccountType.TRADITIONAL_BANK, 
        InstitutionGroup.TRADITIONAL_BANK, 
        "#FF6F00", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a1/Union_Bank_of_the_Philippines_logo.svg/512px-Union_Bank_of_the_Philippines_logo.svg.png"
    ),
    RCBC(
        "RCBC", 
        listOf("RCBC", "RCBC-Alert"), 
        AccountType.TRADITIONAL_BANK, 
        InstitutionGroup.TRADITIONAL_BANK, 
        "#1B5E20", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Rizal_Commercial_Banking_Corporation_Logo.svg/512px-Rizal_Commercial_Banking_Corporation_Logo.svg.png"
    ),
    SEC_BANK(
        "Security Bank", 
        listOf("SecBank", "SecurityBank"), 
        AccountType.TRADITIONAL_BANK, 
        InstitutionGroup.TRADITIONAL_BANK, 
        "#004D40", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/Security_Bank_Corporation_logo.svg/512px-Security_Bank_Corporation_logo.svg.png"
    ),
    PNB(
        "PNB", 
        listOf("PNB", "PNB_Alert"), 
        AccountType.TRADITIONAL_BANK, 
        InstitutionGroup.TRADITIONAL_BANK, 
        "#01579B", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Philippine_National_Bank_Logo.svg/512px-Philippine_National_Bank_Logo.svg.png"
    ),
    LANDBANK(
        "Landbank", 
        listOf("Landbank", "LBP"), 
        AccountType.TRADITIONAL_BANK, 
        InstitutionGroup.TRADITIONAL_BANK, 
        "#2E7D32", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/Landbank_logo.svg/512px-Landbank_logo.svg.png"
    ),
    CHINABANK(
        "Chinabank", 
        listOf("CBC", "Chinabank"), 
        AccountType.TRADITIONAL_BANK, 
        InstitutionGroup.TRADITIONAL_BANK, 
        "#C62828", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/China_Banking_Corporation_logo.svg/512px-China_Banking_Corporation_logo.svg.png"
    ),

    // Foreign Banks
    CITIBANK(
        "Citibank", 
        listOf("Citibank", "Citi", "CitiAlert"), 
        AccountType.FOREIGN_BANK, 
        InstitutionGroup.FOREIGN_BANK, 
        "#0288D1", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Citibank.svg/512px-Citibank.svg.png"
    ),
    HSBC(
        "HSBC", 
        listOf("HSBC", "HSBC_Alert"), 
        AccountType.FOREIGN_BANK, 
        InstitutionGroup.FOREIGN_BANK, 
        "#D32F2F", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/HSBC_logo_%282018%29.svg/512px-HSBC_logo_%282018%29.svg.png"
    ),
    STAN_CHART(
        "Standard Chartered", 
        listOf("StanChart", "SCB"), 
        AccountType.FOREIGN_BANK, 
        InstitutionGroup.FOREIGN_BANK, 
        "#388E3C", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Standard_Chartered_%282021%29.svg/512px-Standard_Chartered_%282021%29.svg.png"
    ),
    MAYBANK(
        "Maybank", 
        listOf("Maybank", "Maybank_Alert"), 
        AccountType.FOREIGN_BANK, 
        InstitutionGroup.FOREIGN_BANK, 
        "#FBC02D", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a1/Maybank-Logo.svg/512px-Maybank-Logo.svg.png"
    ),
    WISE(
        "Wise", 
        listOf("Wise", "TransferWise", "Transfer-Wise"), 
        AccountType.FOREIGN_BANK, 
        InstitutionGroup.FOREIGN_BANK, 
        "#9FE870", 
        Icons.Default.Home,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Wise_Logomark.svg/512px-Wise_Logomark.svg.png"
    );

    val id: String get() = name
}

object FinancialCatalog {
    val items = Institution.values().toList()

    fun findBySenderId(sender: String): Institution? {
        val cleanSender = sender.uppercase().trim()
        return items.find { inst ->
            inst.senderIds.any { id -> id.uppercase() == cleanSender } ||
                    inst.id == cleanSender ||
                    cleanSender.contains(inst.id) ||
                    inst.senderIds.any { id -> cleanSender.contains(id.uppercase()) }
        }
    }

    fun findById(id: String): Institution? {
        return items.find { it.id == id }
    }
}

data class CardTheme(
    val backgroundColor: Color,
    val textColor: Color
)

fun Institution.getCardTheme(): CardTheme {
    return when (this) {
        Institution.GCASH -> CardTheme(Color(0xFF005CE6), Color(0xFFFFFFFF))
        Institution.MAYA -> CardTheme(Color(0xFF111111), Color(0xFF00E676))
        Institution.GRABPAY -> CardTheme(Color(0xFF00B14F), Color(0xFFFFFFFF))
        Institution.SHOPEEPAY -> CardTheme(Color(0xFFFFF1ED), Color(0xFFEE4D2D))
        Institution.MAYA_BANK -> CardTheme(Color(0xFF0C1017), Color(0xFF00E676))
        Institution.UNO_BANK -> CardTheme(Color(0xFF3F00FF), Color(0xFFFFFFFF))
        Institution.GOTYME -> CardTheme(Color(0xFF4D2C18), Color(0xFFFAFAFA))
        Institution.TONIK -> CardTheme(Color(0xFF2A004F), Color(0xFFFF007A))
        Institution.MARIBANK -> CardTheme(Color(0xFFE6F2FF), Color(0xFF007AFF))
        Institution.CIMB -> CardTheme(Color(0xFF8C0B12), Color(0xFFFFFFFF))
        Institution.BDO -> CardTheme(Color(0xFF003366), Color(0xFFFFCC00))
        Institution.BPI -> CardTheme(Color(0xFFD11919), Color(0xFFFFFFFF))
        Institution.METROBANK -> CardTheme(Color(0xFF0038A8), Color(0xFFFFFFFF))
        Institution.UNIONBANK -> CardTheme(Color(0xFF231F20), Color(0xFFFF6600))
        Institution.RCBC -> CardTheme(Color(0xFF008080), Color(0xFFFFFFFF))
        Institution.SEC_BANK -> CardTheme(Color(0xFF006A4E), Color(0xFFFFFFFF))
        Institution.PNB -> CardTheme(Color(0xFF002060), Color(0xFFFFFFFF))
        Institution.LANDBANK -> CardTheme(Color(0xFF006633), Color(0xFFFFCC00))
        Institution.CHINABANK -> CardTheme(Color(0xFF1F4E79), Color(0xFFFFFFFF))
        Institution.CITIBANK -> CardTheme(Color(0xFF003B70), Color(0xFFFFFFFF))
        Institution.HSBC -> CardTheme(Color(0xFF333333), Color(0xFFDB0011))
        Institution.STAN_CHART -> CardTheme(Color(0xFF003F87), Color(0xFFFFFFFF))
        Institution.MAYBANK -> CardTheme(Color(0xFFFFCC00), Color(0xFF000000))
        Institution.WISE -> CardTheme(Color(0xFF1E2B47), Color(0xFF25B84C))
    }
}
