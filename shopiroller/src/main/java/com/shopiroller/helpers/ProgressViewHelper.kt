package com.shopiroller.helpers

import android.R
import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.*
import com.shopiroller.Shopiroller

/**
 * Created by ealtaca on 27.12.2016.
 */
class ProgressViewHelper {
    private var progressBar: ProgressBar? = null
    private var rl: RelativeLayout? = null
    private var progressType: Int
    private var progressColor: Int

    constructor(context: Activity, progressType: Int, progressColor: Int) {
        this.progressColor = progressColor
        this.progressType = progressType
        setProgressBar(context, Gravity.CENTER)
    }

    @JvmOverloads
    constructor(context: Activity, gravity: Int = Gravity.CENTER) {
        progressColor = Shopiroller.getTheme().progressColor
        progressType = Shopiroller.getTheme().progressType
        setProgressBar(context, gravity)
    }

    fun show() {
        rl!!.visibility = View.VISIBLE
        progressBar!!.visibility = View.VISIBLE
    }

    fun dismiss() {
        rl!!.visibility = View.GONE
        progressBar!!.visibility = View.GONE
    }

    fun cancel() {
        rl!!.visibility = View.GONE
        progressBar!!.visibility = View.GONE
    }

    fun notCancelable() {
        if (rl != null) rl!!.setOnClickListener { }
    }

    val progressDialog: ProgressBar?
        get() = if (progressBar != null) progressBar else null
    val isShowing: Boolean
        get() = progressBar!!.visibility == View.VISIBLE


    val progressDrawable: Drawable
        get() {
            val progressDrawable: Sprite = when (progressType) {
                1 -> RotatingPlane()
                2 -> DoubleBounce()
                3 -> Wave()
                4 -> WanderingCubes()
                5 -> Pulse()
                6 -> ChasingDots()
                7 -> ThreeBounce()
                8 -> Circle()
                9 -> CubeGrid()
                10 -> FadingCircle()
                11 -> FoldingCube()
                12 -> RotatingCircle()
                else -> Circle()
            }
            progressDrawable.color = progressColor
            return progressDrawable
        }

    private fun setProgressBar(context: Activity, gravity: Int) {
        val layout = context.findViewById<View>(R.id.content).rootView as ViewGroup
        progressBar = ProgressBar(context, null, R.attr.progressBarStyleLarge)
        progressBar!!.isIndeterminate = true
        progressBar!!.indeterminateDrawable = progressDrawable
        progressBar!!.layoutParams = RelativeLayout.LayoutParams(150, 150)
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        rl = RelativeLayout(context)
        rl!!.setOnClickListener { dismiss() }
        rl!!.gravity = gravity or Gravity.CENTER_HORIZONTAL
        rl!!.addView(progressBar)
        layout.addView(rl, params)
        dismiss()
    }
}