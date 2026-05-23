package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val menuItemId: String,
    val name: String,
    val category: String,
    val price: Int,
    val quantity: Int,
    val chosenFlavor: String = "", // e.g. "PERI PERI", "BBQ" etc (+20)
    val chosenAddons: String = "", // Comma-separated add-ons names
    val extraCost: Int = 0,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int
)

@Entity(tableName = "loyalty_profile")
data class LoyaltyProfile(
    @PrimaryKey val memberId: String = "PT-JAIPUR-8849",
    val name: String = "Gym Rat Cadet",
    val points: Int = 180, // Default loyalty points
    val visitsCount: Int = 12,
    val levelName: String = "Gold Bulker" // "Silver Shredder", "Gold Bulker", "Platinum Titan"
)

@Entity(tableName = "feedback_entries")
data class Feedback(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val foodRating: Float,
    val serviceRating: Float,
    val comments: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "coaching_bookings")
data class CoachingBooking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientName: String,
    val clientPhone: String,
    val chosenSlotTime: String, // e.g. "07:00 AM - 08:30 AM"
    val coachingType: String, // e.g. "Fat Loss Transformation", "Hypertrophy Gain"
    val bookingDate: String, // e.g. "2026-05-24"
    val details: String = "",
    val status: String = "Confirmed", // "Pending", "Confirmed", "Completed"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "pt_orders")
data class PtOrder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serializedItems: String, // JSON style list or simple summary string
    val totalCost: Int,
    val totalCalories: Int,
    val totalProtein: Int,
    val totalCarbs: Int,
    val totalFats: Int,
    val orderType: String, // "Dine In", "Takeaway", "Home Delivery"
    val status: String, // "Order Placed", "In Kitchen", "Macros Checked", "Packing Out", "Ready for Pickup"
    val trackingProgress: Float, // 0.2f for placed, 0.5f for preparation, 1.0f for complete
    val timestamp: Long = System.currentTimeMillis()
)

// Standard static menu items representing the menu image
data class MenuProduct(
    val id: String,
    val name: String,
    val category: String,
    val price: Int,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val isHiglighted: Boolean = false,
    val description: String = ""
)
