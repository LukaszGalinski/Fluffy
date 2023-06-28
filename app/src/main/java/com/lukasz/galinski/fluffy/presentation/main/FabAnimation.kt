package com.lukasz.galinski.fluffy.presentation.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.lukasz.galinski.fluffy.presentation.common.setGone
import com.lukasz.galinski.fluffy.presentation.common.setVisible

private const val ROTATE_DURATION = 200L
private const val ROTATE_ANGLE = 270f
private const val DEFAULT_ANGLE = 0f
private const val OFF_VALUE = 0f
private const val ON_VALUE = 1f

class FabAnimation {
    fun rotateFab(v: View, rotate: Boolean): Boolean {
        v.animate().setDuration(ROTATE_DURATION)
            .rotation(if (rotate) ROTATE_ANGLE else DEFAULT_ANGLE)
        return rotate
    }

    fun showIn(v: View) {
        v.setVisible()
        v.alpha = OFF_VALUE
        v.translationY = v.height.toFloat()
        v.animate()
            .setDuration(ROTATE_DURATION)
            .translationY(OFF_VALUE)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(ON_VALUE)
            .start()
    }

    fun showOut(v: View) {
        v.alpha = ON_VALUE
        v.translationY = OFF_VALUE
        v.animate()
            .setDuration(ROTATE_DURATION)
            .translationY(v.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    v.setGone()
                    super.onAnimationEnd(animation)
                }
            }).alpha(OFF_VALUE)
            .start()
    }

    fun init(v: View) {
        v.setGone()
        v.translationY = v.height.toFloat()
        v.alpha = OFF_VALUE
    }
}