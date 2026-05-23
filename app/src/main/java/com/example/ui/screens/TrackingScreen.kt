package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.PtViewModel

@Composable
fun TrackingScreen(
    viewModel: PtViewModel,
    modifier: Modifier = Modifier
) {
    val activeOrders by viewModel.activeAndPastOrders.collectAsState()

    Box(modifier = modifier.fillMaxSize().background(MatteBlack)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Titles
            item {
                Text(
                    text = "GPS DISPATCH CORRIDOR",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        color = RedBrand,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Live Order Tracking",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = TextLight,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            if (activeOrders.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, SteelGray),
                        colors = CardDefaults.cardColors(containerColor = CarbonSlate),
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp).fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Route,
                                contentDescription = "No routes",
                                tint = TextMuted,
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "No Active Delivery Routes Plotted",
                                color = TextLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "To view GPS order routes, dispatch a meal order from your workout cart. We'll plot progress updates straight to your Jaipurean sector address.",
                                color = TextMuted,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                item {
                    Text(
                        text = "ACTIVE MEALS ROUTING PIPELINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                items(activeOrders, key = { it.id }) { order ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, RedBrand.copy(alpha = 0.5f)),
                        colors = CardDefaults.cardColors(containerColor = CarbonSlate),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Header row ID
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ReceiptLong,
                                        contentDescription = "Order Receipt ID",
                                        tint = RedBrand,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "ORDER PT-#${order.id + 10450}",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp,
                                        color = TextLight
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .background(RedBrand.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = order.status.uppercase(),
                                        color = RedBrand,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Items: ${order.serializedItems}", color = TextMuted, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(12.dp))

                            // TRACKER DISPATCH MAP CANVAS
                            Text("LIVE GPS CO-ORDINATE STREAMER", fontSize = 9.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, SteelGray),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Canvas(modifier = Modifier.fillMaxSize().background(MatteBlack)) {
                                        val w = size.width
                                        val h = size.height

                                        // Draw shipping lanes road path from source (Protein Theory, right) to client node (left)
                                        val roadPath = Path().apply {
                                            moveTo(w * 0.9f, h * 0.2f) // Start (Protein Theory source)
                                            quadraticTo(w * 0.5f, h * 0.1f, w * 0.5f, h * 0.5f) // Road curve
                                            quadraticTo(w * 0.5f, h * 0.9f, w * 0.1f, h * 0.8f) // Destination client address node
                                        }

                                        // Draw background road lane outline
                                        drawPath(
                                            path = roadPath,
                                            color = SteelGray,
                                            style = Stroke(width = 12f)
                                        )

                                        // Draw active completed track path based on order progress float
                                        drawPath(
                                            path = roadPath,
                                            color = RedBrand,
                                            style = Stroke(width = 4f)
                                        )

                                        // Calculate exact rider coordinates interpolating path curves
                                        val progress = order.trackingProgress
                                        // Simple interpolation for Bezier curves
                                        val ptY = h * 0.2f * (1f - progress) * (1f - progress) + 2f * (h * 0.1f) * progress * (1f - progress) + h * 0.5f * progress * progress
                                        val ptX = w * 0.9f * (1f - progress) * (1f - progress) + 2f * (w * 0.5f) * progress * (1f - progress) + w * 0.5f * progress * progress

                                        val finalX = if (progress < 0.5f) {
                                            val t = progress * 2f
                                            w * 0.9f * (1f - t) + w * 0.5f * t
                                        } else {
                                            val t = (progress - 0.5f) * 2f
                                            w * 0.5f * (1f - t) + w * 0.1f * t
                                        }

                                        val finalY = if (progress < 0.5f) {
                                            val t = progress * 2f
                                            h * 0.2f * (1f - t) + h * 0.5f * t
                                        } else {
                                            val t = (progress - 0.5f) * 2f
                                            h * 0.5f * (1f - t) + h * 0.8f * t
                                        }

                                        // Draw Point A (Protein Theory Outlet)
                                        drawCircle(MacroProteinGreen, radius = 10f, center = Offset(w * 0.9f, h * 0.2f))

                                        // Draw Point B (Client Gym Address Destination)
                                        drawCircle(GlowingAmber, radius = 10f, center = Offset(w * 0.1f, h * 0.8f))

                                        // Draw active Rider coordinates
                                        if (progress < 1.0f) {
                                            drawCircle(RedBrand.copy(alpha = 0.4f), radius = 22f, center = Offset(finalX, finalY))
                                            drawCircle(RedBrand, radius = 7f, center = Offset(finalX, finalY))
                                            drawCircle(TextLight, radius = 3f, center = Offset(finalX, finalY))
                                        }
                                    }

                                    // Graphic overlays
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                            .background(MacroProteinGreen.copy(alpha = 0.85f), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("⚡ PT DISPATCH LAB", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = TextLight)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(8.dp)
                                            .background(GlowingAmber.copy(alpha = 0.85f), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("🏋️ CLIENT DESTINATION", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = MatteBlack)
                                    }

                                    if (order.trackingProgress < 1.0f) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .align(Alignment.TopStart)
                                                .padding(8.dp)
                                                .background(RedBrand, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.LocalFireDepartment, contentDescription = null, tint = TextLight, modifier = Modifier.size(10.dp))
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text("RIDER SPEED: IN TRANSIT", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = TextLight)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Progress percentage indicator
                            val percentage = (order.trackingProgress * 100).toInt()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Rider Stage Segment Progress: $percentage%",
                                    fontSize = 11.sp,
                                    color = TextLight,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "₹${order.totalCost} • ${order.orderType}",
                                    fontSize = 11.sp,
                                    color = RedBrand,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            LinearProgressIndicator(
                                progress = order.trackingProgress,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .height(6.dp),
                                color = RedBrand,
                                trackColor = SteelGray
                            )

                            // Quick progress simulated update block (aligned with lifters speed)
                            if (order.trackingProgress < 1.0f) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "⚡ Real-time coordinates update simulates automatically via database increments.",
                                    fontSize = 9.sp,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Medium
                                )
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MacroProteinGreen, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Your muscle fuel has been successfully prepared, packaged, and verified. Bon appétit!",
                                        fontSize = 10.sp,
                                        color = MacroProteinGreen,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
