package com.example.aura.presentation.recording

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun RecordingScreen(
    isProcessing: Boolean = false,
    elapsedSeconds: Int = 0,
    pipelineStep: PipelineStep = PipelineStep.IDLE,
    onStopClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val timeString = "%d:%02d".format(minutes, seconds)

    val gradientBrush = Brush.verticalGradient(
        colors = if (isProcessing) listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.background)
        else listOf(Color(0xFF1A1A1A), Color.Black)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isProcessing) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("REC $timeString", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isProcessing) {
            Text(
                "Analysing your meeting...",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(32.dp))
            // Stepper
            Row(verticalAlignment = Alignment.CenterVertically) {
                StepperStep("Transcript", pipelineStep >= PipelineStep.EXTRACTING)
                StepperDivider()
                StepperStep("Extracting", pipelineStep >= PipelineStep.COMPOSING)
                StepperDivider()
                StepperStep("Compose", pipelineStep == PipelineStep.DONE)
            }
        } else {
            // Waveform
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(100.dp)
            ) {
                WaveformBar(targetHeight = 40f)
                WaveformBar(targetHeight = 80f)
                WaveformBar(targetHeight = 60f)
                WaveformBar(targetHeight = 100f)
                WaveformBar(targetHeight = 50f)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Transcript / Streaming Results box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                if (isProcessing) {
                    Text("✓ 1 decision found\n✓ Action item: Priya -> comms\n⟳ Checking deadlines...")
                } else {
                    Text("\"...and we decided to push the launch by two weeks, Priya will own the comms plan by Friday...\"")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Bottom Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancelClick) {
                Text("Cancel", color = if(isProcessing) MaterialTheme.colorScheme.primary else Color.White)
            }
            if (!isProcessing) {
                Button(
                    onClick = { onStopClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text("⏹ Stop & Analyse", color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun WaveformBar(targetHeight: Float) {
    val infiniteTransition = rememberInfiniteTransition()
    val height by infiniteTransition.animateFloat(
        initialValue = targetHeight * 0.3f,
        targetValue = targetHeight,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "waveform"
    )

    Box(
        modifier = Modifier
            .width(8.dp)
            .height(height.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
    )
}

@Composable
fun StepperStep(label: String, isComplete: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
        )
        Text(label, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun StepperDivider() {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(2.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 4.dp)
    )
}
