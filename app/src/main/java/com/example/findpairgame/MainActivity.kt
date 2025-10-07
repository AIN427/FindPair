package com.example.findpairgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.example.findpairgame.navigation.AppNavigation
import com.example.findpairgame.ui.theme.FindPairGameTheme
import com.example.findpairgame.utils.MusicManager
import com.example.findpairgame.utils.SoundManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        MusicManager.start(this)
        SoundManager.init(this)
        setContent {
            FindPairGameTheme {
                val view = window.decorView
                androidx.compose.runtime.SideEffect {
                    window.statusBarColor = android.graphics.Color.TRANSPARENT
                    window.navigationBarColor = android.graphics.Color.TRANSPARENT
                    WindowCompat.getInsetsController(window, view)?.apply {
                        isAppearanceLightStatusBars = true
                        isAppearanceLightNavigationBars = true
                    }
                }

                AppNavigation()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        MusicManager.pause()
    }

    override fun onResume() {
        super.onResume()
        MusicManager.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicManager.release()
        SoundManager.release()
    }
}