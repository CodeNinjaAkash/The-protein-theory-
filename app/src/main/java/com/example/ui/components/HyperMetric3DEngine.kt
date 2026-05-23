package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// Definition of a 3D point in space
data class Point3D(val x: Float, val y: Float, val z: Float)

// Macro types with distinct color codes and properties
enum class MacroType(
    val displayName: String,
    val description: String,
    val color: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val metricUnit: String,
    val dailyGoalValue: String,
    val infoMessage: String
) {
    PROTEIN(
        displayName = "PROTEIN COMPLEX CORE",
        description = "Lean Peptide Synthesis & BCAA Chains",
        color = Color(0xFF10B981), // MacroProteinGreen (Emerald)
        icon = Icons.Default.FitnessCenter,
        metricUnit = "155g / 180g",
        dailyGoalValue = "86.1% Synthesized",
        infoMessage = "Biologically high-value whey and protein peptide profiles active for skeletal muscle mass reconstruction."
    ),
    VITAMINS(
        displayName = "ESSENTIAL MICRO VITAMINS",
        description = "Co-Factor Metabolism & Energy Catalyst",
        color = Color(0xFFF59E0B), // GlowingAmber (Orange/Amber)
        icon = Icons.Default.Bolt,
        metricUnit = "100% RDA Target",
        dailyGoalValue = "98.4% Saturated",
        infoMessage = "Zinc, Magnesium and Vitamin D3/B12 complexes perfectly active for hormone balance & cellular recovery."
    ),
    NUTRITION(
        displayName = "NUTRITION PLAN TARGET",
        description = "Custom Culinary Formulas & Calorie Load",
        color = Color(0xFFEF4444), // RedBrand Accent
        icon = Icons.Default.Restaurant,
        metricUnit = "2,350 / 2,600 kcal",
        dailyGoalValue = "90.3% Optimized",
        infoMessage = "Premium athletic nourishment from Jagatpura kitchen, supporting metabolic health and fat-loss velocity."
    ),
    HYDRATION(
        displayName = "ANABOLIC VOLUMIZER MESH",
        description = "Intracellular Sarcoplasmic Volume",
        color = Color(0xFF3B82F6), // Ice Blue
        icon = Icons.Default.WaterDrop,
        metricUnit = "3.2L / 4.0L",
        dailyGoalValue = "80.0% Hydrated",
        infoMessage = "High electrolyte water levels. Keeps muscular cell volumization and athletic endurance fully engaged."
    )
}

@Composable
fun HyperMetric3DEngine(
    modifier: Modifier = Modifier
) {
    var selectedMacro by remember { mutableStateOf(MacroType.PROTEIN) }
    
    // 3D Rotations (in radians)
    var yaw by remember { mutableFloatStateOf(0.4f) }    // Rotation around Y
    var pitch by remember { mutableFloatStateOf(-0.3f) }  // Rotation around X
    
    // Interactivity state
    var isDragging by remember { mutableStateOf(false) }
    var autoSpinEnable by remember { mutableStateOf(true) }
    var scaleMultiplier by remember { mutableFloatStateOf(1.0f) }
    var particleQuantitySlider by remember { mutableFloatStateOf(8f) } // Ring density
    
    // Pulse animation for node selection
    val infiniteTransition = rememberInfiniteTransition(label = "Pulse3D")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowPulse"
    )

    // Orbital angle that drives atom rotation
    val orbitalAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * java.lang.Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbitalAngle"
    )

    // Auto spin effect when not dragging
    LaunchedEffect(isDragging, autoSpinEnable) {
        if (!isDragging && autoSpinEnable) {
            while (true) {
                yaw += 0.008f
                pitch = -0.3f + sin(yaw * 0.4f) * 0.15f // subtle floating wobble
                delay(16)
            }
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111113)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF26262B)),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Info & Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "3D HOLOGRAM METRIC ENGINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFF1E27), // RedBrand
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Interactive Anabolic Core",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                // Spin Toggle Button
                Box(
                    modifier = Modifier
                        .background(
                            if (autoSpinEnable) Color(0xFFFF1E27).copy(alpha = 0.12f) else Color.White.copy(alpha = 0.05f),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { autoSpinEnable = !autoSpinEnable }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (autoSpinEnable) Icons.Default.Sync else Icons.Default.SyncDisabled,
                            contentDescription = "Spin Mode",
                            tint = if (autoSpinEnable) Color(0xFFFF1E27) else Color.DarkGray,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (autoSpinEnable) "SPINNING" else "LOCKED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = if (autoSpinEnable) Color.White else Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Instructions Overlay HUD
            Text(
                text = "⚡ TAP NODES SELECT • SWIPE DRAG TO ROTATE DEPLOYMENT ORBIT",
                fontSize = 8.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                letterSpacing = 1.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            // 3D Canvas Box Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF070708))
                    .border(1.dp, Color(0xFF1E1E22), RoundedCornerShape(12.dp))
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { isDragging = true },
                            onDragEnd = { isDragging = false },
                            onDragCancel = { isDragging = false },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                // Drag updates our 3D yaw and pitch coordinates
                                yaw += dragAmount.x * 0.007f
                                pitch = (pitch + dragAmount.y * 0.007f).coerceIn(-1.2f, 1.2f)
                            }
                        )
                    }
            ) {
                // Background 3D Radar Circle grid depth
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2
                    val cy = h / 2
                    
                    // Scope radar concentric grids
                    drawCircle(Color(0xFF141416), radius = cx * 0.8f, center = Offset(cx, cy))
                    drawCircle(Color(0xFF141416), radius = cx * 0.55f, center = Offset(cx, cy))
                    drawCircle(Color(0xFF141416), radius = cx * 0.3f, center = Offset(cx, cy))
                    
                    // Radar line sweeps
                    drawLine(Color(0xFF1F1F24), Offset(cx - cx * 0.8f, cy), Offset(cx + cx * 0.8f, cy), 1f)
                    drawLine(Color(0xFF1F1F24), Offset(cx, cy - cy * 0.8f), Offset(cx, cy + cy * 0.8f), 1f)
                }

                // Node interactive coordinates tracking container
                var nodeHotspots by remember { mutableStateOf<List<Pair<MacroType, Offset>>>(emptyList()) }

                // The Main mathematical 3D Renderer!
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(nodeHotspots) {
                            // Touch detection directly inside the 3D projected Canvas nodes
                            detectDragGestures(
                                onDragStart = { isDragging = true },
                                onDragEnd = { isDragging = false },
                                onDragCancel = { isDragging = false },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    yaw += dragAmount.x * 0.007f
                                    pitch = (pitch + dragAmount.y * 0.007f).coerceIn(-1.2f, 1.2f)
                                }
                            )
                        }
                        .clickable(enabled = true) {
                            // Let's implement proximity click targeting on 3D coordinates projection!
                            // (We intercept tap events from here)
                        }
                ) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2
                    val cy = h / 2
                    
                    // Dimensions of our core hologram
                    val rPrimaryMax = (cx * 0.40f) * scaleMultiplier // Radius of spheres
                    val dDepth = 350f / scaleMultiplier // Camera focal depth

                    // 3D coordinate rotator functions
                    fun rotatePoint(pt: Point3D): Point3D {
                        // 1st: Rotate around Y axis (Yaw)
                        val xY = pt.x * cos(yaw) - pt.z * sin(yaw)
                        val zY = pt.x * sin(yaw) + pt.z * cos(yaw)
                        val yY = pt.y

                        // 2nd: Rotate around X axis (Pitch)
                        val yX = yY * cos(pitch) - zY * sin(pitch)
                        val zX = yY * sin(pitch) + zY * cos(pitch)
                        val xX = xY

                        return Point3D(xX, yX, zX)
                    }

                    // Project 3D coordinate to 2D Screen
                    fun projectPoint(pt: Point3D): Offset {
                        // Perspective divide
                        val scaleFactor = dDepth / (dDepth + pt.z)
                        val screenX = cx + pt.x * scaleFactor * 1.5f
                        val screenY = cy + pt.y * scaleFactor * 1.5f
                        return Offset(screenX, screenY)
                    }

                    // Let's draw standard horizontal grid rings of our 3D core cage
                    val ringSteps = particleQuantitySlider.toInt()
                    val ringsList = listOf(-60f, 0f, 60f) // Y coordinates of the 3 rings
                    
                    ringsList.forEachIndexed { ringIdx, ringY ->
                        val path = Path()
                        val ringTypeColor = when(ringIdx) {
                            0 -> MacroType.PROTEIN.color.copy(alpha = 0.35f)
                            1 -> MacroType.VITAMINS.color.copy(alpha = 0.35f)
                            else -> MacroType.NUTRITION.color.copy(alpha = 0.35f)
                        }
                        
                        // We construct a circle ring in 3D by compiling polar coordinates points
                        val points3D = mutableListOf<Point3D>()
                        val numSegments = 36
                        for (i in 0..numSegments) {
                            val angle = (2 * Math.PI * i / numSegments).toFloat()
                            val rx = rPrimaryMax * cos(angle)
                            val rz = rPrimaryMax * sin(angle)
                            points3D.add(Point3D(rx, ringY, rz))
                        }

                        // Rotate and compile projected offsets
                        val screenOffsets = points3D.map { projectPoint(rotatePoint(it)) }
                        
                        // Begin path drawing
                        path.moveTo(screenOffsets[0].x, screenOffsets[0].y)
                        for (i in 1 until screenOffsets.size) {
                            path.lineTo(screenOffsets[i].x, screenOffsets[i].y)
                        }
                        path.close()

                        // Draw ring lines
                        drawPath(
                            path = path,
                            color = ringTypeColor,
                            style = Stroke(
                                width = if (selectedMacro.ordinal == ringIdx) 7f else 2.5f,
                                pathEffect = if (selectedMacro.ordinal != ringIdx) androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(12f, 10f)) else null
                            )
                        )
                    }

                    // Draw Vertical Alignment structural struts (Anabolic Volume Shield wires)
                    val strutCount = 6
                    for (s in 0 until strutCount) {
                        val strutAngle = (2 * Math.PI * s / strutCount).toFloat()
                        val sx = rPrimaryMax * cos(strutAngle)
                        val sz = rPrimaryMax * sin(strutAngle)
                        
                        val top3D = rotatePoint(Point3D(sx, -90f, sz))
                        val bot3D = rotatePoint(Point3D(sx, 90f, sz))
                        
                        // Perspective divide
                        val scaleT = dDepth / (dDepth + top3D.z)
                        val scaleB = dDepth / (dDepth + bot3D.z)
                        
                        val ptTop = Offset(cx + top3D.x * scaleT * 1.5f, cy + top3D.y * scaleT * 1.5f)
                        val ptBottom = Offset(cx + bot3D.x * scaleB * 1.5f, cy + bot3D.y * scaleB * 1.5f)
                        
                        drawLine(
                            color = MacroType.HYDRATION.color.copy(alpha = 0.2f),
                            start = ptTop,
                            end = ptBottom,
                            strokeWidth = 1.8f
                        )
                    }

                    // 🧬 NEW 3D HIGH FIDELITY DOUBLE HELIX WAVE (Representing Protein Peptide Chains & Vitamin Particles)
                    val helixRes = 24
                    val helixRadius = rPrimaryMax * 0.3f // tight elegant center helix
                    for (h in 0..helixRes) {
                        val theta = (h * 4.5f * Math.PI / helixRes).toFloat()
                        val hy = -80f + (160f * h / helixRes)
                        
                        // Protein Helix 1 Strand
                        val hx1 = helixRadius * cos(theta + orbitalAngle)
                        val hz1 = helixRadius * sin(theta + orbitalAngle)
                        
                        // Vitamin Helix 2 Strand (180 deg out of phase)
                        val hx2 = helixRadius * cos(theta + orbitalAngle + Math.PI.toFloat())
                        val hz2 = helixRadius * sin(theta + orbitalAngle + Math.PI.toFloat())
                        
                        val pt1Rot = rotatePoint(Point3D(hx1, hy, hz1))
                        val pt2Rot = rotatePoint(Point3D(hx2, hy, hz2))
                        
                        val proj1 = projectPoint(pt1Rot)
                        val proj2 = projectPoint(pt2Rot)
                        
                        val scale1 = dDepth / (dDepth + pt1Rot.z)
                        val scale2 = dDepth / (dDepth + pt2Rot.z)
                        
                        // Draw peptide nodes orbiting
                        drawCircle(
                            color = MacroType.PROTEIN.color.copy(alpha = 0.7f),
                            radius = 3.5f * scale1,
                            center = proj1
                        )
                        drawCircle(
                            color = MacroType.VITAMINS.color.copy(alpha = 0.7f),
                            radius = 3.5f * scale2,
                            center = proj2
                        )
                        
                        // Draw bonding connector filaments
                        if (h % 2 == 0) {
                            drawLine(
                                color = Color.White.copy(alpha = 0.22f),
                                start = proj1,
                                end = proj2,
                                strokeWidth = 1.2f
                            )
                        }
                    }

                    // Let's position and render major interactive Node Spheres representing macros!
                    // Node 1: Protein (Top Core Center)
                    val ptProt = Point3D(0f, -60f, 0f)
                    // Node 2: Vitamins (Middle Left Orbit)
                    val ptCarbs = Point3D(-rPrimaryMax * 0.9f, 0f, -rPrimaryMax * 0.4f)
                    // Node 3: Nutrition (Middle Right Orbit)
                    val ptFats = Point3D(rPrimaryMax * 0.9f, 0f, rPrimaryMax * 0.4f)
                    // Node 4: Hydration (Bottom Core Center)
                    val ptHydra = Point3D(0f, 60f, 0f)

                    val nodesList = listOf(
                        MacroType.PROTEIN to ptProt,
                        MacroType.VITAMINS to ptCarbs,
                        MacroType.NUTRITION to ptFats,
                        MacroType.HYDRATION to ptHydra
                    )

                    val spots = mutableListOf<Pair<MacroType, Offset>>()

                    nodesList.forEach { (type, originalCoord) ->
                        val rot = rotatePoint(originalCoord)
                        val proj = projectPoint(rot)
                        spots.add(type to proj)

                        val scaleFactor = dDepth / (dDepth + rot.z)
                        val circleRadius = (16f * scaleFactor).coerceAtLeast(6f)
                        val isSelected = selectedMacro == type

                        // Core Node Glow outline
                        if (isSelected) {
                            drawCircle(
                                color = type.color.copy(alpha = 0.18f * pulseAlpha),
                                radius = circleRadius * 3.5f,
                                center = proj
                            )
                            drawCircle(
                                color = type.color.copy(alpha = 0.45f * pulseAlpha),
                                radius = circleRadius * 2f,
                                center = proj
                            )
                        }

                        // Physical node circle
                        drawCircle(
                            color = if (isSelected) type.color else type.color.copy(alpha = 0.65f),
                            radius = circleRadius,
                            center = proj
                        )

                        // Center white hot electron speck
                        drawCircle(
                            color = Color.White,
                            radius = circleRadius * 0.35f,
                            center = proj
                        )

                        // 3D Tag overlay text
                        drawIntoCanvas { canvas ->
                            try {
                                val textPaint = android.graphics.Paint().apply {
                                    color = if (isSelected) android.graphics.Color.WHITE else android.graphics.Color.GRAY
                                    textSize = (9.dp.toPx() * scaleFactor).coerceAtLeast(18f)
                                    isFakeBoldText = isSelected
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    typeface = android.graphics.Typeface.create(android.graphics.Typeface.MONOSPACE, android.graphics.Typeface.BOLD)
                                }
                                canvas.nativeCanvas.drawText(
                                    if (isSelected) "● ${type.displayName.split(" ")[0]}" else type.displayName.split(" ")[0],
                                    proj.x,
                                    proj.y - circleRadius - 10f,
                                    textPaint
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    // Draw fast orbiting atom particles!
                    val atomCount = 4
                    for (a in 0 until atomCount) {
                        val offsetAngle = (a * (2 * Math.PI) / atomCount).toFloat() + orbitalAngle
                        val ax = rPrimaryMax * cos(offsetAngle)
                        val az = rPrimaryMax * sin(offsetAngle)
                        val ay = sin(offsetAngle * 2) * 20f // orbital wavy z-axis glide
                        
                        val rotAtom = rotatePoint(Point3D(ax, ay, az))
                        val projAtom = projectPoint(rotAtom)
                        val atomScale = dDepth / (dDepth + rotAtom.z)
                        
                        drawCircle(
                            color = selectedMacro.color,
                            radius = 4.5f * atomScale,
                            center = projAtom
                        )
                        
                        // Atom trail halo
                        drawCircle(
                            color = selectedMacro.color.copy(alpha = 0.25f),
                            radius = 9f * atomScale,
                            center = projAtom
                        )
                    }

                    // Update parent hot-spot coordinates registry
                    nodeHotspots = spots
                }

                // Invisible click interceptor on node hotspots
                nodeHotspots.forEach { (type, offset) ->
                    Box(
                        modifier = Modifier
                            .offset(
                                x = with(LocalDensity.current) { (offset.x - 24).toDp() },
                                y = with(LocalDensity.current) { (offset.y - 24).toDp() }
                            )
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable {
                                selectedMacro = type
                            }
                    )
                }

                // Corner HUD Coordinates Stream overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .padding(6.dp)
                ) {
                    Text(
                        text = "CORE VECTOR COORDS",
                        fontSize = 7.sp,
                        color = Color.LightGray,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "YAW:  ${((yaw * 10000).toInt() / 10000.0)} rad",
                        fontSize = 7.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "PITCH: ${((pitch * 10000).toInt() / 10000.0)} rad",
                        fontSize = 7.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "CELLS: ${particleQuantitySlider.toInt() * 3 + 6} NODES ACTIVE",
                        fontSize = 7.sp,
                        color = selectedMacro.color,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Active Node Status Panel Overlay in Top-Right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .background(selectedMacro.color.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .border(1.dp, selectedMacro.color.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(selectedMacro.color, CircleShape)
                        )
                        Text(
                            text = "FOCUS: ${selectedMacro.displayName.split(" ")[0]}",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Selection Quick Tab Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MacroType.values().forEach { type ->
                    val isSelected = selectedMacro == type
                    Button(
                        onClick = { selectedMacro = type },
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) type.color else Color(0xFF1E1E22),
                            contentColor = if (isSelected) Color.Black else Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = type.icon,
                                contentDescription = type.displayName,
                                modifier = Modifier.size(11.dp),
                                tint = if (isSelected) Color.Black else type.color
                            )
                            Text(
                                text = type.displayName.split(" ")[0],
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details Panel of Selected 3D Node
            AnimatedContent(
                targetState = selectedMacro,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220, delayMillis = 90)) + slideInHorizontally(
                        initialOffsetX = { 80 }) togetherWith
                            fadeOut(animationSpec = tween(90))
                },
                label = "DetailAnimate"
            ) { active ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161619)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, active.color.copy(alpha = 0.25f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = active.icon,
                                    contentDescription = active.displayName,
                                    tint = active.color,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = active.displayName,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                            
                            Text(
                                text = active.dailyGoalValue,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                color = active.color
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = active.description,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )

                        Divider(
                            color = Color(0xFF26262B),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "TARGET FUEL SYNTH LEVEL",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = active.metricUnit,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }

                            // Radial progress score indicator directly inside card
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(38.dp)
                            ) {
                                CircularProgressIndicator(
                                    progress = { 0.82f },
                                    color = active.color,
                                    strokeWidth = 3.5.dp,
                                    trackColor = Color(0xFF26262B)
                                )
                                Text(
                                    text = "82%",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = active.infoMessage,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.LightGray,
                            lineHeight = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hologram fine adjustments
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Interactive slider for zoom perspective multiplier
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "3D PERSPECTIVE ZOOM",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = "${(scaleMultiplier * 100).toInt()}%",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Slider(
                        value = scaleMultiplier,
                        onValueChange = { scaleMultiplier = it },
                        valueRange = 0.6f..1.4f,
                        colors = SliderDefaults.colors(
                            thumbColor = selectedMacro.color,
                            activeTrackColor = selectedMacro.color.copy(alpha = 0.6f),
                            inactiveTrackColor = Color(0xFF26262B)
                        ),
                        modifier = Modifier.height(20.dp)
                    )
                }

                // Interactive slider for mesh ring steps (granularity)
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "MESH DENSITY STEPS",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = "${particleQuantitySlider.toInt()}",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Slider(
                        value = particleQuantitySlider,
                        onValueChange = { particleQuantitySlider = it },
                        valueRange = 4f..16f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = selectedMacro.color,
                            activeTrackColor = selectedMacro.color.copy(alpha = 0.6f),
                            inactiveTrackColor = Color(0xFF26262B)
                        ),
                        modifier = Modifier.height(20.dp)
                    )
                }
            }
        }
    }
}
