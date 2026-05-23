package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.PtViewModel

@Composable
fun RewardsScreen(
    viewModel: PtViewModel,
    modifier: Modifier = Modifier
) {
    val loyaltyOpt by viewModel.loyaltyProfile.collectAsState()

    var signupName by remember { mutableStateOf("") }
    var signupPhone by remember { mutableStateOf("") }

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
                    text = "LOYALTY COMPLIANCE MATRIX",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        color = RedBrand,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Loyalty & Fuel Rewards",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = TextLight,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // USER DOES NOT HAVE A MEMBERSHIP PROFILE YET -> SHOW REGISTRATION
            if (loyaltyOpt == null) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, RedBrand.copy(alpha = 0.5f)),
                        colors = CardDefaults.cardColors(containerColor = CarbonSlate),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                "REGISTER NEW ATHLETIC PASS",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = TextLight,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Accumulate 10 points for every clean meal order. Redeem points directly at the jaipur checkout terminal for premium health shakes, isolates, and wraps.",
                                color = TextMuted,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = signupName,
                                onValueChange = { signupName = it },
                                placeholder = { Text("Display Name (E.g. Siddharth)", color = TextMuted, fontSize = 12.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = SteelGray,
                                    unfocusedContainerColor = SteelGray,
                                    focusedBorderColor = RedBrand,
                                    unfocusedBorderColor = SteelGray,
                                    focusedTextColor = TextLight,
                                    unfocusedTextColor = TextLight
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .testTag("loyalty_name_input")
                            )

                            OutlinedTextField(
                                value = signupPhone,
                                onValueChange = { signupPhone = it },
                                placeholder = { Text("Mobile (+91...)", color = TextMuted, fontSize = 12.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = SteelGray,
                                    unfocusedContainerColor = SteelGray,
                                    focusedBorderColor = RedBrand,
                                    unfocusedBorderColor = SteelGray,
                                    focusedTextColor = TextLight,
                                    unfocusedTextColor = TextLight
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                                    .testTag("loyalty_phone_input")
                            )

                            val allowed = signupName.trim().isNotEmpty() && signupPhone.trim().isNotEmpty()
                            Button(
                                onClick = {
                                    viewModel.accruePointsSimulated(
                                        clientName = signupName,
                                        clientPhone = signupPhone,
                                        points = 10 // Starts with 10 free sign-up points
                                    )
                                },
                                enabled = allowed,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = RedBrand,
                                    disabledContainerColor = SteelGray
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().testTag("loyalty_signup_button")
                            ) {
                                Text("ACTIVATE FREE LIFTER PASS (10 PTS FREE)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (allowed) TextLight else TextMuted)
                            }
                        }
                    }
                }
            } else {
                // USER HAS AN ACTIVE REWARDS PASS BOARD
                val profile = loyaltyOpt!!
                
                item {
                    // VIP Card representation (3D tactical finish)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, GlowingAmber),
                        colors = CardDefaults.cardColors(containerColor = CarbonSlate),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Text(
                                        text = "PROTEIN THEORY VIP LIFTER",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 11.sp,
                                        letterSpacing = 1.5.sp,
                                        color = GlowingAmber
                                    )
                                    Text(
                                        text = profile.name.uppercase(),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp,
                                        color = TextLight
                                    )
                                    Text(
                                        text = "ID: ${profile.memberId}",
                                        fontSize = 11.sp,
                                        color = TextMuted,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(GlowingAmber.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CardMembership,
                                        contentDescription = "Membership Pass icon",
                                        tint = GlowingAmber,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Points tally indicator
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Column {
                                    Text("ACCUMULATED POINT SCORE", fontSize = 9.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                    Text("${profile.points} PTS", fontSize = 32.sp, fontWeight = FontWeight.Black, color = RedBrand)
                                }
                                Box(
                                    modifier = Modifier
                                        .background(RedBrand, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = profile.levelName.uppercase(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextLight
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Dynamic canvas barcode sweep
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .background(TextLight, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val w = size.width
                                    val h = size.height
                                    val lineCount = 36
                                    val section = w / lineCount

                                    for (i in 0 until lineCount) {
                                        // barcode lines
                                        val strokeWidth = if (i % 3 == 0) 12f else if (i % 2 == 0) 6f else 2f
                                        if (i % 4 != 0) { // Gap spacing
                                            drawLine(
                                                color = Color.Black,
                                                start = androidx.compose.ui.geometry.Offset(i * section, 0f),
                                                end = androidx.compose.ui.geometry.Offset(i * section, h),
                                                strokeWidth = strokeWidth
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "PT Barcode: ${profile.memberId}",
                                color = TextMuted,
                                fontSize = 8.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // SIMULATOR CONTROL DECK
                item {
                    Text(
                        text = "LOYALTY TERMINAL EMULATOR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.5.sp,
                            color = GlowingAmber,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, SteelGray),
                        colors = CardDefaults.cardColors(containerColor = SteelGray),
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                "Jaipur Cafe Check-In Terminal Simulator",
                                fontSize = 11.sp,
                                color = TextLight,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Simulate a live payment checkout at Jagatpura outlet to add 20 loyalty points instantly and level up your category tier.",
                                fontSize = 9.sp,
                                color = TextMuted
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    viewModel.accruePointsSimulated(
                                        clientName = profile.name,
                                        clientPhone = profile.memberId,
                                        points = 20
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RedBrand),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().testTag("simulate_accrue_points")
                            ) {
                                Text("SIMULATE checkout • +20 POINTS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // REDEEMABLE PHYSICAL GIFTS BOARD
            item {
                Text(
                    text = "REDEEMABLE HIGH-PROTEIN FUELS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.5.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                RedeemRewardPillRow("Free Premium ISO-Whey Shaker (Any Flavor)", 100, "100 Pts Lock")
                Spacer(modifier = Modifier.height(4.dp))
                RedeemRewardPillRow("Double-Stack Grilled Boiled Eggs Wrap", 70, "70 Pts Lock")
                Spacer(modifier = Modifier.height(4.dp))
                RedeemRewardPillRow("BCAA Shred Citrus Refresher Drink", 50, "50 Pts Lock")
                Spacer(modifier = Modifier.height(4.dp))
                RedeemRewardPillRow("Single Whey Protein Cookie Cookie", 30, "30 Pts Lock")
            }
        }
    }
}

@Composable
fun RedeemRewardPillRow(
    title: String,
    pointsRequired: Int,
    badgeLabel: String
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(0.5.dp, SteelGray),
        colors = CardDefaults.cardColors(containerColor = CarbonSlate),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = RedBrand,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, color = TextLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Outlets validation required for physical claim", color = TextMuted, fontSize = 9.sp)
                }
            }
            Box(
                modifier = Modifier
                    .background(RedBrand.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    .border(0.5.dp, RedBrand, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(badgeLabel, color = RedBrand, fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
        }
    }
}
