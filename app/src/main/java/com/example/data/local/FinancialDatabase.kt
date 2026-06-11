package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FinancialAccount::class, Transaction::class, SavingsVault::class], version = 4, exportSchema = false)
abstract class FinancialDatabase : RoomDatabase() {
    abstract fun financialDao(): FinancialDao

    companion object {
        @Volatile
        private var INSTANCE: FinancialDatabase? = null

        fun getDatabase(context: Context): FinancialDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FinancialDatabase::class.java,
                    "alkansya_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
