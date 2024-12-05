package com.knthcame.myhealthkmp.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import kotlinx.datetime.Instant
import myhealthkmp.composeapp.generated.resources.Res
import myhealthkmp.composeapp.generated.resources.dashboard_heart_rate_card_title
import myhealthkmp.composeapp.generated.resources.dashboard_title
import myhealthkmp.composeapp.generated.resources.heart_rate
import myhealthkmp.composeapp.generated.resources.placeholder
import myhealthkmp.composeapp.generated.resources.unit_beats_per_minute
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreenRoute(viewModel: DashboardViewModel = koinViewModel()) {
    val heartRateUiState by viewModel.heartRateState.collectAsState()

    DashboardScreen(heartRateUiState = heartRateUiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(heartRateUiState: HeartRateUiState) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(Res.string.dashboard_title))
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            HeartRateCard(
                heartRateUiState = heartRateUiState,
            )
        }
    }
}

@Composable
private fun HeartRateCard(
    modifier: Modifier = Modifier,
    heartRateUiState: HeartRateUiState,
) {
    val crossFadeAnimationDuration = 600
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HeartRateCardHeader(
                heartRateUiState = heartRateUiState,
                crossFadeAnimationSpec = tween(crossFadeAnimationDuration),
            )

            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                HeartRateCardValue(
                    modifier = Modifier.weight(0.5f),
                    heartRateUiState = heartRateUiState,
                    crossFadeAnimationSpec = tween(crossFadeAnimationDuration),
                )

                AnimatedVisibility(
                    visible = heartRateUiState is HeartRateUiState.Available,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.weight(0.5f)
                ) {
                    HeartRateCardGraph(
                        modifier = Modifier.fillMaxSize(),
                        values = (heartRateUiState as HeartRateUiState.Available).graphValues,
                    )
                }
            }
        }
    }
}

@Composable
private fun HeartRateCardHeader(
    modifier: Modifier = Modifier,
    heartRateUiState: HeartRateUiState,
    crossFadeAnimationSpec: FiniteAnimationSpec<Float> = tween(),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val pulseTransition = rememberInfiniteTransition()

        val pulseScale by pulseTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0.8f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse,
            )
        )

        Icon(
            painter = painterResource(Res.drawable.heart_rate),
            contentDescription = "Heart rate icon",
            tint = Color.Red,
            modifier = Modifier.size(20.dp).graphicsLayer {
                scaleX = pulseScale
                scaleY = pulseScale
            },
        )
        Text(
            text = stringResource(Res.string.dashboard_heart_rate_card_title),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.weight(1f))

        AnimatedVisibility(visible = heartRateUiState is HeartRateUiState.Available) {
            Crossfade(
                targetState = (heartRateUiState as HeartRateUiState.Available).timeStamp,
                animationSpec = crossFadeAnimationSpec,
            ) { targetState ->
                Text(
                    text = targetState,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun HeartRateCardValue(
    modifier: Modifier = Modifier,
    heartRateUiState: HeartRateUiState,
    crossFadeAnimationSpec: FiniteAnimationSpec<Float> = tween(),
) {
    val bpmText: String = when (heartRateUiState) {
        is HeartRateUiState.Available -> heartRateUiState.value.toString()
        HeartRateUiState.Missing -> stringResource(Res.string.placeholder)
    }
    Crossfade(
        targetState = bpmText,
        animationSpec = crossFadeAnimationSpec,
        modifier = modifier,
    ) { targetState ->
        Text(buildAnnotatedString {
            withStyle(
                SpanStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            ) {
                append(targetState)
            }
            append(" ")
            withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                append(stringResource(Res.string.unit_beats_per_minute))
            }
        })
    }
}

@Composable
fun HeartRateCardGraph(modifier: Modifier, values: List<HeartRate>) {
    val minInstant = remember(values) { values.minOf { item -> item.timeStamp } }
    val maxInstant = remember(values) { values.maxOf { item -> item.timeStamp } }
    val maxValue = 130
    val minValue = 60

    Canvas(modifier = modifier, onDraw = {
        /** Calculates the horizontal offset from the start. */
        fun calculateX(timeStamp: Instant): Float {
            val totalWidth = (maxInstant - minInstant).inWholeMicroseconds
            val localWidth = (timeStamp - minInstant).inWholeMicroseconds
            val percentage = localWidth.toFloat() / totalWidth

            return size.width * percentage
        }

        /** Calculates the vertical offset from the top. */
        fun calculateY(bpm: Double): Float {
            val totalHeight = maxValue - minValue
            val localHeight = maxValue - bpm
            val percentage = localHeight.toFloat() / totalHeight

            return size.height * percentage
        }

        val path = Path()
        val firstValue = values[0]
        path.moveTo(x = calculateX(firstValue.timeStamp), y = calculateY(firstValue.bpm))

        for (i in 1..values.lastIndex) {
            val value = values[i]
            path.lineTo(x = calculateX(value.timeStamp), y = calculateY(value.bpm))
        }

        drawPath(
            path = path,
            brush = SolidColor(Color.Red),
            style = Stroke(
                width = 2.dp.toPx(),
                pathEffect = PathEffect.cornerPathEffect(10.dp.toPx())
            ),
        )
    })
}