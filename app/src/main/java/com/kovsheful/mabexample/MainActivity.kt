package com.kovsheful.mabexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.kovsheful.mab.MAB
import com.kovsheful.mab.rememberMabController
import com.kovsheful.mabexample.ui.theme.MabExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MabExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        val mabController = rememberMabController()
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Element ${mabController.indexOfElementBelow}",
                            )
                            if (mabController.finalIndex != -1) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Final element: ${mabController.finalIndex}",
                                    )
                                    Button(
                                        onClick = {
                                            mabController.resetFinalIndex()
                                        }
                                    ) {
                                        Text(
                                            text = "Reset final element",
                                        )
                                    }
                                }
                            }
                            LazyColumn(
                                modifier = Modifier
                                    .padding(32.dp)
                                    .onGloballyPositioned { coordinates ->
                                        mabController.onListCoordinatesChanged(coordinates)
                                    },
                                content = {
                                    items(30) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colorScheme.secondary)
                                                .onGloballyPositioned { coordinates ->
                                                    mabController.onListElementCoordinatesChanged(
                                                        it, coordinates
                                                    )
                                                }
                                        ) {
                                            Text(
                                                text = "Item $it",
                                                modifier = Modifier.padding(8.dp),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color.Black
                                            )
                                        }
                                    }
                                }
                            )
                        }
                        MAB(
                            modifier = Modifier.align(Alignment.BottomEnd),
                            mabController = mabController
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "FAB",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
