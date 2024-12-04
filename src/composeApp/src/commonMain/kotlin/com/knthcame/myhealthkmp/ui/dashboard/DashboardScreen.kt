package com.knthcame.myhealthkmp.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

                if (heartRateUiState is HeartRateUiState.Available)
                    Text(
                        text = heartRateUiState.timeStamp,
                        style = MaterialTheme.typography.bodyMedium,
                    )
            }

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
    }
}