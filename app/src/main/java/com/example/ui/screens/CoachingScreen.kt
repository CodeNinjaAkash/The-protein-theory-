package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.VerifiedUser
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
import com.example.ui.components.CredentialInteractiveHub
import androidx.compose.ui.platform.LocalUriHandler

data class LocalCoachingService(
    val title: String,
    val cost: Int,
    val durationMin: Int,
    val details: String
)

val CoachesServicesList = listOf(
    LocalCoachingService("1-on-1 Personalized Diet Layout Plan", 1500, 45, "Analyze metabolic rates, clean calorie macro thresholds, and layout exact Jaipur grocery lists."),
    LocalCoachingService("Hypertrophy / Powerlifting Contest Prep", 2500, 60, "Full biomechanical assessment, heavy deadlift, squat, bench form correction, and lifting split."),
    LocalCoachingService("Body Recomposition Blueprint Session", 1200, 35, "Stamina conditioning, carb cycling instructions, and fat-loss management tips.")
)

val CalendarDatesList = listOf("Mon May 25", "Tue May 26", "Wed May 27", "Thu May 28", "Fri May 29")
val BookingTimeSlotsList = listOf("07:00 AM - 08:30 AM", "09:00 AM - 10:30 AM", "11:30 AM - 01:00 PM", "05:00 PM - 06:30 PM", "07:30 PM - 09:00 PM")

@Composable
fun CoachingScreen(
    viewModel: PtViewModel,
    modifier: Modifier = Modifier
) {
    val bookedCoachingSessions by viewModel.bookingsList.collectAsState()

    var selectedService by remember { mutableStateOf(CoachesServicesList[0]) }
    var selectedDate by remember { mutableStateOf(CalendarDatesList[0]) }
    var selectedSlot by remember { mutableStateOf(BookingTimeSlotsList[3]) }

    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var coachingNotes by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize().background(MatteBlack)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            item {
                Text(
                    text = "MEET THE FOUNDER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        color = RedBrand,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Athletic Coaching Center",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = TextLight,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // FOUNDER HERO BRANDING BLOCK (3D Sleek Highlight theme)
            item {
                val uriHandler = LocalUriHandler.current
                Card(
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, SteelGray),
                    colors = CardDefaults.cardColors(containerColor = CarbonSlate),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Dumbbell symbol badge
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(RedBrand.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                    .border(1.dp, RedBrand, RoundedCornerShape(12.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = "Builder avatar",
                                    tint = RedBrand,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Jatin Kumar Jain",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp,
                                        color = TextLight
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.VerifiedUser,
                                        contentDescription = "Verified Coach",
                                        tint = GlowingAmber,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "ISSA Nutritionist & Weight Management Specialist",
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Founder of @proteintheoryjaipur",
                                    fontSize = 10.sp,
                                    color = GlowingAmber
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Specialty tags
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SteelGray, RoundedCornerShape(8.dp))
                                .border(0.5.dp, SteelGray, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            StatPillRow("FAT LOSS", "✓ SEEDING")
                            StatPillRow("MUSCLE GAIN", "✓ MASSIVE")
                            StatPillRow("ONLINE COACHING", "✓ GLOBAL")
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Jatin is a certified Nutrition Specialist and founder of Protein Theory Jaipur. He combines advanced exercise biomechanics with customized athletic meal formulations so that clients achieve stellar body recomposition securely and efficiently.",
                            color = TextMuted,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Start
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dial button
                        Button(
                            onClick = {
                                uriHandler.openUri("tel:+918058615380")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SteelGray),
                            border = BorderStroke(1.dp, RedBrand),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = "Call Coach", tint = RedBrand, modifier = Modifier.size(16.dp))
                                Text("SECURE CO-FOUNDER HOTLINE: +91 80586 15380", fontSize = 10.sp, color = TextLight, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // DYNAMIC 3D CREDENTIAL LOCKER HUB
            item {
                CredentialInteractiveHub()
            }

            // COACHING BOOKING CONSOLE
            item {
                Text(
                    text = "APPOINTMENT BOOKING CORRIDOR",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.5.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, RedBrand.copy(alpha = 0.5f)),
                    colors = CardDefaults.cardColors(containerColor = CarbonSlate),
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // 1. Service Type Choose
                        Text("1. CHOOSE ATHLETIC SESSION:", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))

                        CoachesServicesList.forEach { service ->
                            val isSelected = selectedService == service
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) RedBrand.copy(alpha = 0.08f) else SteelGray)
                                    .border(0.5.dp, if (isSelected) RedBrand else SteelGray, RoundedCornerShape(8.dp))
                                    .clickable { selectedService = service }
                                    .padding(8.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedService = service },
                                    colors = RadioButtonDefaults.colors(selectedColor = RedBrand, unselectedColor = TextMuted)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Column {
                                    Text(service.title, color = TextLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("₹${service.cost} | ${service.durationMin} MINS | ${service.details}", color = TextMuted, fontSize = 9.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 2. Select Date Row
                        Text("2. DATE SELECTOR:", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(CalendarDatesList) { date ->
                                val isSelected = selectedDate == date
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) RedBrand else SteelGray)
                                        .border(0.5.dp, if (isSelected) Color.Transparent else SteelGray, RoundedCornerShape(8.dp))
                                        .clickable { selectedDate = date }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = date,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TextLight else TextMuted
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 3. Time Slots Row
                        Text("3. TIME SLOTS AVAILABLE:", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(BookingTimeSlotsList) { slot ->
                                val isSelected = selectedSlot == slot
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) RedBrand else SteelGray)
                                        .border(0.5.dp, if (isSelected) Color.Transparent else SteelGray, RoundedCornerShape(8.dp))
                                        .clickable { selectedSlot = slot }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = slot,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TextLight else TextMuted
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 4. Client profile info
                        Text("4. CONTACT IDENTIFIER PARAMS:", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = clientName,
                            onValueChange = { clientName = it },
                            placeholder = { Text("Your Gym Name/ID", color = TextMuted, fontSize = 12.sp) },
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
                                .testTag("coach_client_name")
                        )

                        OutlinedTextField(
                            value = clientPhone,
                            onValueChange = { clientPhone = it },
                            placeholder = { Text("Mobile (+91 ...)", color = TextMuted, fontSize = 12.sp) },
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
                                .testTag("coach_client_phone")
                        )

                        OutlinedTextField(
                            value = coachingNotes,
                            onValueChange = { coachingNotes = it },
                            placeholder = { Text("Optional notes (goals, current weight, etc.)", color = TextMuted, fontSize = 12.sp) },
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
                                .testTag("coach_client_notes")
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Booking button
                        val canBook = clientName.trim().isNotEmpty() && clientPhone.trim().isNotEmpty()
                        Button(
                            onClick = {
                                viewModel.submitBooking(
                                    clientName = clientName,
                                    clientPhone = clientPhone,
                                    slotTime = selectedSlot,
                                    coachingType = selectedService.title,
                                    date = selectedDate,
                                    notes = coachingNotes
                                )
                                clientName = ""
                                clientPhone = ""
                                coachingNotes = ""
                            },
                            enabled = canBook,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RedBrand,
                                disabledContainerColor = SteelGray
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("book_coach_session")
                        ) {
                            Text(
                                text = if (canBook) "LOCK COACHING APPOINTMENT (₹${selectedService.cost})" else "ENTER YOUR PARAMS TO LOCK SLOT",
                                fontWeight = FontWeight.Bold,
                                color = if (canBook) TextLight else TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // DISPLAY ACTIVE CONFIRMED RESERVATIONS LIST
            if (bookedCoachingSessions.isNotEmpty()) {
                item {
                    Text(
                        text = "ACTIVE CONFIRMED RESERVATIONS BOARD",
                        fontSize = 11.sp,
                        color = MacroProteinGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    bookedCoachingSessions.forEach { book ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MacroProteinGreen.copy(alpha = 0.5f)),
                            colors = CardDefaults.cardColors(containerColor = CarbonSlate),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(book.coachingType, color = TextLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Card(colors = CardDefaults.cardColors(containerColor = MacroProteinGreen.copy(alpha = 0.15f))) {
                                        Text("CONFIRMED", color = MacroProteinGreen, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Client: ${book.clientName} | Phone: ${book.clientPhone}", color = TextMuted, fontSize = 11.sp)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 4.dp).fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.DateRange, contentDescription = "Booking status time icon", tint = GlowingAmber, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("${book.bookingDate} | ${book.chosenSlotTime}", color = GlowingAmber, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                                if (book.details.isNotEmpty()) {
                                    Text("Notes: ${book.details}", color = TextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                                }
                                Text("🔗 Meeting invite link: meet.google.com/ptj-suman-fit", color = MacroProteinGreen, fontSize = 10.sp, modifier = Modifier.padding(top = 6.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatPillRow(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 12.dp)) {
        Text(text = label, fontSize = 8.sp, color = TextMuted, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 14.sp, color = RedBrand, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
fun CertificateBadgePill(
    title: String,
    issuer: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .background(SteelGray, RoundedCornerShape(8.dp))
            .border(0.5.dp, SteelGray, RoundedCornerShape(8.dp))
            .padding(18.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Certificate mark",
            tint = MacroProteinGreen,
            modifier = Modifier.size(16.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, color = TextLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(issuer, color = TextMuted, fontSize = 9.sp)
        }
    }
}
