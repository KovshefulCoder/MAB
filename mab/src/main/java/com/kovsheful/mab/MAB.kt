package com.kovsheful.mab

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun MAB(
    mabController: MabController,
    modifier: Modifier = Modifier,
    paddingFromEdges: Dp = 16.dp,
    size: Dp = 72.dp,
    backgroundColor: Color = Color.Blue,
    shape: Shape = CircleShape,
    content: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "MAB",
            tint = Color.White
        )
    }
) {
    val deltaXFromInitPosition = remember { mutableStateOf(0f) }
    val deltaYFromInitPosition = remember { mutableStateOf(0f) }
    FloatingActionButton(
        onClick = { mabController.onMabClicked() },
        modifier = modifier
            .padding(paddingFromEdges)
            .size(size)
            .offset {// set position on the screen
                IntOffset(
                    deltaXFromInitPosition.value.roundToInt(),
                    deltaYFromInitPosition.value.roundToInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {//start dragging mab
                        mabController.onMabStart()
                    },
                    onDragEnd = { // end dragging mab, return to init position
                        deltaXFromInitPosition.value = 0f
                        deltaYFromInitPosition.value = 0f
                        mabController.onMabEnd()
                    },
                ) { change, dragAmount ->
                    change.consume()
                    // < 0 to prevent dragging outside right and bottom edges of the screen
                    if (deltaXFromInitPosition.value + dragAmount.x < 0)
                        deltaXFromInitPosition.value += dragAmount.x
                    if (deltaYFromInitPosition.value + dragAmount.y < 0)
                        deltaYFromInitPosition.value += dragAmount.y
                }
            }
            .onGloballyPositioned { coordinates -> //react on mab position change and transmit it to controller
                mabController.onMabCoordinatesChanged(
                    coordinates.positionInRoot().x + (coordinates.size.width / 2),
                    coordinates.positionInRoot().y + (coordinates.size.height / 2)
                )
            },
        shape = shape,
        containerColor = backgroundColor,
        content = content
    )
}