package com.jaikeerthick.composable_graphs.charts.common

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumeAllChanges
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SwipeGestureRecognizer(
    private val coroutineScope: CoroutineScope
) {

    private val _swipeEvents = MutableSharedFlow<SwipeDirection>()
    val swipeEvents = _swipeEvents.asSharedFlow()

    private var currentDirection = SwipeDirection.RIGHT

    private var gestureDetectionJob: Job? = null

    lateinit var pointerInputScope: PointerInputScope

    fun turnOn() {
        gestureDetectionJob?.cancel()
        gestureDetectionJob = coroutineScope.launch {
            with(pointerInputScope) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            _swipeEvents.emit(currentDirection)
                        }
                    }
                ) { change, dragAmount ->

                    change.consumeAllChanges()
                    currentDirection = if (dragAmount > 0) SwipeDirection.RIGHT
                    else SwipeDirection.LEFT
                    Log.d("gesture", "swipe changed")

                }
            }
        }
    }

    fun turnOff() {
        gestureDetectionJob?.cancel()
    }

}

enum class SwipeDirection { LEFT, RIGHT}