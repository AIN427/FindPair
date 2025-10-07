package com.example.findpairgame.ui

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.findpairgame.R
import com.example.findpairgame.ui.components.MenuButton
import com.example.findpairgame.utils.MusicManager
import com.example.findpairgame.utils.SoundManager

@Composable
fun MainMenuScreen(
    onNewGameClick: () -> Unit,
    onGameHistoryClick: () -> Unit,
    onExitClick: () -> Unit
) {
    val context = LocalContext.current
    var isMusicPlaying by remember { mutableStateOf(MusicManager.isMusicPlaying()) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        RadialGradientBackground()

        Image(
            painter = painterResource(
                if (isMusicPlaying) R.drawable.ic_music_on else R.drawable.ic_music_off
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(16.dp)
                .size(48.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    SoundManager.playButtonClick()
                    MusicManager.toggleMusic()
                    isMusicPlaying = MusicManager.isMusicPlaying()
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 48.dp)
            )

            MenuButton(
                text = stringResource(R.string.menu_new_game),
                onClick = {
                    SoundManager.playButtonClick()
                    onNewGameClick()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            MenuButton(
                text = stringResource(R.string.menu_game_history),
                onClick = {
                    SoundManager.playButtonClick()
                    onGameHistoryClick()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            MenuButton(
                text = stringResource(R.string.menu_exit),
                onClick = {
                    SoundManager.playButtonClick()
                    onExitClick()
                }
            )
        }
    }
}
