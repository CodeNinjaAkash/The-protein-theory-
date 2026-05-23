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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Feedback
import com.example.data.LoyaltyProfile
import com.example.viewmodel.PtViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(viewModel: PtViewModel) {
    val context = LocalContext.current
    val loyaltyData by viewModel.loyaltyProfile.collectAsState()
    val rawFeedbackList by viewModel.feedbackList.collectAsState()

    var foodRating by remember { mutableFloatStateOf(4f) }
    var serviceRating by remember { mutableFloatStateOf(5f) }
    var feedbackText by remember { mutableStateOf("") }

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
                .padding(bottom = 80.dp) // padding above navigation
        ) {
            // Loyalty Account Section Title
            Text(
                text = "GYM RAT LOYALTY CARD",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = Color(0xFFFF1E27),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
            )

            // Dynamic Membership Card
            val profile = loyaltyData ?: LoyaltyProfile()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .border(1.dp, Color(0xFFFF1E27).copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFC40008).copy(alpha = 0.25f), Color(0xFF161619)),
                                start = Offset(0f, 0f),
                                end = Offset.Infinite
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp)
                    ) {
                        // Card Top Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = Color(0xFFFF1E27),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "PROTEIN THEORY JAIPUR",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp,
                                    letterSpacing = 2.sp,
                                    color = Color.White
                                )
                            }
                            // Level Pills badge
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFD700).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = profile.levelName.uppercase(),
                                    color = Color(0xFFFFD700),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Points count label details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "CURRENT REWARD POINTS", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${profile.points} PTS",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(text = "Member Name: ${profile.name}", color = Color.LightGray, fontSize = 11.sp)
                                Text(text = "ID: ${profile.memberId} | Visited ${profile.visitsCount} times", color = Color.Gray, fontSize = 9.sp)
                            }

                            // Raw Barcode Canvas generator for scans!
                            Column(horizontalAlignment = Alignment.End) {
                                Canvas(
                                    modifier = Modifier
                                        .width(90.dp)
                                        .height(45.dp)
                                        .background(Color.White, RoundedCornerShape(4.dp))
                                        .border(2.dp, Color.White, RoundedCornerShape(4.dp))
                                ) {
                                    val w = size.width
                                    val h = size.height
                                    // Draw replica physical scanner codes lines
                                    val barWidths = listOf(3f, 1f, 4f, 2f, 1f, 3f, 1f, 5f, 2f, 3f, 4f, 1f, 2f, 3f, 1f)
                                    var currentX = 4f
                                    var idx = 0
                                    while (currentX < w - 6f) {
                                        val runWidth = barWidths.getOrElse(idx % barWidths.size) { 2f } * 1.5f
                                        val isBar = idx % 2 == 0
                                        if (isBar) {
                                            drawRect(
                                                color = Color.Black,
                                                topLeft = Offset(currentX, 2f),
                                                size = androidx.compose.ui.geometry.Size(runWidth, h - 4f)
                                            )
                                        }
                                        currentX += runWidth + 2f
                                        idx++
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("SCAN REGISTER", color = Color.Gray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Quick tier rewards descriptions
            Text(
                text = "ELIGIBLE REWARDS LIST",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = Color(0xFFFF1E27),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 4.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161619))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    RewardTierRow("100 Points Redemptions", "FREE Premium Whey Protein Scoop (30g)", profile.points >= 100)
                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFF26262B))
                    RewardTierRow("200 Points Redemptions", "FREE Lean Grilled Chicken Bowl (150 gm)", profile.points >= 200)
                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFF26262B))
                    RewardTierRow("350 Points Redemptions", "FREE Platinum Combo Meal of your Choice", profile.points >= 350)
                }
            }

            // Customer feedback title section
            Text(
                text = "HYPER-SECURE FEEDBACK PANEL",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = Color(0xFFFF1E27),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 4.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, Color(0xFFFF1E27).copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161619))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.RateReview, contentDescription = null, tint = Color(0xFFFF1E27))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "HELP US IMPROVE FOOD QUALITY",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Feed rating slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Food Quality & Taste", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("${foodRating.toInt()} / 5 Stars", color = Color(0xFF10B981), fontSize = 13.sp, fontWeight = FontWeight.Black)
                    }
                    Slider(
                        value = foodRating,
                        onValueChange = { foodRating = it },
                        valueRange = 1f..5f,
                        steps = 3,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFFF1E27),
                            activeTrackColor = Color(0xFFFF1E27),
                            inactiveTrackColor = Color(0xFF26262B)
                        ),
                        modifier = Modifier.testTag("feedback_food_slider")
                    )

                    // Speed service rating slider
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Service Speed & Hygiene", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("${serviceRating.toInt()} / 5 Stars", color = Color(0xFF10B981), fontSize = 13.sp, fontWeight = FontWeight.Black)
                    }
                    Slider(
                        value = serviceRating,
                        onValueChange = { serviceRating = it },
                        valueRange = 1f..5f,
                        steps = 3,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFFF1E27),
                            activeTrackColor = Color(0xFFFF1E27),
                            inactiveTrackColor = Color(0xFF26262B)
                        ),
                        modifier = Modifier.testTag("feedback_service_slider")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Detailed suggestions", color = Color.Gray, fontSize = 11.sp)
                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { feedbackText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(82.dp)
                            .padding(vertical = 6.dp)
                            .testTag("feedback_comments_input"),
                        textStyle = TextStyle(color = Color.White, fontSize = 12.sp),
                        placeholder = { Text("How were the protein shake macros or boiled egg prep? Add complaints/suggestions...", color = Color.DarkGray, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF1E27),
                            unfocusedBorderColor = Color(0xFF26262B)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (feedbackText.trim().isEmpty()) {
                                Toast.makeText(context, "Please write a brief suggestion", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.submitFeedback(
                                    foodRating = foodRating,
                                    serviceRating = serviceRating,
                                    comments = feedbackText
                                )
                                Toast.makeText(context, "Feedback safely received! Muscle points boosted.", Toast.LENGTH_SHORT).show()
                                feedbackText = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("feedback_submit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1E27)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("SUBMIT ANONYMOUS REVIEW", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // Live Feed of Community reports
            if (rawFeedbackList.isNotEmpty()) {
                Text(
                    text = "JAIPUR GYM RATS COMMUNITY REVIEWS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    color = Color(0xFF10B981),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                rawFeedbackList.forEach { fb ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF111714)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.ThumbUp, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Verified Buyer Review", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Text("Food: ${fb.foodRating.toInt()}★ | Svc: ${fb.serviceRating.toInt()}★", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "\"${fb.comments}\"", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RewardTierRow(points: String, prizeName: String, isUnlocked: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = points, color = if (isUnlocked) Color(0xFF10B981) else Color(0xFFFF1E27), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(text = prizeName, color = Color.White, fontSize = 13.sp)
        }
        Box(
            modifier = Modifier
                .background(
                    if (isUnlocked) Color(0xFF10B981).copy(alpha = 0.15f) else Color.White.copy(alpha = 0.05f),
                    RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (isUnlocked) "UNLOCKED" else "LOCKED",
                color = if (isUnlocked) Color(0xFF10B981) else Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
