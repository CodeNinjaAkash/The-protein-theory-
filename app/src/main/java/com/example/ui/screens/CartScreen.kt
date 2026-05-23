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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartItem
import com.example.viewmodel.PtViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: PtViewModel, onNavigateToMenu: () -> Unit) {
    val cartItems by viewModel.cartItems.collectAsState()
    val ordersList by viewModel.activeAndPastOrders.collectAsState()

    var selectedOrderType by remember { mutableStateOf("Dine In") }

    val totalCost = cartItems.sumOf { it.price * it.quantity }
    val totalCalories = cartItems.sumOf { it.calories * it.quantity }
    val totalProtein = cartItems.sumOf { it.protein * it.quantity }
    val totalCarbs = cartItems.sumOf { it.carbs * it.quantity }
    val totalFats = cartItems.sumOf { it.fats * it.quantity }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp) // pad above bottom tabs
        ) {
            // Cart Header
            Text(
                text = "YOUR WORKOUT CART",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                letterSpacing = 1.sp,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )

            if (cartItems.isEmpty() && ordersList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CancelPresentation,
                            contentDescription = "Empty",
                            tint = Color.Gray,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No calorie fuels added yet!",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Explore the Protein Theory high-protein catalog to feed your muscles.",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNavigateToMenu,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1E27)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("GO TO HEALTH MENU", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cart Products Section
                    if (cartItems.isNotEmpty()) {
                        item {
                            Text(
                                text = "ACTIVE ITEMS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF1E27),
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        items(cartItems) { item ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = item.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        
                                        // Specific modifiers label description
                                        val configParts = mutableListOf<String>()
                                        if (item.chosenFlavor.isNotEmpty()) configParts.add(item.chosenFlavor)
                                        if (item.chosenAddons.isNotEmpty()) configParts.add(item.chosenAddons)
                                        
                                        if (configParts.isNotEmpty()) {
                                            Text(
                                                text = configParts.joinToString(" + "),
                                                color = Color(0xFFFF1E27),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        
                                        // Cumulative protein/macro indicator for this specific item quantity
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${item.protein * item.quantity}g P | ${item.calories * item.quantity} kcal",
                                                color = Color.Gray,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Text(text = "₹${item.price} each", color = Color.Gray, fontSize = 11.sp)
                                    }

                                    // Counter modifiers
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        IconButton(
                                            onClick = { viewModel.decrementCartQty(item) },
                                            modifier = Modifier
                                                .background(Color(0xFF0C0C0D), RoundedCornerShape(50))
                                                .size(28.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus", tint = Color.White, modifier = Modifier.size(14.dp))
                                        }

                                        Text(text = "${item.quantity}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                                        IconButton(
                                            onClick = { viewModel.incrementCartQty(item) },
                                            modifier = Modifier
                                                .background(Color(0xFF0C0C0D), RoundedCornerShape(50))
                                                .size(28.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.Add, contentDescription = "Plus", tint = Color.White, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                }
                            }
                        }

                        // Cumulative Cart Macro Scoreboard (Gym Rat feature)
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFF10B981).copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1410))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("TOTAL MUSCLE MACROS", color = Color(0xFF10B981), fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        MacroHeaderStats("Protein", "${totalProtein}g Check", Color(0xFF10B981))
                                        MacroHeaderStats("Energy", "${totalCalories} kcal", Color.White)
                                        MacroHeaderStats("Carbs", "${totalCarbs}g source", Color(0xFFFF9800))
                                        MacroHeaderStats("Fats", "${totalFats}g limits", Color(0xFFEF4444))
                                    }
                                }
                            }
                        }

                        // Dine in / Pickup Toggle
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "CHECKOUT TYPE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF1E27),
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF161619), RoundedCornerShape(10.dp))
                                    .padding(4.dp)
                            ) {
                                val types = listOf("Dine In", "Self-Pickup", "Home Delivery")
                                types.forEach { type ->
                                    val isSelected = selectedOrderType == type
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(
                                                if (isSelected) Color(0xFFFF1E27) else Color.Transparent,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .clickable { selectedOrderType = type }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = type,
                                            color = if (isSelected) Color.White else Color.Gray,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Order Cost Breakdown Summary and button
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF161619))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Subtotal cost", color = Color.Gray, fontSize = 13.sp)
                                        Text("₹$totalCost", color = Color.White, fontSize = 13.sp)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Macro label packaging", color = Color.Gray, fontSize = 13.sp)
                                        Text("FREE", color = Color(0xFF10B981), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFF26262B))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("FINAL PAYABLE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text("₹$totalCost", color = Color(0xFFFF1E27), fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    }
                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = { viewModel.checkout(selectedOrderType) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .testTag("checkout_button"),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1E27)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            "CONFIRM SECURE DIET ORDER • ₹$totalCost",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Active Tracker Timeline Visual (Under placed history)
                    if (ordersList.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ACTIVE LAB PREP & TRACKING",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF1E27),
                                    letterSpacing = 2.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                TextButton(onClick = { viewModel.clearAllOrders() }) {
                                    Text("Reset History", color = Color.Gray, fontSize = 11.sp)
                                }
                            }
                        }

                        items(ordersList) { order ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "PT Order ID: #${order.id + 10450}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(text = "Option: ${order.orderType}", color = Color.Gray, fontSize = 11.sp)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    if (order.trackingProgress == 1.0f) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFFF1E27).copy(alpha = 0.15f),
                                                    RoundedCornerShape(4.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = order.status,
                                                color = if (order.trackingProgress == 1.0f) Color(0xFF10B981) else Color(0xFFFF1E27),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = order.serializedItems, color = Color(0xFF9E9EA4), fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Dynamic Horizontal tracking dots / stages
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TrackingNode("Placed", order.trackingProgress >= 0.15f, modifier = Modifier.weight(1f))
                                        TrackingConnector(order.trackingProgress >= 0.40f)
                                        TrackingNode("Cooking", order.trackingProgress >= 0.40f, modifier = Modifier.weight(1f))
                                        TrackingConnector(order.trackingProgress >= 0.70f)
                                        TrackingNode("Checked", order.trackingProgress >= 0.70f, modifier = Modifier.weight(1f))
                                        TrackingConnector(order.trackingProgress >= 0.90f)
                                        TrackingNode("Sealed", order.trackingProgress >= 0.90f, modifier = Modifier.weight(1f))
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Energy Breakdown verification
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFF0C0C0D), RoundedCornerShape(6.dp))
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Text("P: ${order.totalProtein}g", color = Color(0xFF10B981), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text("C: ${order.totalCarbs}g", color = Color(0xFFFF9800), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text("F: ${order.totalFats}g", color = Color(0xFFEF4444), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text("Cal: ${order.totalCalories}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text("Total: ₹${order.totalCost}", color = Color(0xFFFF1E27), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MacroHeaderStats(name: String, value: String, color: Color) {
    Column {
        Text(name, color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Text(value, color = color, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
fun TrackingNode(title: String, isActive: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(50))
                .background(if (isActive) Color(0xFFFF1E27) else Color(0xFF26262B)),
            contentAlignment = Alignment.Center
        ) {
            if (isActive) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, color = if (isActive) Color.White else Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RowScope.TrackingConnector(isActive: Boolean) {
    Box(
        modifier = Modifier
            .weight(0.4f)
            .height(2.dp)
            .background(if (isActive) Color(0xFFFF1E27) else Color(0xFF26262B))
    )
}
