package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MenuProduct
import com.example.viewmodel.PtViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(viewModel: PtViewModel, onNavigateToCart: () -> Unit) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredMenu by viewModel.filteredMenu.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()

    // Active bottom-sheet customizer state variables
    var showCustomizerProduct by remember { mutableStateOf<MenuProduct?>(null) }
    var chosenFlavor by remember { mutableStateOf("") }
    val chosenAddons = remember { mutableStateListOf<String>() }

    val categories = listOf("All", "Egg Specials", "Veg & Plant Protein", "Grill Specials", "Quick Bites", "Shakes & Drinks", "Combo Deals")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0D))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // Search Bar at Top of Menu
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Search by egg, paneer, oats, meal...", color = Color.Gray, fontSize = 13.sp) },
                prefix = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFFFF1E27),
                        modifier = Modifier.padding(end = 6.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF1E27),
                    unfocusedBorderColor = Color(0xFF26262B),
                    focusedContainerColor = Color(0xFF161619),
                    unfocusedContainerColor = Color(0xFF161619),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            )

            // Scrollable row category tags
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Color(0xFFFF1E27) else Color(0xFF161619))
                            .clickable { viewModel.selectCategory(category) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else Color(0xFF9E9EA4),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Menu Items List view
            if (filteredMenu.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.SentimentDissatisfied, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("No matching gym-feed found!", color = Color.Gray, fontSize = 14.sp)
                        Text("Try searching chicken, wrap, boiled, or shake", color = Color(0xFF5E5E64), fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredMenu) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    chosenFlavor = ""
                                    chosenAddons.clear()
                                    showCustomizerProduct = product
                                }
                                .animateContentSize(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Visual indicators
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (product.category.contains("Grill") || product.name.contains("Egg") || product.name.contains("Chicken")) Color(0xFFE53E3E) else Color(0xFF38A169),
                                                    RoundedCornerShape(4.dp)
                                                )
                                                .size(10.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = product.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Text(
                                        text = "₹${product.price}",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp,
                                        color = Color(0xFFFF1E27)
                                    )
                                }

                                if (product.description.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = product.description,
                                        fontSize = 11.sp,
                                        color = Color(0xFF9E9EA4),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Macros Display Grid Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    MacroPill("Protein (P)", "${product.protein}g", Color(0xFF10B981).copy(alpha = 0.15f), Color(0xFF10B981))
                                    MacroPill("Carbs (C)", "${product.carbs}g", Color(0xFFFF9800).copy(alpha = 0.15f), Color(0xFFFF9800))
                                    MacroPill("Fats (F)", "${product.fats}g", Color(0xFFEF4444).copy(alpha = 0.15f), Color(0xFFEF4444))
                                    MacroPill("Energy", "${product.calories} kcal", Color.White.copy(alpha = 0.05f), Color.White)
                                }
                            }
                        }
                    }
                }
            }

            // Interactive Bottom-Tray indicating items in cart quick action
            AnimatedVisibility(
                visible = cartItems.isNotEmpty(),
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Surface(
                    onClick = onNavigateToCart,
                    color = Color(0xFFFF1E27),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 76.dp), // stay above bottom tab bar navigation
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.ShoppingBasket, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${cartItems.sumOf { it.quantity }} items added to target diet",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "VIEW ORDER CART",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp).padding(start = 2.dp))
                        }
                    }
                }
            }
        }

        // Custom Bottom Sheet style overlay modal (3D tactical design)
        if (showCustomizerProduct != null) {
            val product = showCustomizerProduct!!
            
            // Background dim barrier
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f))
                    .clickable { showCustomizerProduct = null }
            )

            // Sheet container
            AnimatedVisibility(
                visible = showCustomizerProduct != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 60.dp) // padding above navigation structure
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFFF1E27), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .clickable(enabled = false) {}, // prevent click-through closures
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Drag Indicator block
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(50))
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Header details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = product.name, fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                                Text(text = product.category, fontSize = 11.sp, color = Color(0xFFFF1E27), fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { showCustomizerProduct = null }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Macros block inside sheet
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0C0C0D), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("PROTEIN", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text("${product.protein}g", fontSize = 14.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("CARBS", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text("${product.carbs}g", fontSize = 14.sp, color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("FATS", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text("${product.fats}g", fontSize = 14.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("CALORIES", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                Text("${product.calories} kcal", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }

                        // FLAVOR SELECTION (Only if Grill Special or egg/paneer items)
                        if (product.category.contains("Grill", true) || product.category.contains("Protein", true) || product.category.contains("Egg", true)) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("CHOSE CORE ATHLETIC FLAVOUR (+₹20)", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            val flavors = listOf("PERI PERI", "BBQ SMOKI", "GARLIC BUTTER", "LEMON HERB SHRED")
                            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                                flavors.forEach { f ->
                                    val isSelected = chosenFlavor == f
                                    Box(
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) Color(0xFFFF1E27) else Color(0xFF0C0C0D))
                                            .clickable { chosenFlavor = if (isSelected) "" else f }
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(text = f, color = if (isSelected) Color.White else Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // EXTRAS ADD-ONS (Except Combo Deals)
                        if (!product.category.contains("Combo", true)) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("CAFE SPECIAL ADD-ONS", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            val addonsAvailable = listOf(
                                "Extra Grilled Chicken (+₹40)" to "Extra Chicken",
                                "Raw Low-Fat Paneer (+₹70)" to "Extra Paneer",
                                "Single Boiled Egg (+₹20)" to "Extra Egg",
                                "Whey Isolate Scoop (+₹80)" to "Whey Scoop"
                            )

                            addonsAvailable.forEach { (label, name) ->
                                val isChecked = chosenAddons.contains(name)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (isChecked) chosenAddons.remove(name) else chosenAddons.add(name)
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = {
                                            if (isChecked) chosenAddons.remove(name) else chosenAddons.add(name)
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFF1E27))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(label, color = Color.White, fontSize = 13.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Compute active customization pricing
                        val flavorCost = if (chosenFlavor.isNotEmpty()) 20 else 0
                        val addonCost = chosenAddons.sumOf {
                            when (it) {
                                "Extra Chicken" -> 40
                                "Extra Paneer" -> 70
                                "Extra Egg" -> 20
                                "Whey Scoop" -> 80
                                else -> 0
                            }
                        }
                        val finalPrice = product.price + flavorCost + addonCost

                        Button(
                            onClick = {
                                viewModel.addItemToCart(
                                    product = product,
                                    flavor = chosenFlavor,
                                    addons = chosenAddons,
                                    extraCost = flavorCost + addonCost
                                )
                                showCustomizerProduct = null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("add_to_cart_confirm"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1E27)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "ADD TO WORKOUT CART • ₹$finalPrice",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MacroPill(label: String, value: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(
            text = "$label: $value",
            color = textColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
