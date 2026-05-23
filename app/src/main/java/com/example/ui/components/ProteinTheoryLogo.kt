package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProteinTheoryLogo(modifier: Modifier = Modifier, scale: Float = 1f) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // 🧬 Custom Drawn 3D "P" Logo with Dumbbell Weights
        Canvas(
            modifier = Modifier
                .size((75 * scale).dp)
                .padding(2.dp)
        ) {
            val w = size.width
            val h = size.height
            
            // 3D Shadow layer (offset down and right in dark grey for 3D depth)
            val shadowColor = Color(0xFF1E1E21)
            drawP(this, shadowColor, 3f, 3f, w, h)
            
            // Main white P body
            val pColor = Color.White
            drawP(this, pColor, 0f, 0f, w, h)
            
            // Dumbbell Bar (Horizontal cutting through middle of 'P')
            val barY = h * 0.44f
            val barHeight = h * 0.08f
            val barStart = w * 0.03f
            val barEnd = w * 0.88f
            
            // Bar shadow
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.4f),
                topLeft = Offset(barStart + 2f, barY + 2f),
                size = Size(barEnd - barStart, barHeight),
                cornerRadius = CornerRadius(2f, 2f)
            )
            
            // Red Barbell core shaft
            drawRoundRect(
                color = Color(0xFFFF1E27),
                topLeft = Offset(barStart, barY),
                size = Size(barEnd - barStart, barHeight),
                cornerRadius = CornerRadius(2f, 2f)
            )
            
            // Spiced Red Plates Stack on lateral sides
            val pLeft1X = w * 0.03f
            val pLeft2X = w * 0.10f
            val pLeft3X = w * 0.17f
            
            val pRight1X = w * 0.80f
            val pRight2X = w * 0.73f
            val pRight3X = w * 0.66f
            
            val plateW = w * 0.05f
            
            // Left plates stack
            drawPlateGroup(this, pLeft1X, barY, plateW, barHeight, h * 0.12f)
            drawPlateGroup(this, pLeft2X, barY, plateW, barHeight, h * 0.22f)
            drawPlateGroup(this, pLeft3X, barY, plateW, barHeight, h * 0.32f)
            
            // Right plates stack
            drawPlateGroup(this, pRight1X, barY, plateW, barHeight, h * 0.12f)
            drawPlateGroup(this, pRight2X, barY, plateW, barHeight, h * 0.22f)
            drawPlateGroup(this, pRight3X, barY, plateW, barHeight, h * 0.32f)
        }
        
        Spacer(modifier = Modifier.width((12 * scale).dp))
        
        // Branded Stack text
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "PROTEIN",
                color = Color.White,
                fontSize = (25 * scale).sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (0.5 * scale).sp,
                fontFamily = FontFamily.SansSerif,
                lineHeight = (25 * scale).sp
            )
            Text(
                text = "THEORY",
                color = Color(0xFFFF1E27),
                fontSize = (25 * scale).sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (0.5 * scale).sp,
                fontFamily = FontFamily.SansSerif,
                lineHeight = (25 * scale).sp
            )
            Spacer(modifier = Modifier.height((2 * scale).dp))
            Text(
                text = "— FUEL YOUR THEORY —",
                color = Color.White,
                fontSize = (8 * scale).sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (1.8 * scale).sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

private fun drawP(scope: androidx.compose.ui.graphics.drawscope.DrawScope, color: Color, offsetX: Float, offsetY: Float, w: Float, h: Float) {
    val path = Path().apply {
        // Vertical stalk
        moveTo(w * 0.28f + offsetX, h * 0.10f + offsetY)
        lineTo(w * 0.52f + offsetX, h * 0.10f + offsetY)
        
        // Top outer loop curve
        cubicTo(
            w * 0.88f + offsetX, h * 0.10f + offsetY,
            w * 0.88f + offsetX, h * 0.58f + offsetY,
            w * 0.52f + offsetX, h * 0.58f + offsetY
        )
        
        // Loop back and vertical drop
        lineTo(w * 0.44f + offsetX, h * 0.58f + offsetY)
        lineTo(w * 0.44f + offsetX, h * 0.90f + offsetY)
        lineTo(w * 0.28f + offsetX, h * 0.90f + offsetY)
        close()
    }
    
    scope.drawPath(
        path = path,
        color = color,
        style = Fill
    )
    
    // Hollow inner circle subtraction inside 'P' loop
    val hollowPath = Path().apply {
        moveTo(w * 0.44f + offsetX, h * 0.24f + offsetY)
        lineTo(w * 0.52f + offsetX, h * 0.24f + offsetY)
        cubicTo(
            w * 0.68f + offsetX, h * 0.24f + offsetY,
            w * 0.68f + offsetX, h * 0.44f + offsetY,
            w * 0.52f + offsetX, h * 0.44f + offsetY
        )
        lineTo(w * 0.44f + offsetX, h * 0.44f + offsetY)
        close()
    }
    
    scope.drawPath(
        path = hollowPath,
        color = Color(0xFF0C0C0D), // Overlay background color
        style = Fill
    )
}

private fun drawPlateGroup(
    scope: androidx.compose.ui.graphics.drawscope.DrawScope, 
    x: Float, 
    barY: Float, 
    plateW: Float, 
    barHeight: Float, 
    plateH: Float
) {
    val plateY = barY + barHeight / 2 - plateH / 2
    // Shadow
    scope.drawRoundRect(
        color = Color.Black.copy(alpha = 0.4f),
        topLeft = Offset(x + 1f, plateY + 1f),
        size = Size(plateW, plateH),
        cornerRadius = CornerRadius(2f, 2f)
    )
    // Core Plate
    scope.drawRoundRect(
        color = Color(0xFFFF1E27),
        topLeft = Offset(x, plateY),
        size = Size(plateW, plateH),
        cornerRadius = CornerRadius(2f, 2f)
    )
}
