package com.example.findpairgame.ui

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.findpairgame.R
import com.example.findpairgame.model.Card
import com.example.findpairgame.ui.components.BackButton
import com.example.findpairgame.ui.components.CardItem
import com.example.findpairgame.ui.components.GameOverDialog
import com.example.findpairgame.ui.components.MenuButton
import com.example.findpairgame.utils.SoundManager
import com.example.findpairgame.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    onNavigateBack: () -> Unit,
    viewModel: GameViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    val vibrationEvent by viewModel.vibrationEvent.collectAsState()
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    LaunchedEffect(vibrationEvent) {
        if (vibrationEvent > 0) {
            val vibrator = getSystemService(context, Vibrator::class.java)
            vibrator?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    it.vibrate(100)
                }
            }
        }
    }

    GameScreenPortrait(
        gameState = gameState,
        onNavigateBack = onNavigateBack,
        onCardClick = { viewModel.onCardClick(it) },
        onNewGame = { viewModel.initGame() }
    )

    if (gameState.isGameOver) {
        GameOverDialog(
            isWon = gameState.isWon,
            pairsFound = gameState.pairsFound,
            onNewGame = { viewModel.initGame() },
            onNavigateBack = onNavigateBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreenPortrait(
    gameState: com.example.findpairgame.model.GameState,
    onNavigateBack: () -> Unit,
    onCardClick: (Card) -> Unit,
    onNewGame: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        RadialGradientBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = dimensionResource(R.dimen.screen_padding)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.pairs_found, gameState.pairsFound),
                    fontSize = dimensionResource(R.dimen.counter_text_size).value.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) { index ->
                        Text(
                            text = if (index < gameState.livesLeft) "❤️" else "🖤",
                            fontSize = 36.sp
                        )
                    }
                }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(dimensionResource(R.dimen.card_spacing)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.card_spacing)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.card_spacing)),
                modifier = Modifier.wrapContentHeight()
            ) {
                items(gameState.cards) { card ->
                    CardItem(
                        card = card,
                        onClick = { onCardClick(card) }
                    )
                }
            }
        }

                Spacer(modifier = Modifier.height(16.dp))

                MenuButton(
                    text = stringResource(R.string.restart_game),
                    onClick = {
                        SoundManager.playButtonClick()
                        onNewGame()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

        BackButton(
            onClick = {
                SoundManager.playButtonClick()
                onNavigateBack()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(16.dp)
        )
    }
}

