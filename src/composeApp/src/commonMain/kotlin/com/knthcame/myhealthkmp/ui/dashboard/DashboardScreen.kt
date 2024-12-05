package com.knthcame.myhealthkmp.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.sizeIn
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
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HeartRateCardHeader(heartRateUiState)

            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                HeartRateCardValue(heartRateUiState)
                Spacer(Modifier.sizeIn(minWidth = 16.dp).weight(1f))
                AnimatedVisibility(heartRateUiState is HeartRateUiState.Available) {
                    HeartRateCardGraph(
                        modifier = Modifier.fillMaxWidth(fraction = 0.8f).fillMaxHeight(),
                        values = (heartRateUiState as HeartRateUiState.Available).graphValues,
                    )
                }
            }
        }
    }
}

@Composable
private fun HeartRateCardHeader(heartRateUiState: HeartRateUiState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.heart_rate),
            contentDescription = "Heart rate icon",
            tint = Color.Red,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = stringResource(Res.string.dashboard_heart_rate_card_title),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.weight(1f))

        AnimatedVisibility(visible = heartRateUiState is HeartRateUiState.Available) {
            Text(
                text = (heartRateUiState as HeartRateUiState.Available).timeStamp,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun HeartRateCardValue(heartRateUiState: HeartRateUiState) {
    val bpmValue = when (heartRateUiState) {
        is HeartRateUiState.Available -> heartRateUiState.value
        HeartRateUiState.Missing -> stringResource(Res.string.placeholder)
    }
    Text(buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
            )
        ) {
            append(bpmValue)
        }
        append(" ")
        withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold)) {
            append(stringResource(Res.string.unit_beats_per_minute))
        }
    })
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