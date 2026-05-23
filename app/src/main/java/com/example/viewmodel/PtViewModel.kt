package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class PtViewModel(application: Application) : AndroidViewModel(application) {

    private val ptDao = PtDatabase.getDatabase(application).ptDao()
    private val repository = PtRepository(ptDao)

    // Current menu & navigation states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    // Database backed states
    val cartItems = repository.cartItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val loyaltyProfile = repository.loyaltyProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val feedbackList = repository.allCommentsFeedback.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bookingsList = repository.allCoachingBookings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val activeAndPastOrders = repository.activeAndPastOrders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Derived filtered menu items
    val filteredMenu = combine(
        _searchQuery,
        _selectedCategory
    ) { query, cat ->
        repository.fullMenu.filter { item ->
            val matchesCategory = if (cat == "All") true else item.category.equals(cat, ignoreCase = true)
            val matchesQuery = if (query.isEmpty()) true else {
                item.name.contains(query, ignoreCase = true) ||
                item.description.contains(query, ignoreCase = true) ||
                item.category.contains(query, ignoreCase = true)
            }
            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.fullMenu
    )

    init {
        // Run seed for first launch
        seedInitialLoyaltyProfile()
        seedInitialOrders()
    }

    private fun seedInitialOrders() {
        viewModelScope.launch {
            repository.activeAndPastOrders.firstOrNull()?.let { list ->
                if (list.isEmpty()) {
                    repository.placeOrder(
                        PtOrder(
                            serializedItems = "1x Grilled Chicken (200 gm) [BBQ]\n1x Chocolate Protein Shake\n1x Paneer Wrap (100 gm paneer)",
                            totalCost = 518,
                            totalCalories = 1080,
                            totalProtein = 90,
                            totalCarbs = 65,
                            totalFats = 38,
                            orderType = "Dine In",
                            status = "Completed",
                            trackingProgress = 1.0f,
                            timestamp = System.currentTimeMillis() - 86400000 * 2 // 2 days ago
                        )
                    )
                    repository.placeOrder(
                        PtOrder(
                            serializedItems = "2x Chicken (150g) + Shakes Combo [Peri-Peri]\n1x Peanut Butter Banana Shake",
                            totalCost = 747,
                            totalCalories = 1350,
                            totalProtein = 112,
                            totalCarbs = 90,
                            totalFats = 39,
                            orderType = "Home Delivery",
                            status = "Completed",
                            trackingProgress = 1.0f,
                            timestamp = System.currentTimeMillis() - 86400000 * 5 // 5 days ago
                        )
                    )
                }
            }
        }
    }

    private fun seedInitialLoyaltyProfile() {
        viewModelScope.launch {
            repository.loyaltyProfile.first()?.let {
                // Profile exists
            } ?: run {
                repository.saveProfile(
                    LoyaltyProfile(
                        memberId = "PT-JAIPUR-${Random.nextInt(1000, 9999)}",
                        name = "Alpha Gym Rat",
                        points = 180,
                        visitsCount = 12,
                        levelName = "Gold Shredder"
                    )
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    // --- Cart Actions ---
    fun addItemToCart(
        product: MenuProduct,
        flavor: String = "",
        addons: List<String> = emptyList(),
        extraCost: Int = 0
    ) {
        viewModelScope.launch {
            // Check if exact same item with same config exists
            val existing = cartItems.value.find { 
                it.menuItemId == product.id && it.chosenFlavor == flavor && it.chosenAddons == addons.joinToString(",")
            }

            if (existing != null) {
                repository.updateCartQuantity(existing, existing.quantity + 1)
            } else {
                val formattedAddons = addons.joinToString(",")
                val addedCal = addons.size * 50 // roughly mock extra addon macros
                val addedProt = if (addons.any { it.contains("Chicken", true) }) 10 else if (addons.any { it.contains("Egg", true) }) 6 else 0
                
                repository.addToCart(
                    CartItem(
                        menuItemId = product.id,
                        name = product.name,
                        category = product.category,
                        price = product.price + extraCost,
                        quantity = 1,
                        chosenFlavor = flavor,
                        chosenAddons = formattedAddons,
                        extraCost = extraCost,
                        calories = product.calories + addedCal,
                        protein = product.protein + addedProt,
                        carbs = product.carbs,
                        fats = product.fats
                    )
                )
            }
        }
    }

    fun incrementCartQty(item: CartItem) {
        viewModelScope.launch {
            repository.updateCartQuantity(item, item.quantity + 1)
        }
    }

    fun decrementCartQty(item: CartItem) {
        viewModelScope.launch {
            repository.updateCartQuantity(item, item.quantity - 1)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // --- Checkout & Real-time Live Tracking Simulation ---
    fun checkout(orderType: String) {
        val currentCart = cartItems.value
        if (currentCart.isEmpty()) return

        val totalCost = currentCart.sumOf { it.price * it.quantity }
        val sumCal = currentCart.sumOf { it.calories * it.quantity }
        val sumProt = currentCart.sumOf { it.protein * it.quantity }
        val sumCarbs = currentCart.sumOf { it.carbs * it.quantity }
        val sumFats = currentCart.sumOf { it.fats * it.quantity }

        val itemsSummary = currentCart.joinToString("\n") { 
            val config = buildString {
                if (it.chosenFlavor.isNotEmpty()) append("[${it.chosenFlavor}]")
                if (it.chosenAddons.isNotEmpty()) append(" + ${it.chosenAddons}")
            }
            "${it.quantity}x ${it.name} $config"
        }

        viewModelScope.launch {
            // Clear prior cart
            repository.clearCart()

            // Credit Loyalty Points proportional to spending: 1 point per 10 INR
            val pointsEarned = totalCost / 10
            val profile = loyaltyProfile.value ?: LoyaltyProfile()
            val newPoints = profile.points + pointsEarned
            val newVisits = profile.visitsCount + 1
            val newLevel = when {
                newPoints > 500 -> "Platinum Titan"
                newPoints > 250 -> "Gold Bulker"
                else -> "Silver Shredder"
            }
            repository.saveProfile(
                profile.copy(
                    points = newPoints,
                    visitsCount = newVisits,
                    levelName = newLevel
                )
            )

            // Insert initial active state order
            val newOrder = PtOrder(
                serializedItems = itemsSummary,
                totalCost = totalCost,
                totalCalories = sumCal,
                totalProtein = sumProt,
                totalCarbs = sumCarbs,
                totalFats = sumFats,
                orderType = orderType,
                status = "Order Placed",
                trackingProgress = 0.15f
            )
            repository.placeOrder(newOrder)

            // Start simulated real-time tracking progression cascade in the background
            simulateRealtimeTracking()
        }
    }

    private fun simulateRealtimeTracking() {
        viewModelScope.launch {
            // Get the latest placed order
            val orders = activeAndPastOrders.value
            if (orders.isNotEmpty()) {
                val targetOrder = orders.firstOrNull { it.status == "Order Placed" || it.trackingProgress < 1.0f } ?: return@launch
                
                // Transition 1: Cooking in progress
                delay(8000)
                repository.updateOrder(
                    targetOrder.copy(
                        status = "In Kitchen: Meal Sizzling!",
                        trackingProgress = 0.40f
                    )
                )

                // Transition 2: Nutrition/Macro validation step
                delay(8000)
                repository.updateOrder(
                    targetOrder.copy(
                        status = "Nutrition Validation Complete!",
                        trackingProgress = 0.70f
                    )
                )

                // Transition 3: Packing under high-satiety container
                delay(8000)
                repository.updateOrder(
                    targetOrder.copy(
                        status = "Packed & Thermal Insulated",
                        trackingProgress = 0.90f
                    )
                )

                // Transition 4: Completed and ready for food drop or physical pickup
                delay(8000)
                val finalStatus = if (targetOrder.orderType == "Home Delivery") "Out for Delivery" else "Ready at Cash Counter!"
                repository.updateOrder(
                    targetOrder.copy(
                        status = finalStatus,
                        trackingProgress = 1.00f
                    )
                )
            }
        }
    }

    // --- Book Coaching ---
    fun submitBooking(
        clientName: String,
        clientPhone: String,
        slotTime: String,
        coachingType: String,
        date: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.bookCoaching(
                CoachingBooking(
                    clientName = clientName,
                    clientPhone = clientPhone,
                    chosenSlotTime = slotTime,
                    coachingType = coachingType,
                    bookingDate = date,
                    details = notes,
                    status = "Confirmed"
                )
            )
        }
    }

    // --- Feedback Action ---
    fun submitFeedback(foodRating: Float, serviceRating: Float, comments: String) {
        viewModelScope.launch {
            repository.saveFeedback(
                Feedback(
                    foodRating = foodRating,
                    serviceRating = serviceRating,
                    comments = comments
                )
            )
        }
    }

    fun clearAllOrders() {
        viewModelScope.launch {
            repository.clearAllOrders()
        }
    }

    fun injectCustomBillToHistory(order: PtOrder) {
        viewModelScope.launch {
            repository.placeOrder(order)
        }
    }

    fun accruePointsSimulated(clientName: String, clientPhone: String, points: Int) {
        viewModelScope.launch {
            val current = loyaltyProfile.value ?: LoyaltyProfile()
            val newPoints = current.points + points
            val tier = when {
                newPoints > 500 -> "Platinum Titan"
                newPoints > 250 -> "Gold Bulker"
                else -> "Silver Shredder"
            }
            repository.saveProfile(
                current.copy(
                    name = if (clientName.isNotEmpty()) clientName else current.name,
                    points = newPoints,
                    levelName = tier
                )
            )
        }
    }
}
