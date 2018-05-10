package com.elvotra.clean.presentation.ui.widgets

import android.content.Context
import android.content.res.TypedArray
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

import com.elvotra.clean.R

class AvatarImageBehavior(private val context: Context, attrs: AttributeSet?) : CoordinatorLayout.Behavior<ImageView>() {

    private var customFinalYPosition: Float = 0.toFloat()
    private var customFinalHeight: Float = 0.toFloat()

    private var startXPosition: Int = 0
    private var startToolbarPosition: Float = 0.toFloat()
    private var startYPosition: Int = 0
    private var finalYPosition: Int = 0
    private var startHeight: Int = 0
    private var finalXPosition: Int = 0
    private var changeBehaviorPoint: Float = 0.toFloat()

    init {

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageBehavior)
            customFinalYPosition = a.getDimension(R.styleable.AvatarImageBehavior_finalYPosition, 0f)
            customFinalHeight = a.getDimension(R.styleable.AvatarImageBehavior_finalHeight, 0f)

            a.recycle()
        }
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: ImageView?, dependency: View?): Boolean {
        return dependency is Toolbar
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: ImageView?, dependency: View?): Boolean {
        maybeInitProperties(child, dependency)

        val maxScrollDistance = startToolbarPosition.toInt()
        val expandedPercentageFactor = dependency!!.y / maxScrollDistance

        if (expandedPercentageFactor < changeBehaviorPoint) {
            val heightFactor = (changeBehaviorPoint - expandedPercentageFactor) / changeBehaviorPoint

            val distanceXToSubtract = (startXPosition - finalXPosition) * heightFactor + child!!.height / 2
            val distanceYToSubtract = (startYPosition - finalYPosition) * (1f - expandedPercentageFactor) + child.height / 2

            child.x = startXPosition - distanceXToSubtract
            child.y = startYPosition - distanceYToSubtract

            val heightToSubtract = (startHeight - customFinalHeight) * heightFactor

            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
            lp.width = (startHeight - heightToSubtract).toInt()
            lp.height = (startHeight - heightToSubtract).toInt()
            child.layoutParams = lp
        } else {
            val distanceYToSubtract = (startYPosition - finalYPosition) * (1f - expandedPercentageFactor) + startHeight / 2

            child!!.x = (startXPosition - child.width / 2).toFloat()
            child.y = startYPosition - distanceYToSubtract

            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
            lp.width = startHeight
            lp.height = startHeight
            child.layoutParams = lp
        }
        return true
    }

    private fun maybeInitProperties(child: ImageView?, dependency: View?) {
        if (startYPosition == 0)
            startYPosition = dependency!!.y.toInt()

        if (finalYPosition == 0)
            finalYPosition = dependency!!.height / 2

        if (startHeight == 0)
            startHeight = child!!.height

        if (startXPosition == 0)
            startXPosition = (child!!.x + child.width / 2).toInt()

        if (finalXPosition == 0)
            finalXPosition = context.resources.getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + customFinalHeight.toInt() / 2 + customFinalYPosition.toInt()

        if (startToolbarPosition == 0f)
            startToolbarPosition = dependency!!.y

        if (changeBehaviorPoint == 0f) {
            changeBehaviorPoint = (child!!.height - customFinalHeight) / (2f * (startYPosition - finalYPosition))
        }
    }
}