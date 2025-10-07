package com.example.findpairgame.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.example.findpairgame.R

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isMusicEnabled = true
    private const val TAG = "MusicManager"

    fun start(context: Context) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .build()
                    )
                    val afd = context.resources.openRawResourceFd(R.raw.background_music)
                    setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                    afd.close()
                    isLooping = true
                    setVolume(0.5f, 0.5f)
                    setOnErrorListener { mp, what, extra ->
                        Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")
                        false
                    }
                    setOnCompletionListener {
                        Log.d(TAG, "MediaPlayer completed")
                    }
                    prepare()
                }
            }
            if (isMusicEnabled) {
                mediaPlayer?.start()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error starting music", e)
        }
    }

    fun pause() {
        try {
            mediaPlayer?.pause()
        } catch (e: Exception) {
            Log.e(TAG, "Error pausing music", e)
        }
    }

    fun resume() {
        try {
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e(TAG, "Error resuming music", e)
        }
    }

    fun release() {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing music", e)
        }
    }

    fun toggleMusic() {
        isMusicEnabled = !isMusicEnabled
        if (isMusicEnabled) {
            resume()
        } else {
            pause()
        }
    }

    fun isMusicPlaying(): Boolean = isMusicEnabled
}
