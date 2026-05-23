package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PtRepository(private val ptDao: PtDao) {

    // Persistent reactive states
    val cartItems: Flow<List<CartItem>> = ptDao.getCartItems()
    val loyaltyProfile: Flow<LoyaltyProfile?> = ptDao.getLoyaltyProfile()
    val allCommentsFeedback: Flow<List<Feedback>> = ptDao.getAllFeedback()
    val allCoachingBookings: Flow<List<CoachingBooking>> = ptDao.getAllBookings()
    val activeAndPastOrders: Flow<List<PtOrder>> = ptDao.getAllOrders()

    // --- Static Full Menu (Macro Guide included) ---
    val fullMenu: List<MenuProduct> = listOf(
        // Egg Specials
        MenuProduct("egg_1", "Boiled Egg (1 pc)", "Egg Specials", 15, 70, 6, 1, 5, false, "1 Whole boiled egg seasoned slightly to kickstart metabolism."),
        MenuProduct("egg_2", "Boiled Eggs (2 pcs)", "Egg Specials", 30, 140, 12, 2, 10, true, "2 Double boiled eggs. Prime standard breakfast for clean protein."),
        MenuProduct("egg_3", "Boiled Eggs (4 pcs)", "Egg Specials", 60, 280, 24, 4, 20, true, "4 Loaded boiled eggs. Ideal for heavy muscle repair."),
        MenuProduct("egg_4", "3 Egg Masala Omelette", "Egg Specials", 109, 270, 18, 3, 21, false, "3 Fluffy pan-flipped eggs with rich onion, tomato, and peppers."),
        MenuProduct("egg_5", "Cheese Omelette", "Egg Specials", 119, 330, 22, 4, 26, false, "3 Eggs folded over rich cheddar. Clean fat-to-protein fuel."),
        MenuProduct("egg_6", "Scrambled Eggs", "Egg Specials", 129, 290, 18, 3, 22, false, "3 Creamy scrambled eggs cooked in minimal fat."),
        MenuProduct("egg_7", "Egg Bhurji (3 eggs)", "Egg Specials", 129, 300, 18, 5, 22, false, "Dry spiced Indian egg scramble with premium core aromatics."),

        // Veg & Plant Protein
        MenuProduct("veg_1", "Paneer Salad (120 gm)", "Veg & Plant Protein", 149, 300, 18, 10, 20, true, "Diced fresh low-fat cottage cheese over leafy greens and rich dressing."),
        MenuProduct("veg_2", "Paneer Wrap (100 gm paneer)", "Veg & Plant Protein", 169, 350, 20, 30, 18, false, "Low carb high protein whole wheat wrap loaded with spiced cottage cheese cubes."),
        MenuProduct("veg_3", "Paneer Bhurji (120 gm)", "Veg & Plant Protein", 159, 320, 19, 8, 22, false, "Crushed fresh paneer sautéed with dynamic Indian bell peppers and spices."),
        MenuProduct("veg_4", "Paneer Sandwich (80-100 gm)", "Veg & Plant Protein", 139, 300, 15, 28, 15, false, "Grilled multi-grain bread stuffing juicy spiced paneer layers."),
        MenuProduct("veg_5", "Tofu Stir Fry (120 gm)", "Veg & Plant Protein", 159, 220, 16, 8, 12, true, "Gluten-free organic tofu tosses alongside fresh high-fiber veggies."),
        MenuProduct("veg_6", "Soya Chunk Bowl (80 gm)", "Veg & Plant Protein", 149, 280, 24, 18, 6, true, "Highly potent soy chunks high in density. Ultimate plant muscle builder."),
        MenuProduct("veg_7", "Soya Wrap (80 gm)", "Veg & Plant Protein", 139, 300, 20, 30, 8, false, "Fresh soy nugget chunks spiced nicely in whole-grain high-fiber wheat wraps."),

        // Grill Specials
        MenuProduct("grill_1", "Grilled Chicken (150 gm)", "Grill Specials", 140, 250, 35, 0, 10, true, "Tender chicken breast breast grilled dry, zero carbon carb loading."),
        MenuProduct("grill_2", "Grilled Chicken (200 gm)", "Grill Specials", 170, 330, 45, 0, 14, true, "Ultimate bodybuilding fuel. Heavy 200 gm of premium quality chicken."),
        MenuProduct("grill_3", "Grilled Fish (150 gm)", "Grill Specials", 160, 220, 30, 0, 9, false, "Lean river fish rich in Omega-3 fatty acids grilled lightly."),
        MenuProduct("grill_4", "Grilled Fish (200 gm)", "Grill Specials", 190, 300, 40, 0, 12, false, "Double fillet clean dry high omega fish feed."),

        // Quick Bites
        MenuProduct("qb_1", "Chicken Wrap (120 gm)", "Quick Bites", 179, 400, 28, 35, 15, true, "Spiced succulent chicken logs rolled inside high-grade tortilla bases."),
        MenuProduct("qb_2", "Paneer Roll (100 gm)", "Quick Bites", 159, 350, 20, 30, 18, false, "Thick delicious roll packed with creamy low fat marinated paneer layers."),
        MenuProduct("qb_3", "Veg Sandwich", "Quick Bites", 119, 250, 10, 35, 8, false, "Multi-layered dynamic vegetable sandwich with clean mint spreads."),

        // Shakes & Drinks
        MenuProduct("drink_1", "Peanut Butter Banana Shake", "Shakes & Drinks", 149, 350, 12, 40, 15, false, "Sweet real banana puree blended beautifully with rich loaded crunchy peanut butter."),
        MenuProduct("drink_2", "Chocolate Protein Shake", "Shakes & Drinks", 179, 250, 25, 20, 6, true, "Premium dynamic whey isolate double blended into high protein creamy drink."),
        MenuProduct("drink_3", "Oats Smoothie (300 ml)", "Shakes & Drinks", 159, 300, 10, 45, 8, false, "High satiety instant energetic meal consisting oats, honey, and fresh seeds."),
        MenuProduct("drink_4", "Cold Coffee Protein (300 ml)", "Shakes & Drinks", 169, 220, 20, 18, 5, true, "Strong organic espresso shot combined with high-grade Whey Isolate protein shaker."),
        MenuProduct("drink_5", "Lemonade Active", "Shakes & Drinks", 60, 15, 0, 4, 0, false, "Tangy fresh lemon squeezed with zero-calorie sweetener to balance electrolytes."),
        MenuProduct("drink_6", "Iced Tea Health Booster", "Shakes & Drinks", 60, 20, 0, 5, 0, false, "Brewed black tea leaves served frosty cold loaded with metabolic vitamins."),
        MenuProduct("drink_7", "Intense Green Tea", "Shakes & Drinks", 70, 5, 0, 1, 0, false, "Raw high antioxidant match green tea to accelerate daily calorie burns."),
        MenuProduct("drink_8", "Detox Water Spark", "Shakes & Drinks", 70, 2, 0, 0, 0, false, "Infused mint, cucumber, and fresh lemon slices boosting deep systemic detox."),

        // Combo Deals
        MenuProduct("combo_1", "Chicken (150g) + Shakes", "Combo Deals", 299, 500, 50, 25, 12, true, "Bulk pack: Grilled breast + high scoop whey shake. Packs an amazing 50g protein!"),
        MenuProduct("combo_2", "Paneer Wrap + Shakes", "Combo Deals", 249, 500, 30, 45, 15, false, "High energy combo: Cottage cheese whole wheat wrap plus thick berry smoothie."),
        MenuProduct("combo_3", "Egg Meal + Cold Coffee", "Combo Deals", 199, 350, 20, 22, 10, false, "Breakfast champion: 3 boiled eggs with our cold coffee protein shaker.")
    )

    // Write-through mutations
    suspend fun addToCart(item: CartItem) {
        ptDao.insertCartItem(item)
    }

    suspend fun updateCartQuantity(item: CartItem, newQty: Int) {
        if (newQty <= 0) {
            ptDao.deleteCartItem(item)
        } else {
            ptDao.updateCartItem(item.copy(quantity = newQty))
        }
    }

    suspend fun removeFromCart(item: CartItem) {
        ptDao.deleteCartItem(item)
    }

    suspend fun clearCart() {
        ptDao.clearCart()
    }

    suspend fun saveProfile(profile: LoyaltyProfile) {
        ptDao.saveLoyaltyProfile(profile)
    }

    suspend fun saveFeedback(feedback: Feedback) {
        ptDao.insertFeedback(feedback)
    }

    suspend fun bookCoaching(booking: CoachingBooking) {
        ptDao.insertBooking(booking)
    }

    suspend fun placeOrder(order: PtOrder) {
        ptDao.insertOrder(order)
    }

    suspend fun updateOrder(order: PtOrder) {
        ptDao.updateOrder(order)
    }

    suspend fun deleteOrder(orderId: Int) {
        ptDao.deleteOrderById(orderId)
    }

    suspend fun clearAllOrders() {
        ptDao.clearAllOrders()
    }
}
