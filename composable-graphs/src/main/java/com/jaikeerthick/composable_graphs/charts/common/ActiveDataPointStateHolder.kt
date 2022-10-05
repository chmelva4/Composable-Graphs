package com.jaikeerthick.composable_graphs.charts.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActiveDataPointStateHolder(
    private val dataSize: Int,
    private val scope: CoroutineScope
) {

    val gestureRecognizer = SwipeGestureRecognizer(scope)

    private val _activeItemFlow = MutableStateFlow<Int?>(null)
    private var activeItem = 0
    val activeItemFlow = _activeItemFlow.asStateFlow()

    private val _onFocusOutEvent = MutableSharedFlow<FocusDirection>()
    val onFocusOutEvent = _onFocusOutEvent.asSharedFlow()

    private var gestureDetectorJob: Job? = null

    init {

    }

    fun focusIn(direction: FocusDirection) {
        if (gestureDetectorJob != null) return

        scope.launch {
             activeItem =  when (direction) {
                FocusDirection.FRONT -> 0
                FocusDirection.BACK -> dataSize - 1
            }
            _activeItemFlow.emit(activeItem)
        }
        hookupGestureDetector()
        gestureRecognizer.turnOn()
    }

    private fun hookupGestureDetector() {
        gestureDetectorJob = scope.launch {
             gestureRecognizer.swipeEvents.collect {
                 when (it) {
                     SwipeDirection.LEFT -> {
                         if (activeItem > 0) {
                             activeItem--
                             _activeItemFlow.emit(activeItem)
                         } else {
                             _onFocusOutEvent.emit(FocusDirection.FRONT)
                             cleanup()
                         }
                     }
                     SwipeDirection.RIGHT -> {
                         if (activeItem < dataSize - 1) {
                             activeItem++
                             _activeItemFlow.emit(activeItem)
                         } else {
                             _onFocusOutEvent.emit(FocusDirection.BACK)
                             cleanup()
                         }
                     }
                 }
             }
        }
    }

    private fun cleanup() {
        gestureDetectorJob?.cancel()
        gestureDetectorJob = null
        gestureRecognizer.turnOff()
        scope.launch { _activeItemFlow.emit(null) }
    }
}

enum class FocusDirection {FRONT, BACK}

@Composable
fun rememberActiveDataPointStateHolder(
    dataSize: Int,
    coroutineScope: CoroutineScope
) = remember {
    ActiveDataPointStateHolder(dataSize, coroutineScope)
}