package com.saveeat.utils.extn

import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

fun NestedScrollView.onScrollStateChanged(startDelay: Long = 100, stopDelay: Long = 400, listener: (Boolean) -> Unit) {
    setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_SCROLL, MotionEvent.ACTION_MOVE -> { handler.postDelayed({ listener.invoke(true) }, startDelay) }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> { handler.postDelayed({ listener.invoke(false) }, stopDelay) }
        }
        false
    }
}