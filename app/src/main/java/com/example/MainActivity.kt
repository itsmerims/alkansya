package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.collectAsState
import com.example.ui.AlkansyaApp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.FinancialViewModel

class MainActivity : ComponentActivity() {
    
    private lateinit var viewModel: FinancialViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Dynamic Edge-to-Edge window insets mapping
        enableEdgeToEdge()
        
        // Instantiate our secure local state manager
        viewModel = ViewModelProvider(this)[FinancialViewModel::class.java]
        
        setContent {
            val themeMode = viewModel.themeMode.collectAsState()
            val isDark = themeMode.value == "DARK"
            MyApplicationTheme(darkTheme = isDark) {
                AlkansyaApp(viewModel = viewModel)
            }
        }
    }
}
