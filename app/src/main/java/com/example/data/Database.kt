package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CartItem::class,
        LoyaltyProfile::class,
        Feedback::class,
        CoachingBooking::class,
        PtOrder::class
    ],
    version = 2,
    exportSchema = false
)
abstract class PtDatabase : RoomDatabase() {
    abstract fun ptDao(): PtDao

    companion object {
        @Volatile
        private var INSTANCE: PtDatabase? = null

        fun getDatabase(context: Context): PtDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PtDatabase::class.java,
                    "protein_theory_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
