package com.shopiroller.activities

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.app.PictureInPictureUiState
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.LoadEventInfo
import com.google.android.exoplayer2.source.MediaLoadData
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.shopiroller.R
import com.shopiroller.SharedApplication
import com.shopiroller.helpers.ProgressViewHelper
import java.io.IOException

class VideoPlayerActivity : AppCompatActivity() {

    interface Listener {
        fun isNotVideoUrl(videoUrl: String)
    }

    private lateinit var rootView: ConstraintLayout
    private lateinit var playerView: StyledPlayerView
    private lateinit var progressViewHelper: ProgressViewHelper

    private lateinit var player: ExoPlayer
    private lateinit var videoUrl: String

    private var isFullScreen = false
    private var isManuelFullScreen = false

    companion object {
        private var listener: Listener? = null
        private const val ARG_VIDEO_URL = "VideoPlayerActivity.URL"
        var appContext: Context? = null
        fun getVideoPlayerIntent(streamUrl: String, listener: Listener?): Intent {
            this.listener = listener
            return Intent(SharedApplication.context, VideoPlayerActivity::class.java)
                .putExtra(ARG_VIDEO_URL, streamUrl)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = this
        setContentView(R.layout.activity_video_player)

        rootView = findViewById(R.id.activity_video_player_root)
        playerView = findViewById(R.id.simple_exo_player)

        progressViewHelper = ProgressViewHelper(this)
        progressViewHelper.notCancelable()

        if (intent.hasExtra(ARG_VIDEO_URL))
            intent.getStringExtra(ARG_VIDEO_URL)?.let { videoUrl = it } ?: run { finish() }
        else
            finish()

        loadUI()
    }

    private fun loadUI() {
        progressViewHelper.show()

        val dataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, applicationInfo.loadLabel(packageManager).toString())
        )

        player = ExoPlayer.Builder(this).build()
        player.playWhenReady = true

        when (Util.inferContentType(Uri.parse(videoUrl))) {
            C.CONTENT_TYPE_HLS -> {
                player.addMediaSource(
                    HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                        MediaItem.fromUri(
                            Uri.parse(videoUrl)
                        )
                    )
                )
                player.prepare()
            }
            C.CONTENT_TYPE_OTHER -> {
                player.addMediaSource(
                    ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(
                            MediaItem.fromUri(
                                Uri.parse(videoUrl)
                            )
                        )
                )
                player.prepare()
            }
            else -> finish()
        }

        player.addAnalyticsListener(object : AnalyticsListener {
            override fun onIsPlayingChanged(
                eventTime: AnalyticsListener.EventTime,
                isPlaying: Boolean
            ) {
                super.onIsPlayingChanged(eventTime, isPlaying)
                progressViewHelper.dismiss()
            }

            override fun onLoadError(
                eventTime: AnalyticsListener.EventTime,
                loadEventInfo: LoadEventInfo,
                mediaLoadData: MediaLoadData,
                error: IOException,
                wasCanceled: Boolean
            ) {
                progressViewHelper.dismiss()
                player.release()
                listener?.isNotVideoUrl(videoUrl)
                finish()
            }
        })

        playerView.setFullscreenButtonClickListener {
            if (isFullScreen) {
                isManuelFullScreen = false
                configureMinimizedScreen(false)
            } else {
                isManuelFullScreen = true
                configureFullScreen(false)
            }
        }
        playerView.player = player
    }

    private fun configureFullScreen(withSensor: Boolean = true) {
        requestedOrientation = if (withSensor)
            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        else
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
        playerView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        playerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        isFullScreen = true
        hideSystemUI()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun configureMinimizedScreen(withSensor: Boolean = true) {
        requestedOrientation = if (withSensor)
            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        else
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT

        playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
        playerView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        playerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        isFullScreen = false
        showSystemUI()
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, rootView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        // androidx.core:core:1.5.0
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, rootView).show(WindowInsetsCompat.Type.systemBars())
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (isManuelFullScreen)
            return
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            configureFullScreen()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            configureMinimizedScreen()
        }
    }

    override fun onResume() {
        super.onResume()
        player.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        playerView.player = null
        player.release()
    }

    override fun onStop() {
        super.onStop()
        player.playWhenReady = false
    }

}
