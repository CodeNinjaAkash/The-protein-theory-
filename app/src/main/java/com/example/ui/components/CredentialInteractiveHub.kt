package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

data class PersonalTrainerCertificate(
    val title: String,
    val type: String, // "ISSA_NUTRITION", "ISSA_WEIGHT", "K11_PT", "REPS_INDIA", "TEAM_BOSS"
    val certNo: String,
    val issueDate: String,
    val expiryDate: String,
    val authority: String,
    val gradeOrStatus: String,
    val description: String,
    val color: Color
)

val JatinCertificatesList = listOf(
    PersonalTrainerCertificate(
        title = "ISSA SPORTS NUTRITIONIST CERTIFIED",
        type = "ISSA_NUTRITION",
        certNo = "5789857",
        issueDate = "April 16, 2025",
        expiryDate = "April 16, 2027",
        authority = "International Sports Sciences Association (ISSA)",
        gradeOrStatus = "ACCREDITED NUTRITIONIST",
        description = "Global gold standard master qualification in sports nutrition metabolics, macro-nutrient partition planning, skeletal recovery diets, and calorie compensation models.",
        color = Color(0xFF10B981) // High Protein Emerald
    ),
    PersonalTrainerCertificate(
        title = "ISSA WEIGHT MANAGEMENT SPECIALIST",
        type = "ISSA_WEIGHT",
        certNo = "5789856",
        issueDate = "April 16, 2025",
        expiryDate = "April 16, 2027",
        authority = "International Sports Sciences Association (ISSA)",
        gradeOrStatus = "WEIGHT ANALYSIS EXPERT",
        description = "Advanced clinical certification specialized in endocrine hormone modulation, progressive fat loss velocity protocols, healthy lean tissue retention, and muscle building biology.",
        color = Color(0xFF3B82F6) // Electric Blue
    ),
    PersonalTrainerCertificate(
        title = "K11 DIPLOMA IN PERSONAL TRAINING",
        type = "K11_PT",
        certNo = "StudID: 9742165",
        issueDate = "October 30, 2024",
        expiryDate = "Lifetime Credential",
        authority = "K11 School of Fitness Sciences",
        gradeOrStatus = "GRADE: PASS (HIGH RANK)",
        description = "Extensive training in human body mechanics, resistance training biomechanics, muscular hypertrophy programming, and safety diagnostics for advanced lifters.",
        color = Color(0xFFEF4444) // Neon Red
    ),
    PersonalTrainerCertificate(
        title = "REPS INDIA OFFICIAL REGISTRATION",
        type = "REPS_INDIA",
        certNo = "REPSIN008812",
        issueDate = "January 30, 2024",
        expiryDate = "January 30, 2026",
        authority = "Register of Exercise Professionals India",
        gradeOrStatus = "CATEGORY A - FULL STATUS",
        description = "Professional global exercise standards match recognition. Full legal licensure and accreditation for private and group coaching systems in Jaipur, India.",
        color = Color(0xFFFF9800) // Warming Amber
    ),
    PersonalTrainerCertificate(
        title = "TEAM BOSS BODYBUILDING CONTEST PREP",
        type = "TEAM_BOSS",
        certNo = "CPMUM2101202409",
        issueDate = "January 21, 2024",
        expiryDate = "Completed Certification",
        authority = "Team Boss Fitness Academy (BICP)",
        gradeOrStatus = "BICP LEVEL 1 THEORY",
        description = "Specialized certification from athletic authority Harry Sandhu in peak week hydration manipulation, glycogen loading cycles, anabolic system science, and muscular symmetry optimization.",
        color = Color(0xFFA855F7) // Purple Neon
    )
)

@Composable
fun CredentialInteractiveHub(
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentCert = JatinCertificatesList[selectedIndex]

    // 3D Tilt rotations driven by drag
    var tiltX by remember { mutableFloatStateOf(0f) }
    var tiltY by remember { mutableFloatStateOf(0f) }

    val coroutineScope = rememberCoroutineScope()

    // Smooth return to zero translation
    val animTiltX = animateFloatAsState(
        targetValue = tiltX,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "TiltXAnim"
    )
    val animTiltY = animateFloatAsState(
        targetValue = tiltY,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "TiltYAnim"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0F0F11), RoundedCornerShape(24.dp))
            .border(BorderStroke(1.dp, Color(0xFF1F1F24)), RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ACCREDITED CREDENTIALS HUB",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.8.sp,
                        color = Color(0xFFFF1E27),
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Text(
                    text = "Interactive Certification Locker",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
            // Verified Badge Indicator (Green LED flashing subtly)
            Box(
                modifier = Modifier
                    .background(Color(0xFF10B981).copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                    .border(BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f)), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color(0xFF10B981), CircleShape)
                    )
                    Text(
                        text = "5/5 VERIFIED",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Multi-Certificate Horizontal selector tags
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            JatinCertificatesList.forEachIndexed { index, cert ->
                val isSelected = selectedIndex == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) cert.color else Color(0xFF17171B))
                        .border(
                            BorderStroke(1.dp, if (isSelected) Color.White.copy(alpha = 0.4f) else Color(0xFF26262B)),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            selectedIndex = index
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (cert.type) {
                            "ISSA_NUTRITION" -> "ISSA\nNUTRI"
                            "ISSA_WEIGHT" -> "ISSA\nWMS"
                            "K11_PT" -> "K11\nPT"
                            "REPS_INDIA" -> "REPS\nIND"
                            else -> "BOSS\nPREP"
                        },
                        fontSize = 8.sp,
                        lineHeight = 9.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = if (isSelected) Color.Black else Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Swipe Instructions Overlay
        Text(
            text = "⚡ SWIPE OR DRAG THE CARD TO ROTATE IN 3D REfLECTIVE GLOSS MODE",
            fontSize = 7.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            letterSpacing = 0.8.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Interactive 3D Mock Certificate Card (Uses tilt and graphic transformation layers!)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .graphicsLayer {
                    // Feed current tilt states into 3D projection parameters
                    rotationX = animTiltX.value
                    rotationY = animTiltY.value
                    cameraDistance = 14f * density
                }
                .pointerInput(currentCert) {
                    detectDragGestures(
                        onDragStart = { },
                        onDragEnd = {
                            tiltX = 0f
                            tiltY = 0f
                        },
                        onDragCancel = {
                            tiltX = 0f
                            tiltY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            // Calculate simple physics limits
                            tiltX = (tiltX - dragAmount.y * 0.15f).coerceIn(-18f, 18f)
                            tiltY = (tiltY + dragAmount.x * 0.15f).coerceIn(-18f, 18f)
                        }
                    )
                }
                .clip(RoundedCornerShape(16.dp))
                .border(BorderStroke(1.dp, currentCert.color.copy(alpha = 0.4f)), RoundedCornerShape(16.dp))
        ) {
            // Underlay details & graphic vector replica drawn on Compose Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Draw solid background with dynamic gradient aligned with modern premium styling
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF232328), Color(0xFF0F0F11)),
                        center = Offset(w / 2, h / 2),
                        radius = w * 0.8f
                    )
                )

                // Classic elegant secure outer credential border
                drawRoundRect(
                    color = currentCert.color.copy(alpha = 0.5f),
                    topLeft = Offset(12f, 12f),
                    size = Size(w - 24f, h - 24f),
                    cornerRadius = CornerRadius(12f, 12f),
                    style = Stroke(width = 2.5f)
                )

                // Secondary inner elegant hairline border
                drawRoundRect(
                    color = Color.DarkGray.copy(alpha = 0.4f),
                    topLeft = Offset(18f, 18f),
                    size = Size(w - 36f, h - 36f),
                    cornerRadius = CornerRadius(10f, 10f),
                    style = Stroke(width = 0.8f)
                )

                // Draw geometric verification watermark emblem in center background
                drawCircle(
                    color = currentCert.color.copy(alpha = 0.05f),
                    radius = h * 0.35f,
                    center = Offset(w * 0.75f, h * 0.5f),
                    style = Stroke(width = 2f)
                )

                // Draw interactive gloss reflect line directly governed by current tilt!
                val gleamX = (animTiltY.value + 18f) / 36f * w
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f),
                            Color.White.copy(alpha = 0.16f),
                            Color.White.copy(alpha = 0f)
                        ),
                        start = Offset(gleamX - 60f, 0f),
                        end = Offset(gleamX + 60f, h)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(w, h),
                    strokeWidth = 40f
                )

                // ISSA/K11 Accreditation Ribbon Stamp on the Right
                drawCircle(
                    color = currentCert.color.copy(alpha = 0.15f),
                    radius = 32f,
                    center = Offset(w - 60f, h - 60f)
                )
                drawCircle(
                    color = currentCert.color,
                    radius = 26f,
                    center = Offset(w - 60f, h - 60f),
                    style = Stroke(width = 1.5f)
                )
                // Small secure certification text badge details
                drawIntoCanvas { canvas ->
                    try {
                        val textPaint = android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = 10.dp.toPx()
                            isFakeBoldText = true
                            typeface = android.graphics.Typeface.create(android.graphics.Typeface.MONOSPACE, android.graphics.Typeface.BOLD)
                        }

                        // Certificate Authority Name Text
                        canvas.nativeCanvas.drawText(
                            currentCert.authority.uppercase(),
                            35f,
                            48f,
                            textPaint
                        )

                        // Secondary Subheader
                        textPaint.apply {
                            color = currentCert.color.toArgb()
                            textSize = 8.dp.toPx()
                        }
                        canvas.nativeCanvas.drawText(
                            "OFFICIAL DIGITAL CREDENTIAL",
                            35f,
                            68f,
                            textPaint
                        )

                        // Name of Recipient ("Jatin Kumar Jain" beautifully presented)
                        textPaint.apply {
                            color = android.graphics.Color.WHITE
                            textSize = 15.dp.toPx()
                            isFakeBoldText = true
                            typeface = android.graphics.Typeface.create(android.graphics.Typeface.SERIF, android.graphics.Typeface.BOLD_ITALIC)
                        }
                        canvas.nativeCanvas.drawText(
                            "Jatin Kumar Jain",
                            35f,
                            110f,
                            textPaint
                        )

                        // Certificate classification
                        textPaint.apply {
                            color = android.graphics.Color.GRAY
                            textSize = 7.5.dp.toPx()
                            typeface = android.graphics.Typeface.create(android.graphics.Typeface.MONOSPACE, android.graphics.Typeface.NORMAL)
                        }
                        canvas.nativeCanvas.drawText(
                            "Accredited Spec: ${currentCert.gradeOrStatus}",
                            35f,
                            130f,
                            textPaint
                        )

                        // Issue Code and Dates footer
                        canvas.nativeCanvas.drawText(
                            "Reg No: ${currentCert.certNo}",
                            35f,
                            168f,
                            textPaint
                        )
                        canvas.nativeCanvas.drawText(
                            "Issued: ${currentCert.issueDate} | Exp: ${currentCert.expiryDate}",
                            35f,
                            182f,
                            textPaint
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            // High Fidelity verification mark on top-right corner overlay
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Spec",
                        tint = currentCert.color,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = when(currentCert.type) {
                            "ISSA_NUTRITION", "ISSA_WEIGHT" -> "ISSA CERT"
                            "K11_PT" -> "K11 DIPLOMA"
                            "REPS_INDIA" -> "REPS REGD"
                            else -> "BICP COMPLETED"
                        },
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Certificate Details Panel
        AnimatedContent(
            targetState = currentCert,
            transitionSpec = {
                fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(150))
            },
            label = "CertDetailsAnimate"
        ) { activeCert ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141416)),
                border = BorderStroke(1.dp, Color(0xFF26262B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = activeCert.title,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = activeCert.authority,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = activeCert.color
                    )

                    Divider(
                        color = Color(0xFF26262B),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    Text(
                        text = "SECURE AUDIT CREDENTIAL PARAMETERS",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("LICENSE ID NUMBER", fontSize = 8.sp, color = Color.Gray)
                            Text(activeCert.certNo, fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                        Column {
                            Text("ACCREDITATION LEVEL", fontSize = 8.sp, color = Color.Gray)
                            Text(activeCert.gradeOrStatus, fontSize = 11.sp, fontWeight = FontWeight.Black, color = activeCert.color)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = activeCert.description,
                        fontSize = 10.5.sp,
                        color = Color.LightGray,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Connect social buttons
        val uriHandler = LocalUriHandler.current
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    uriHandler.openUri("https://www.instagram.com/_fitjatin?igsh=amp3bTZvdDV1ejFw")
                },
                modifier = Modifier
                    .weight(1.5f)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1E27)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Launch,
                        contentDescription = "Instagram Owner",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "JATIN'S INSTAGRAM",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Button(
                onClick = {
                    uriHandler.openUri("https://www.instagram.com/proteintheoryjaipur?igsh=bmZkOXplMzNpNDM5")
                },
                modifier = Modifier
                    .weight(1.5f)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E22)),
                border = BorderStroke(1.dp, Color(0xFF26262B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Instagram Cafe",
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "CAFE INSTAGRAM",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
