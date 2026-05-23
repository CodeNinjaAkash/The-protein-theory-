package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PtDao {

    // --- CART OPS ---
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Update
    suspend fun updateCartItem(item: CartItem)

    @Delete
    suspend fun deleteCartItem(item: CartItem)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItemById(id: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()


    // --- LOYALTY OPS ---
    @Query("SELECT * FROM loyalty_profile ORDER BY memberId LIMIT 1")
    fun getLoyaltyProfile(): Flow<LoyaltyProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLoyaltyProfile(profile: LoyaltyProfile)


    // --- FEEDBACK OPS ---
    @Query("SELECT * FROM feedback_entries ORDER BY timestamp DESC")
    fun getAllFeedback(): Flow<List<Feedback>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: Feedback)


    // --- COACHING BOOKINGS OPS ---
    @Query("SELECT * FROM coaching_bookings ORDER BY timestamp DESC")
    fun getAllBookings(): Flow<List<CoachingBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: CoachingBooking)


    // --- ORDER TRACKING OPS ---
    @Query("SELECT * FROM pt_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<PtOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: PtOrder)

    @Update
    suspend fun updateOrder(order: PtOrder)

    @Query("DELETE FROM pt_orders WHERE id = :orderId")
    suspend fun deleteOrderById(orderId: Int)

    @Query("DELETE FROM pt_orders")
    suspend fun clearAllOrders()
}
