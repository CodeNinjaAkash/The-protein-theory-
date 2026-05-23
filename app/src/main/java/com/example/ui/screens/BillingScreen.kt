package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.PtOrder
import com.example.data.MenuProduct
import com.example.viewmodel.PtViewModel
import com.example.ui.components.ProteinTheoryLogo
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingScreen(
    viewModel: PtViewModel,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current
    val orders by viewModel.activeAndPastOrders.collectAsState()
    
    // Custom Bill Generator State
    var showBillGenerator by remember { mutableStateOf(false) }
    val menuList = viewModel.filteredMenu.collectAsState().value
    
    // Custom order items selected for manual bill creation
    var selectedProductForBill by remember { mutableStateOf<MenuProduct?>(null) }
    var selectedQty by remember { mutableIntStateOf(1) }
    val customBillItems = remember { mutableStateListOf<Pair<MenuProduct, Int>>() }
    var customOrderType by remember { mutableStateOf("Dine In") }
    
    // Receipt popup viewer state
    var selectedOrderForReceipt by remember { mutableStateOf<PtOrder?>(null) }
    var showCustomGeneratedReceipt by remember { mutableStateOf(false) }
    var newlyCreatedCustomOrder by remember { mutableStateOf<PtOrder?>(null) }

    // Date/Time helper
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }

    // Calculate aggregated metrics
    val totalOrdersCount = orders.size
    val totalExpenditure = orders.sumOf { it.totalCost }
    val totalProteinCount = orders.sumOf { it.totalProtein }
    val totalCaloriesCount = orders.sumOf { it.totalCalories }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            // Dashboard header with back button and brand details
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFFF1E27).copy(alpha = 0.15f), Color.Transparent)
                        )
                    )
                    .padding(top = 12.dp, bottom = 12.dp, start = 12.dp, end = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackToHome,
                        modifier = Modifier.testTag("billing_back_button")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "BILLING DASHBOARD",
                            color = Color(0xFFFF1E27),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "My Digital Invoices",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    // Total point accrual display on badge
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
                        border = BorderStroke(1.dp, Color(0xFFFF1E27).copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = null, tint = Color(0xFFFF1E27), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Bills: $totalOrdersCount",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                // High fidelity brand logo header
                ProteinTheoryLogo(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    scale = 0.9f
                )

                // Micro metrics grid combining billing and metabolic targets
                Text(
                    text = "LIFETIME REFUELD METRICS",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricMiniCard(
                        title = "TOTAL SPENT",
                        value = "₹$totalExpenditure",
                        icon = Icons.Default.Payments,
                        iconColor = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        title = "PROTEIN BULK",
                        value = "${totalProteinCount}g",
                        icon = Icons.Default.FitnessCenter,
                        iconColor = Color(0xFF3B82F6),
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        title = "TOTAL FUEL",
                        value = "$totalCaloriesCount Cal",
                        icon = Icons.Default.LocalFireDepartment,
                        iconColor = Color(0xFFFFD700),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Interactive CUSTOM BILL GENERATOR Panel Launcher
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showBillGenerator = !showBillGenerator }
                        .border(1.dp, Color(0xFFFF1E27).copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131112))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Receipt, contentDescription = null, tint = Color(0xFFFF1E27))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "LIVE INTERACTIVE RECEIPT BUILDER",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                               )
                               Text(
                                    text = "Build items dynamically & generate full official tax invoice with macros!",
                                    color = Color.Gray,
                                    fontSize = 10.sp
                               )
                            }
                            Icon(
                                imageVector = if (showBillGenerator) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }

                        AnimatedVisibility(visible = showBillGenerator) {
                            Column(modifier = Modifier.padding(top = 14.dp)) {
                                Divider(color = Color(0xFF26262B))
                                Spacer(modifier = Modifier.height(12.dp))

                                // Select Product
                                Text("Choose healthy product", color = Color.Gray, fontSize = 11.sp)
                                var expandedMenuDropdown by remember { mutableStateOf(false) }
                                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                    OutlinedButton(
                                        onClick = { expandedMenuDropdown = true },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                        border = BorderStroke(1.dp, Color(0xFF26262B)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = selectedProductForBill?.name ?: "Select from Menu...",
                                            fontSize = 13.sp,
                                            textAlign = TextAlign.Start,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                                    }

                                    DropdownMenu(
                                        expanded = expandedMenuDropdown,
                                        onDismissRequest = { expandedMenuDropdown = false },
                                        modifier = Modifier.background(Color(0xFF161619)).heightIn(max = 240.dp)
                                    ) {
                                        menuList.forEach { product ->
                                            DropdownMenuItem(
                                                text = { Text("${product.name} (₹${product.price})", color = Color.White, fontSize = 13.sp) },
                                                onClick = {
                                                    selectedProductForBill = product
                                                    expandedMenuDropdown = false
                                                }
                                            )
                                        }
                                    }
                                }

                                // Quantity Select
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Set Quantity", color = Color.Gray, fontSize = 11.sp)
                                    
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { if (selectedQty > 1) selectedQty-- }) {
                                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.White)
                                        }
                                        Text("$selectedQty", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(horizontal = 12.dp))
                                        IconButton(onClick = { selectedQty++ }) {
                                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White)
                                        }
                                    }
                                }

                                // Add to Draft button
                                Button(
                                    onClick = {
                                        val prod = selectedProductForBill
                                        if (prod == null) {
                                            Toast.makeText(context, "Please select a product first", Toast.LENGTH_SHORT).show()
                                        } else {
                                            customBillItems.add(prod to selectedQty)
                                            Toast.makeText(context, "Added to Custom Bill draft!", Toast.LENGTH_SHORT).show()
                                            selectedProductForBill = null
                                            selectedQty = 1
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26262B)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Add to Draft List", fontSize = 12.sp)
                                }

                                // Items added in current custom draft
                                if (customBillItems.isNotEmpty()) {
                                    Text("CURRENT DRAFT LIST", color = Color(0xFFFF1E27), fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.sp, modifier = Modifier.padding(top = 10.dp, bottom = 4.dp))
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0C0C0D)),
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            customBillItems.forEachIndexed { index, pair ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text("${pair.second}x ${pair.first.name}", color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                                    Text("₹${pair.first.price * pair.second}", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    IconButton(
                                                        onClick = { customBillItems.removeAt(index) },
                                                        modifier = Modifier.size(18.dp)
                                                    ) {
                                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(14.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Order type selection for custom bill
                                    Text("Order Fulfilment Type", color = Color.Gray, fontSize = 11.sp)
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        listOf("Dine In", "Takeaway", "Home Delivery").forEach { type ->
                                            val isSel = customOrderType == type
                                            Card(
                                                colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFFFF1E27).copy(alpha = 0.15f) else Color(0xFF161619)),
                                                border = BorderStroke(1.dp, if (isSel) Color(0xFFFF1E27) else Color.Transparent),
                                                modifier = Modifier.weight(1f).clickable { customOrderType = type }
                                            ) {
                                                Text(
                                                    type,
                                                    color = if (isSel) Color.White else Color.Gray,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                                                )
                                            }
                                        }
                                    }

                                    // Generate and render final dynamic tax bill
                                    Button(
                                        onClick = {
                                            // Compute aggregated statistics
                                            val totCost = customBillItems.sumOf { it.first.price * it.second }
                                            val totCal = customBillItems.sumOf { it.first.calories * it.second }
                                            val totProt = customBillItems.sumOf { it.first.protein * it.second }
                                            val totCarb = customBillItems.sumOf { it.first.carbs * it.second }
                                            val totFat = customBillItems.sumOf { it.first.fats * it.second }
                                            
                                            val serialized = customBillItems.joinToString("\n") { 
                                                "${it.second}x ${it.first.name} (M:${it.first.protein}g P)"
                                            }

                                            // Assemble a new simulated past order record
                                            newlyCreatedCustomOrder = PtOrder(
                                                serializedItems = serialized,
                                                totalCost = totCost,
                                                totalCalories = totCal,
                                                totalProtein = totProt,
                                                totalCarbs = totCarb,
                                                totalFats = totFat,
                                                orderType = customOrderType,
                                                status = "Completed (Offline)",
                                                trackingProgress = 1.0f,
                                                timestamp = System.currentTimeMillis()
                                            )

                                            showCustomGeneratedReceipt = true
                                        },
                                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1E27)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.DoubleArrow, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("COMPILE & PRINT CUSTOM BILL", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Customer Order History Invoice Section Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ARCHIVED PHYSICAL BILLS",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = "Clear History",
                        color = Color.DarkGray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            viewModel.clearAllOrders()
                            Toast.makeText(context, "Archive and invoices purged.", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // List of past invoices/bills
                if (orders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.Inbox, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No past billing invoices recorded.", color = Color.Gray, fontSize = 12.sp)
                            Text("Place orders in Cart or use the Receipt Builder!", color = Color.DarkGray, fontSize = 10.sp)
                        }
                    }
                } else {
                    orders.forEach { order ->
                        InvoiceHistoryRow(
                            order = order,
                            dateString = dateFormatter.format(Date(order.timestamp)),
                            onViewReceipt = {
                                selectedOrderForReceipt = order
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Modal Receipt Dialog for Selected Past Orders
    selectedOrderForReceipt?.let { order ->
        InvoiceDetailedReceiptDialog(
            order = order,
            dateString = dateFormatter.format(Date(order.timestamp)),
            onDismiss = { selectedOrderForReceipt = null }
        )
    }

    // Modal Receipt Dialog for Dynamically Compiled Custom Bill
    if (showCustomGeneratedReceipt) {
        newlyCreatedCustomOrder?.let { customOrder ->
            InvoiceDetailedReceiptDialog(
                order = customOrder,
                dateString = dateFormatter.format(Date(customOrder.timestamp)),
                onDismiss = {
                    showCustomGeneratedReceipt = false
                },
                isCustomBill = true,
                onSaveToArchive = {
                    // Inject custom order into general repository history
                    viewModel.injectCustomBillToHistory(customOrder)
                    Toast.makeText(context, "Custom Bill successfully archived to database records!", Toast.LENGTH_SHORT).show()
                    customBillItems.clear()
                    showCustomGeneratedReceipt = false
                    showBillGenerator = false
                }
            )
        }
    }
}



@Composable
fun InvoiceHistoryRow(
    order: PtOrder,
    dateString: String,
    onViewReceipt: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFFF1E27).copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (order.orderType == "Home Delivery") Icons.Default.LocalShipping else Icons.Default.ReceiptLong,
                    contentDescription = null,
                    tint = Color(0xFFFF1E27),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "INV-PT-2026-${1000 + order.id}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = dateString,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${order.orderType} • ${order.status}",
                    color = if (order.status.contains("Complete", true)) Color(0xFF10B981) else Color(0xFFFFD700),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${order.totalCost}",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onViewReceipt,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26262B)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(24.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("BILL 🧾", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MetricMiniCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = title, color = Color.Gray, fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Black)
        }
    }
}

// Full page thermal-styled paper invoice billing Dialog sheet
@Composable
fun InvoiceDetailedReceiptDialog(
    order: PtOrder,
    dateString: String,
    onDismiss: () -> Unit,
    isCustomBill: Boolean = false,
    onSaveToArchive: (() -> Unit)? = null
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .border(2.dp, Color(0xFFFF1E27), RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White) // Thermal receipt paper is white!
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Thermal receipt header
                Text(
                    text = "••• OFFICIAL RECEIPT •••",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Text(
                    text = "PROTEIN THEORY",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp
                )
                Text(
                    text = "— FUEL YOUR THEORY —",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp
                )
                Text(
                    text = "Shop 4, S-33, Mahal Road, Jagatpura, Jaipur",
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp
                )
                Text(
                    text = "Ph: +91 80586 15380 | Lic: 12224026000456",
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Receipt general info block
                Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("INVOICE NO:", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("PT-INV-2026-${1380 + order.id}", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("DATE TIME:", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(dateString, color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("TYPE / FULFIL:", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(order.orderType.uppercase(), color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("PAYMENT METHOD:", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("UPI (PAID SECURE)", color = Color(0xFF10B981), fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }

                Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

                // Itemized table header
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("QTY/ITEM DESCRIPTION", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("AMOUNT", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Divider(color = Color.LightGray, thickness = 0.5.dp)

                // Item lines
                val itemLines = order.serializedItems.split("\n")
                var calculatedSubtotal = 0f
                itemLines.forEach { line ->
                    if (line.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = line,
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                modifier = Modifier.weight(1f)
                            )
                            // Estimate prices from line total
                            val qtyValue = line.firstOrNull { it.isDigit() }?.toString()?.toIntOrNull() ?: 1
                            val estimatedPriceFactor = if (line.contains("Chicken", true)) 170 else if (line.contains("Shake", true)) 179 else if (line.contains("Wrap", true)) 169 else 80
                            val costForThisLine = estimatedPriceFactor * qtyValue
                            calculatedSubtotal += costForThisLine
                            
                            Text(
                                text = "₹$costForThisLine",
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp
                            )
                        }
                    }
                }

                Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

                // Calculations: tax invoice details
                val rawSubtotal = if (calculatedSubtotal > 0) calculatedSubtotal else order.totalCost * 0.90f
                val sgst = rawSubtotal * 0.025f // 2.5% SGST
                val cgst = rawSubtotal * 0.025f // 2.5% CGST
                val totalBillAmount = order.totalCost

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("SUBTOTAL :", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                    Text("₹${String.format("%.2f", rawSubtotal)}", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("SGST (2.5%) :", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                    Text("₹${String.format("%.2f", sgst)}", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("CGST (2.5%) :", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                    Text("₹${String.format("%.2f", cgst)}", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("NET PAYABLE ROUNDED :", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("₹$totalBillAmount", color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

                // 🧬 METABOLIC MACRO REPORT (Protein Theory Exclusive detail!)
                Text(
                    text = "--- COMPLIANCE MACRO VERIFICATION ---",
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MacroReceiptTag(label = "KCAL", value = "${order.totalCalories}")
                    MacroReceiptTag(label = "PROT", value = "${order.totalProtein}g")
                    MacroReceiptTag(label = "CARB", value = "${order.totalCarbs}g")
                    MacroReceiptTag(label = "FATS", value = "${order.totalFats}g")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Simulated barcodes/QR code for digital verification and validation
                Text(
                    text = "||||| | |||| |||| || |||| ||||||",
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "THANK YOU FOR FUELING YOUR THEORY!",
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Actions below invoice sheet
                if (isCustomBill && onSaveToArchive != null) {
                    Button(
                        onClick = onSaveToArchive,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SAVE INVOICE TO ARCHIVE HISTORY", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                OutlinedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("CLOSE RECEIPT SHEET", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MacroReceiptTag(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = Color.Gray, fontFamily = FontFamily.Monospace, fontSize = 7.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = Color.Black, fontFamily = FontFamily.Monospace, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}
