package com.example.findpairgame.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.findpairgame.R

object SoundManager {
    private var soundPool: SoundPool? = null
    private var buttonClickSoundId: Int = 0
    private var victorySoundId: Int = 0
    private var looseSoundId: Int = 0

    fun init(context: Context) {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        buttonClickSoundId = soundPool?.load(context, R.raw.btn_click_sound, 1) ?: 0
        victorySoundId = soundPool?.load(context, R.raw.sound_victory, 1) ?: 0
        looseSoundId = soundPool?.load(context, R.raw.sound_loose, 1) ?: 0
    }

    fun playButtonClick() {
        soundPool?.play(buttonClickSoundId, 0.7f, 0.7f, 0, 0, 1f)
    }

    fun playVictory() {
        soundPool?.play(victorySoundId, 0.8f, 0.8f, 1, 0, 1f)
    }

    fun playLoose() {
        soundPool?.play(looseSoundId, 0.8f, 0.8f, 1, 0, 1f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
