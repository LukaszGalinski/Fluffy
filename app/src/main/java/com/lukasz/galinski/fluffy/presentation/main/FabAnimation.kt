package com.lukasz.galinski.fluffy.presentation.main

import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lukasz.galinski.fluffy.presentation.common.FabAnimationTool

class FabAnimation(val list: List<FloatingActionButton>) {

    private val fabAnimationTool = FabAnimationTool()

    init {
        initFabButtonsView()
    }

    fun showInFabButtons() {
        list.forEach {
            fabAnimationTool.showIn(it)
        }
    }

    fun showOutFabButtons() {
        list.forEach {
            fabAnimationTool.showOut(it)
        }
    }

    private fun initFabButtonsView() {
        list.forEach {
            fabAnimationTool.init(it)
        }
    }

    fun setFabAnimation(view: View, isViewRotated: Boolean) =
         fabAnimationTool.rotateFab(view, isViewRotated)
}