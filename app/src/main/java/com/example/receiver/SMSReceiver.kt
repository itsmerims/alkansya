package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.example.data.local.FinancialDatabase
import com.example.data.repository.FinancialRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Production-ready SMS Receiver to parse transaction alerts dynamically in background.
 * Listens for RECEIVE_SMS broadcasts and securely processes them using safe regex patterns.
 */
class SMSReceiver : BroadcastReceiver() {
    
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            if (messages.isNullOrEmpty()) return

            Log.d("SMSReceiver", "Received entry. Parsing ${messages.size} message parts.")
            
            val db = FinancialDatabase.getDatabase(context)
            val repository = FinancialRepository(db.financialDao())

            for (message in messages) {
                val sender = message.displayOriginatingAddress ?: message.originatingAddress
                val messageBody = message.displayMessageBody ?: message.messageBody

                if (sender.isNullOrEmpty() || messageBody.isNullOrEmpty()) continue

                // Process in a secure Coroutine context
                scope.launch {
                    try {
                        val parsed = repository.processIncomingSMS(sender, messageBody)
                        if (parsed) {
                            Log.i("SMSReceiver", "Successfully processed & recorded transaction from SMS sender: $sender")
                        } else {
                            Log.d("SMSReceiver", "SMS from sender: $sender did not match any local transaction patterns.")
                        }
                    } catch (e: Exception) {
                        Log.e("SMSReceiver", "Crash protected parsing failure for sender: $sender", e)
                    }
                }
            }
        }
    }
}
