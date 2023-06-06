package com.kovsheful.mab

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntSize

class MabController {

    private val _status = mutableStateOf(MabStatus.IDLE)
    val status get() = _status.value
    private val _indexOfElementBelow = mutableStateOf(-1) // index of list element below the mab
    val indexOfElementBelow get() = _indexOfElementBelow.value
    private val _finalIndex = mutableStateOf(-1) // final index that sets when onDragEnd
    val finalIndex get() = _finalIndex.value

    private var visibleListElementsCoordinates = mutableMapOf<Int, Pair<Float, Float>>() //index, (x, y)
    private var listState = ListMeasures()


    fun onMabCoordinatesChanged(xMab: Float, yMab: Float) {
        if (status == MabStatus.IDLE || status == MabStatus.ENDED) {
            return
        }
        if ( // check if mab inside list
            yMab > listState.listTopYCoordinate && // below Y top
            yMab < listState.listTopYCoordinate + listState.size.height && // above Y bottom
            xMab > listState.listTopXCoordinate && // To the right of the left edge
            xMab < listState.listTopXCoordinate + listState.size.width //To the left of the right edge of list
        ) { //check if the mab is inside the list
            if (visibleListElementsCoordinates.isEmpty()) {
                _indexOfElementBelow.value = -1
                return
            }
            visibleListElementsCoordinates.forEach { (index, elemCoordinates) -> // check all visible element in order if mab above one of them
                if (
                    yMab > elemCoordinates.second && // below Y top
                    yMab < (visibleListElementsCoordinates[index + 1]?.second // above Y bottom
                        ?: (listState.listTopYCoordinate + listState.size.height)) && // if the next element is not visible, then the bottom of the list used
                    xMab > elemCoordinates.first && // To the right of the left edge
                    xMab < (elemCoordinates.first + listState.size.width) //To the left of the right edge of list
                ) {
                    _indexOfElementBelow.value = index
                    return
                }
            }
        }
        if (_indexOfElementBelow.value != -1) { // if we were on the list before and now we are not
            _indexOfElementBelow.value = -1
        }
    }

    fun onListElementCoordinatesChanged(index: Int, coordinates: LayoutCoordinates) { // react to scrolling
        if (coordinates.positionInRoot().y >= listState.listTopYCoordinate &&
            coordinates.positionInRoot().y + coordinates.size.height <=
            listState.listTopYCoordinate + listState.size.height
        ) { // If the elements are visible and not scrolled outside of the list coordinates, then update them
            visibleListElementsCoordinates[index] =
                Pair(coordinates.positionInRoot().x, coordinates.positionInRoot().y)
        } else if (visibleListElementsCoordinates.containsKey(index)) { // If they were scrolled outside, then delete them
            visibleListElementsCoordinates.remove(index)
        }
    }

    fun onListCoordinatesChanged(coordinates: LayoutCoordinates) { // set list coordinated and size
        listState = ListMeasures(
            listTopXCoordinate = coordinates.positionInRoot().x,
            listTopYCoordinate = coordinates.positionInRoot().y,
            size = coordinates.size
        )
    }

    fun resetFinalIndex() {
        _finalIndex.value = -1
    }

    fun onMabStart() = run { _status.value = MabStatus.MOVING }
    fun onMabClicked() = run {
        _status.value = MabStatus.CLICKED
    }
    fun onMabEnd() {
        _finalIndex.value = _indexOfElementBelow.value
        _indexOfElementBelow.value = -1
        _status.value = MabStatus.ENDED
    }
}

data class ListMeasures(
    val listTopXCoordinate: Float = 0f,
    val listTopYCoordinate: Float = 0f,
    val size: IntSize = IntSize(0, 0)
)

@Composable
fun rememberMabController(): MabController {
    return remember {
        MabController()
    }
}
