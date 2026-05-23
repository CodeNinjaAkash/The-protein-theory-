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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WatchLater
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
import androidx.compose.ui.platform.LocalUriHandler
import com.example.ui.theme.*
import com.example.viewmodel.PtViewModel

@Composable
fun ContactScreen(
    viewModel: PtViewModel,
    modifier: Modifier = Modifier
) {
    val feedbacks by viewModel.feedbackList.collectAsState()

    var feedbackName by remember { mutableStateOf("") }
    var feedbackComment by remember { mutableStateOf("") }
    var selectedRating by remember { mutableStateOf(5) } // Default 5 star

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
                    text = "COMMUNICATION & LOGISTICS HQ",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        color = RedBrand,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Contact & Feedbacks",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = TextLight,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // CORE CAFÉ LOCATION DETAILS CARD
            item {
                val uriHandler = LocalUriHandler.current
                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, SteelGray),
                    colors = CardDefaults.cardColors(containerColor = CarbonSlate),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        LocalDetailInfoRow(
                            icon = Icons.Default.LocationOn,
                            title = "Jaipur Headquarters, India",
                            description = "Protein Theory Cafe, 1st Floor, Shop 4, S-33, Near Capital High Street, Mahal Road, Jagatpura, Jaipur, 302017",
                            modifier = Modifier.clickable {
                                uriHandler.openUri("https://maps.google.com/?q=Protein+Theory+Cafe+Jagatpura+Jaipur")
                            }
                        )
                        LocalDetailInfoRow(
                            icon = Icons.Default.Call,
                            title = "Instant Phone Dispatch Support",
                            description = "+91 80586 15380 (Co-Founder hotline)",
                            modifier = Modifier.clickable {
                                uriHandler.openUri("tel:+918058615380")
                            }
                        )
                        LocalDetailInfoRow(
                            icon = Icons.Default.WatchLater,
                            title = "High Velocity Kitchen Hours",
                            description = "Daily Open: 06:00 AM - 11:30 PM (Aligned with high-intensity lifts)"
                        )

                        // Cafe Instagram Link Callout
                        Button(
                            onClick = {
                                uriHandler.openUri("https://www.instagram.com/proteintheoryjaipur?igsh=bmZkOXplMzNpNDM5")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SteelGray),
                            border = BorderStroke(1.dp, RedBrand),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("📷 VISIT CAFE INSTAGRAM", color = TextLight, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }

            // EMBEDDED MAP GRAPHIC (Fidelity Canvas Jaipur Malviya Nagar Blueprint)
            item {
                Text(
                    text = "HQ ANCHOR PLOTTER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.5.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, SteelGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Canvas(modifier = Modifier.fillMaxSize().background(SteelGray)) {
                            val w = size.width
                            val h = size.height

                            // Draw Jaipur streets
                            val streetColor = CarbonSlate
                            val devStroke = Stroke(width = 16f)

                            val streetPath1 = Path().apply {
                                moveTo(0f, h * 0.3f)
                                lineTo(w, h * 0.3f)
                            }
                            val streetPath2 = Path().apply {
                                moveTo(w * 0.4f, 0f)
                                lineTo(w * 0.4f, h)
                            }
                            val streetPath3 = Path().apply {
                                moveTo(0f, h * 0.7f)
                                lineTo(w, h * 0.7f)
                            }

                            drawPath(streetPath1, streetColor, style = devStroke)
                            drawPath(streetPath2, streetColor, style = devStroke)
                            drawPath(streetPath3, streetColor, style = devStroke)

                            // Draw central Jaipur Jagatpura circle intersection
                            drawCircle(streetColor, radius = 45f, center = Offset(w * 0.4f, h * 0.3f))
                            drawCircle(MatteBlack, radius = 35f, center = Offset(w * 0.4f, h * 0.3f))

                            // Highlight Cafe Position (Shop 4, S-33)
                            val cafePos = Offset(w * 0.65f, h * 0.3f)
                            drawCircle(RedBrand.copy(alpha = 0.3f), radius = 28f, center = cafePos)
                            drawCircle(RedBrand, radius = 10f, center = cafePos)
                            drawCircle(TextLight, radius = 5f, center = cafePos)

                            // Highlight Companion Capital High Street Landmark Location
                            val gymPos = Offset(w * 0.4f, h * 0.7f)
                            drawCircle(GlowingAmber.copy(alpha = 0.3f), radius = 22f, center = gymPos)
                            drawCircle(GlowingAmber, radius = 8f, center = gymPos)
                        }

                        // Map marker descriptions
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .background(RedBrand, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("📍 PROTEIN THEORY CAFE", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TextLight)
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                                .background(GlowingAmber, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("🏢 CAPITAL HIGH STREET", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = MatteBlack)
                        }
                    }
                }
            }

            // FSSAI CERTIFICATION STRIP
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GlowingAmber.copy(alpha = 0.6f)),
                    colors = CardDefaults.cardColors(containerColor = CarbonSlate),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        // Logo Plate
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .background(GlowingAmber.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                                .border(1.dp, GlowingAmber, RoundedCornerShape(8.dp))
                        ) {
                            Text("fssai", color = GlowingAmber, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "FSSAI FOOD ACCREDITED SAFE",
                                fontSize = 11.sp,
                                color = TextLight,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Licence No: 12224026000456  [100% Certified]",
                                fontSize = 10.sp,
                                color = GlowingAmber,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Full compliance audited meticulously for high-protein raw sources, sterile kitchen preparation, and zero preservative contamination.",
                                fontSize = 9.sp,
                                color = TextMuted,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }
            }

            // FEEDBACK INPUT SECTION
            item {
                Text(
                    text = "FEEDBACK AUDIT CHAMBER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.5.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, SteelGray),
                    colors = CardDefaults.cardColors(containerColor = CarbonSlate),
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("SUBMIT PERFORMANCE REVIEW:", fontSize = 11.sp, color = GlowingAmber, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Star selection as dumbbells
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Dumbbelly Rating:", fontSize = 11.sp, color = TextMuted)
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                for (star in 1..5) {
                                    Icon(
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = "Rating dumbbell",
                                        tint = if (star <= selectedRating) RedBrand else TextMuted,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable { selectedRating = star }
                                    )
                                }
                            }
                            Text("($selectedRating / 5)", fontSize = 12.sp, color = RedBrand, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = feedbackName,
                            onValueChange = { feedbackName = it },
                            placeholder = { Text("Your Gym Name / Nickname", color = TextMuted, fontSize = 12.sp) },
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
                                .testTag("feedback_name_field")
                        )

                        OutlinedTextField(
                            value = feedbackComment,
                            onValueChange = { feedbackComment = it },
                            placeholder = { Text("Write your feedback (e.g. food quality, macros setup, weight goals alignment)...", color = TextMuted, fontSize = 12.sp) },
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
                                .height(80.dp)
                                .testTag("feedback_comment_field")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val hasComment = feedbackComment.trim().isNotEmpty()
                        Button(
                            onClick = {
                                val ratingsSummary = if (feedbackName.trim().isEmpty()) "Alpha Lifter" else feedbackName.trim()
                                viewModel.submitFeedback(
                                    foodRating = selectedRating.toFloat(),
                                    serviceRating = 5.0f,
                                    comments = "[Lifter: $ratingsSummary] $feedbackComment"
                                )
                                feedbackName = ""
                                feedbackComment = ""
                                selectedRating = 5
                            },
                            enabled = hasComment,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RedBrand,
                                disabledContainerColor = SteelGray
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("feedback_submit_button")
                        ) {
                            Text("SUBMIT PERFORMANCE REVIEW", color = if (hasComment) TextLight else TextMuted, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }

            // STACK OF REVIEWS SUBMITTED LIVE
            item {
                Text(
                    text = "CUSTOMER AUDITS BOARD",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.5.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(feedbacks, key = { it.id }) { review ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(0.5.dp, SteelGray),
                    colors = CardDefaults.cardColors(containerColor = CarbonSlate),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "PT Feedback #${review.id + 101}",
                                color = TextLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text("Rating: ${review.foodRating.toInt()}/5", fontSize = 11.sp, color = GlowingAmber, fontWeight = FontWeight.Black)
                        }

                        // Star dumbbells visual
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            for (dumbbell in 1..5) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = "Dumbbell rating mark",
                                    tint = if (dumbbell <= review.foodRating) RedBrand else SteelGray,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        // Message body
                        Text(
                            text = review.comments,
                            color = TextMuted,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocalDetailInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RedBrand,
            modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, color = TextLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(description, color = TextMuted, fontSize = 11.sp, lineHeight = 16.sp)
        }
    }
}
