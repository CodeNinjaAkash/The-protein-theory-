package com.example.ui.screens

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HyperMetric3DEngine
import com.example.ui.components.ProteinTheoryLogo
import com.example.viewmodel.PtViewModel
import kotlinx.coroutines.delay
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: PtViewModel,
    onNavigateToMenu: () -> Unit,
    onNavigateToCoaching: () -> Unit,
    onNavigateToContact: () -> Unit,
    onNavigateToBilling: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val activeOrders by viewModel.activeAndPastOrders.collectAsState()
    val activeTrack = activeOrders.firstOrNull { it.trackingProgress < 1.0f || it.status.contains("Ready", true) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0D))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 80.dp) // extra padding for system navigation bar
        ) {
            // Neon Hero Logo Banner Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFC40008).copy(alpha = 0.35f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Glow Backdrop Lines
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    // Red Glow laser line
                    drawLine(
                        start = Offset(0f, canvasHeight * 0.9f),
                        end = Offset(canvasWidth, canvasHeight * 0.9f),
                        color = Color(0xFFFF1E27).copy(alpha = 0.4f),
                        strokeWidth = 3f
                    )
                }

                // Top-right Invoice dashboard shortcut icon button
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = onNavigateToBilling,
                        modifier = Modifier
                            .background(Color(0xFF161619).copy(alpha = 0.8f), RoundedCornerShape(50))
                            .border(1.dp, Color(0xFFFF1E27).copy(alpha = 0.6f), RoundedCornerShape(50))
                            .testTag("home_billing_dashboard_shortcut")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = "Billing Invoice Dashboard",
                            tint = Color(0xFFFF1E27)
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    ProteinTheoryLogo(scale = 1.1f)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Targeting Gym Rats • Jaipur's Premium Food Lab",
                        fontSize = 11.sp,
                        color = Color(0xFF9E9EA4),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Interactive Order Progress Tracker Alert Box at Top
            AnimatedVisibility(
                visible = activeTrack != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                activeTrack?.let { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .border(1.dp, Color(0xFFFF1E27), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF161619))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DirectionsRun,
                                        contentDescription = "Active Track",
                                        tint = Color(0xFFFF1E27),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "LIVE ORDER TRACKING",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White,
                                        letterSpacing = 1.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = order.status,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFFF1E27)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { order.trackingProgress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = Color(0xFFFF1E27),
                                    trackColor = Color(0xFF26262B)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${(order.trackingProgress * 100).toInt()}%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Quick Interactive Search Engine
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    viewModel.updateSearchQuery(it)
                    if (it.isNotEmpty()) {
                        onNavigateToMenu()
                    }
                },
                placeholder = { Text("Search healthy dishes / proteins...", color = Color.Gray, fontSize = 14.sp) },
                prefix = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFFFF1E27),
                        modifier = Modifier.padding(end = 6.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("menu_search_bar"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF1E27),
                    unfocusedBorderColor = Color(0xFF26262B),
                    focusedContainerColor = Color(0xFF161619),
                    unfocusedContainerColor = Color(0xFF161619),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Dynamic Promotion Cards Row "Bulk & Shred Options"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Combo 1 Promo Card
                Card(
                    modifier = Modifier
                        .width(260.dp)
                        .height(130.dp)
                        .clickable {
                            viewModel.selectCategory("Combo Deals")
                            onNavigateToMenu()
                        },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFF1E27).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("POPULAR BULK", color = Color(0xFFFF1E27), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Chicken + Shake Combo", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("50g High Protein • ₹299 onwards", fontSize = 12.sp, color = Color(0xFF9E9EA4))
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Personalized Diet Advisor Banner
                Card(
                    modifier = Modifier
                        .width(260.dp)
                        .height(130.dp)
                        .clickable { onNavigateToCoaching() },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF10B981).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("COACH BRANDING", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Consult Certified Expert", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Book specialized muscle shred schedule", fontSize = 12.sp, color = Color(0xFF9E9EA4))
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Customer Invoices & Billing Dashboard Promo Card
                Card(
                    modifier = Modifier
                        .width(260.dp)
                        .height(130.dp)
                        .clickable { onNavigateToBilling() }
                        .testTag("home_billing_dashboard_promo"),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFD700).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("MY TAX BILLS", color = Color(0xFFFFD700), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Billing & Receipts", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Compile, archive, and view custom GST bills!", fontSize = 12.sp, color = Color(0xFF9E9EA4))
                        }
                    }
                }
            }

            // Interactive 3D Macro Engine Hologram
            HyperMetric3DEngine()

            // Cafe Core Contacts & Fast Details Title
            Text(
                text = "CAFE INTEL & LOCATION",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = Color(0xFFFF1E27),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            // Physical Details card (Phone, Hours, Address)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onNavigateToContact() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Contact Item 1: Phone
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = "Call", tint = Color(0xFFFF1E27), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Secure Dial Contact Number", color = Color.Gray, fontSize = 11.sp)
                            Text("+91 80586 15380", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contact Item 2: Working hours
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Hours", tint = Color(0xFFFF1E27), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Operating hours (Gym Rat Friendly)", color = Color.Gray, fontSize = 11.sp)
                            Text("06:00 AM - 11:30 PM (Daily prep)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contact Item 3: Address
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Address", tint = Color(0xFFFF1E27), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Jaipur Physical Cafe Store Landmark", color = Color.Gray, fontSize = 11.sp)
                            Text("1st Floor, Shop 4, S-33, Near Capital High Street, Mahal Road, Jagatpura, Jaipur, 302017", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Native Mock Map Radar Grid Box (Jaipur Streets Canvas)
            Text(
                text = "NEIGHBORHOOD GPS RADAR (EMBEDDED)",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = Color(0xFFFF1E27),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161619))
            ) {
                var pulseFactor by remember { mutableFloatStateOf(0f) }
                LaunchedEffect(Unit) {
                    var t = 0f
                    while (true) {
                        t += 0.05f
                        pulseFactor = sin(t) * 0.5f + 0.5f
                        delay(50)
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Draw Grid lines (Radar scope design)
                        for (i in 1..8) {
                            val x = w * i / 8f
                            drawLine(Color(0xFF26262B), Offset(x, 0f), Offset(x, h), 1.5f)
                        }
                        for (i in 1..6) {
                            val y = h * i / 6f
                            drawLine(Color(0xFF26262B), Offset(0f, y), Offset(w, y), 1.5f)
                        }

                        // Drawn Streets/Bypasses (Jagatpura Mahal Road representation)
                        drawLine(Color(0xFF32323A).copy(alpha = 0.8f), Offset(0f, h * 0.4f), Offset(w, h * 0.4f), 12f) // Main Flyover Road
                        drawLine(Color(0xFF32323A).copy(alpha = 0.8f), Offset(w * 0.35f, 0f), Offset(w * 0.35f, h), 10f) // Capital High Street Lane
                        drawLine(Color(0xFF32323A).copy(alpha = 0.8f), Offset(w * 0.7f, 0f), Offset(w * 0.7f, h), 8f) // GT Mall Crossroad

                        // Target cafe center spot
                        val cafeX = w * 0.35f
                        val cafeY = h * 0.4f

                        // Blinking/expanding tracking pulse outer circle
                        drawCircle(
                            color = Color(0xFFFF1E27).copy(alpha = 0.15f * (1f - pulseFactor)),
                            radius = 45f * pulseFactor + 10f,
                            center = Offset(cafeX, cafeY)
                        )

                        // Outer ring indicator
                        drawCircle(
                            color = Color(0xFFFF1E27).copy(alpha = 0.4f),
                            radius = 20f,
                            center = Offset(cafeX, cafeY)
                        )

                        // Core Blinking LED Center
                        drawCircle(
                            color = Color(0xFFFF1E27),
                            radius = 8f,
                            center = Offset(cafeX, cafeY)
                        )
                    }

                    // Map Text Tags
                    Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                        Text(
                            text = "Mahal Road Jagatpura",
                            color = Color.Gray,
                            fontSize = 9.sp,
                            modifier = Modifier.align(Alignment.TopStart)
                        )
                        Text(
                            text = "Capital High Street Lane",
                            color = Color.Gray,
                            fontSize = 9.sp,
                            modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 24.dp)
                        )
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF1E27)),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(x = (-12).dp, y = (-26).dp)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("PROTEIN THEORY", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Verified Gold FSSAI License Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f), RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131109))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFD700).copy(alpha = 0.15f), RoundedCornerShape(50))
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = "FSSAI Food license logo",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "FSSAI GOVT REVENUE APPROVED",
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "FSSAI Lic. Reg: 12224026000456",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Dietary health compliance & pure ingredients verified",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
