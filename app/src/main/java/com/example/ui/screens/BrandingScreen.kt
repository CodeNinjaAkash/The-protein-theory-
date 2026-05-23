package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.PtViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandingScreen(viewModel: PtViewModel) {
    val context = LocalContext.current
    val bookingsList by viewModel.bookingsList.collectAsState()

    // Form selection inputs
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableStateOf("Fat Loss Transformation") }
    var selectedSlotTime by remember { mutableStateOf("07:00 AM - 08:30 AM") }
    var clientNotes by remember { mutableStateOf("") }

    val goals = listOf("Extreme Fat Loss", "Hypertrophy Power Build", "General Cardio Endurance", "Contest Prep Coaching")
    val slots = listOf("06:00 AM - 07:30 AM (Early Shred)", "07:30 AM - 09:00 AM (Active Bulk)", "05:00 PM - 06:30 PM (Power Hour)", "07:30 PM - 09:00 PM (Night Warrior)")

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
                .padding(bottom = 80.dp) // pad above navigation tabs
        ) {
            // Profile banner section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFC40008).copy(alpha = 0.2f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFF161619))
                            .border(2.dp, Color(0xFFFF1E27), RoundedCornerShape(50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sports,
                            contentDescription = "Coach Avatar",
                            tint = Color(0xFFFF1E27),
                            modifier = Modifier.size(45.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "COACH AKASH SUMAN", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "FOUNDER & NSCA-CSCS ELITE TRAINER • JAIPUR", fontSize = 11.sp, color = Color(0xFFFF1E27), fontWeight = FontWeight.Bold)
                }
            }

            // Founder bio card description
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161619))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "THE MIND BEHIND PROTEIN THEORY",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFFFF1E27),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Akash Suman is a highly certified Sports Nutritionist and Strength Coach with 9+ years of coaching at Jaipur's gold-tier gym networks. Having trained over 500+ athletes, he founded Protein Theory Jaipur after observing gym-goers struggle to find precise macro-portioned meals with authentic raw ingredients. Every recipe on our menu is clinically designed and macro-approved by Coach Akash to fit your exact shred or bulk phase perfectly.",
                        color = Color.White,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            // Social Redirection link icons
            Text(
                text = "SOCIAL ATHLETE CHANNELS",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = Color(0xFFFF1E27),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SocialIconCard(
                    title = "Instagram feed",
                    handle = "@proteintheoryjaipur",
                    color = Color(0xFFE1306C),
                    icon = Icons.Default.CameraAlt,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/proteintheoryjaipur"))
                        context.startActivity(intent)
                    }
                )
                SocialIconCard(
                    title = "Coach Facebook",
                    handle = "Akash Suman Coaching",
                    color = Color(0xFF1877F2),
                    icon = Icons.Default.Groups,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com"))
                        context.startActivity(intent)
                    }
                )
            }

            // Certified slider metrics
            Text(
                text = "ACCREDITED FITNESS COACH CERTIFICATES",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = Color(0xFFFF1E27),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                CertificateBadge("NSCA - CSCS", "Strength & Conditioning Specialist #104205", Color(0xFFFFD700))
                Spacer(modifier = Modifier.width(10.dp))
                CertificateBadge("ISSA Elite", "Certified Sports Nutritionist Diploma", Color(0xFF1E90FF))
                Spacer(modifier = Modifier.width(10.dp))
                CertificateBadge("FSSAI Registered", "Hygienic Caterer License Certified", Color(0xFF32CD32))
            }

            // Fast interactive Booking scheduler Panel widget
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, Color(0xFFFF1E27).copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161619))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = Color(0xFFFF1E27))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "BOOK PERSONAL TRANSFORMATION CONSULT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Client Name", color = Color.Gray, fontSize = 11.sp)
                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("booking_name_input"),
                        textStyle = TextStyle(color = Color.White, fontSize = 13.sp),
                        placeholder = { Text("e.g. Robin Sharma", color = Color.DarkGray, fontSize = 13.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF1E27),
                            unfocusedBorderColor = Color(0xFF26262B)
                        ),
                        singleLine = true
                    )

                    Text("Mobile Contact phone", color = Color.Gray, fontSize = 11.sp)
                    OutlinedTextField(
                        value = clientPhone,
                        onValueChange = { clientPhone = it },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag("booking_phone_input"),
                        textStyle = TextStyle(color = Color.White, fontSize = 13.sp),
                        placeholder = { Text("e.g. +91 98290 88771", color = Color.DarkGray, fontSize = 13.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF1E27),
                            unfocusedBorderColor = Color(0xFF26262B)
                        ),
                        singleLine = true
                    )

                    Text("Fitness Transformation Goal", color = Color.Gray, fontSize = 11.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 6.dp)
                    ) {
                        goals.forEach { goal ->
                            val isSel = selectedGoal == goal
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) Color(0xFFFF1E27).copy(alpha = 0.2f) else Color(0xFF0C0C0D))
                                    .border(1.dp, if (isSel) Color(0xFFFF1E27) else Color.Transparent, RoundedCornerShape(6.dp))
                                    .clickable { selectedGoal = goal }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(goal, color = if (isSel) Color.White else Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Text("Preferred Training Slot duration", color = Color.Gray, fontSize = 11.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 6.dp)
                    ) {
                        slots.forEach { slot ->
                            val isSel = selectedSlotTime == slot
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) Color(0xFFFF1E27).copy(alpha = 0.2f) else Color(0xFF0C0C0D))
                                    .border(1.dp, if (isSel) Color(0xFFFF1E27) else Color.Transparent, RoundedCornerShape(6.dp))
                                    .clickable { selectedSlotTime = slot }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(slot.substringBefore(" ("), color = if (isSel) Color.White else Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Text("Special health notes (Injuries/experience)", color = Color.Gray, fontSize = 11.sp)
                    OutlinedTextField(
                        value = clientNotes,
                        onValueChange = { clientNotes = it },
                        modifier = Modifier.fillMaxWidth().height(80.dp).padding(vertical = 4.dp),
                        textStyle = TextStyle(color = Color.White, fontSize = 12.sp),
                        placeholder = { Text("Describe prior workouts or current weight stats...", color = Color.DarkGray, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF1E27),
                            unfocusedBorderColor = Color(0xFF26262B)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (clientName.isEmpty() || clientPhone.isEmpty()) {
                                Toast.makeText(context, "Please write Name & Dial Phone", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.submitBooking(
                                    clientName = clientName,
                                    clientPhone = clientPhone,
                                    slotTime = selectedSlotTime,
                                    coachingType = selectedGoal,
                                    date = "2026-05-24", // Mock tomorrow
                                    notes = clientNotes
                                )
                                Toast.makeText(context, "Consult Reservation Confirmed! Coach Dev will call soon.", Toast.LENGTH_LONG).show()
                                clientName = ""
                                clientPhone = ""
                                clientNotes = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("booking_submit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1E27)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("SECURELY REGISTER SCHEDULER", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    }
                }
            }

            // Confirmed consultations display (Verified clients)
            if (bookingsList.isNotEmpty()) {
                Text(
                    text = "YOUR TRANSFORMATION SCHEDULE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    color = Color(0xFF10B981),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                bookingsList.forEach { booking ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1512)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "${booking.coachingType} (${booking.chosenSlotTime})", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(text = "Client: ${booking.clientName} | Status: Confiirmed", color = Color.Gray, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SocialIconCard(title: String, handle: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(76.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(50))
                    .padding(8.dp)
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(title, color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(handle, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            }
        }
    }
}

@Composable
fun CertificateBadge(name: String, number: String, goldColor: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
        modifier = Modifier
            .width(200.dp)
            .border(1.dp, goldColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = name, color = goldColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = number, color = Color.White, fontSize = 11.sp, maxLines = 1)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "Accredited Trainer Panel", color = Color.Gray, fontSize = 9.sp)
        }
    }
}
