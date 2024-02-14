package com.example.mixcolors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mixcolors.ui.theme.MixColorsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MixColorsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Mixing()
                }
            }
        }
    }
}

class MixingViewModel : ViewModel() {
    val colorOne = mutableStateOf(Color.Red)
    val colorTwo = mutableStateOf(Color.Green)
    val colorThree = mutableStateOf(Color.Blue)
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun Mixing() {
    val viewModel: MixingViewModel = viewModel()

    val colors = listOf(
        Color.Black,
        Color.DarkGray,
        Color.Gray,
        Color.LightGray,
        Color.White,
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Cyan,
        Color.Magenta,
        Color.Transparent
    )

    val colorNames = colors.map { color ->
        when (color) {
            Color.Black -> "Black"
            Color.DarkGray -> "DarkGray"
            Color.Gray -> "Gray"
            Color.LightGray -> "LightGray"
            Color.White -> "White"
            Color.Red -> "Red"
            Color.Green -> "Green"
            Color.Blue -> "Blue"
            Color.Yellow -> "Yellow"
            Color.Cyan -> "Cyan"
            Color.Magenta -> "Magenta"
            else -> "Transparent"
        }
    }

    var toggledOne by remember { mutableStateOf(false) }
    val animatedPaddingOne by animateDpAsState(
        if (toggledOne) 20.dp else 0.dp,
        label = "padding"
    )

    var toggledTwo by remember { mutableStateOf(false) }
    val animatedPaddingTwo by animateDpAsState(
        if (toggledTwo) 20.dp else 0.dp,
        label = "padding"
    )

    var toggledThree by remember { mutableStateOf(false) }
    val animatedPaddingThree by animateDpAsState(
        if (toggledThree) 20.dp else 0.dp,
        label = "padding"
    )

    var expandedColorOne by remember { mutableStateOf(false) }
    var expandedColorTwo by remember { mutableStateOf(false) }
    var expandedColorThree by remember { mutableStateOf(false) }

    val colorOne by viewModel.colorOne
    val colorTwo by viewModel.colorTwo
    val colorThree by viewModel.colorThree
    val mixedColor = remember(colorOne, colorTwo, colorThree) {
        Color(
            red = (colorOne.red + colorTwo.red + colorThree.red) / 3f,
            green = (colorOne.green + colorTwo.green + colorThree.green) / 3f,
            blue = (colorOne.blue + colorTwo.blue + colorThree.blue) / 3f,
        )
    }

    LaunchedEffect(colorOne, colorTwo, colorThree) {
        viewModel.colorOne.value = colorOne
        viewModel.colorTwo.value = colorTwo
        viewModel.colorThree.value = colorThree
    }

    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = mixedColor,
        targetValue = mixedColor,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "color"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${colorNames[colors.indexOf(colorOne)]}")
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(animatedPaddingOne)
                    .fillMaxWidth(fraction = 0.4f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        toggledOne = !toggledOne
                        expandedColorOne = true
                    }
                    .clip(shape = RoundedCornerShape(6.dp))
                    .border(
                        BorderStroke(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    .background(colorOne)
            ) {
                DropdownMenu(
                    expanded = expandedColorOne,
                    onDismissRequest = {
                        expandedColorOne = false
                        toggledOne = false
                    },
                ) {
                    colors.forEach { color ->
                        DropdownMenuItem(
                            text = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(color)
                                )
                            },
                            onClick = {
                                viewModel.colorOne.value = color
                                expandedColorOne = false
                                toggledOne = false
                            }
                        )
                    }
                }
            }
        }

        Text(
            text = "+",
            fontSize = 30.sp,
        )

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${colorNames[colors.indexOf(colorTwo)]}")
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(animatedPaddingTwo)
                    .fillMaxWidth(fraction = 0.4f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        toggledTwo = !toggledTwo
                        expandedColorTwo = true
                    }
                    .clip(shape = RoundedCornerShape(6.dp))
                    .border(
                        BorderStroke(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    .background(colorTwo)
            ) {
                DropdownMenu(
                    expanded = expandedColorTwo,
                    onDismissRequest = {
                        expandedColorTwo = false
                        toggledTwo = false
                    },
                ) {
                    colors.forEach { color ->
                        DropdownMenuItem(
                            text = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(color)
                                )
                            },
                            onClick = {
                                viewModel.colorTwo.value = color
                                expandedColorTwo = false
                                toggledTwo = false
                            }
                        )
                    }
                }
            }
        }

        Text(
            text = "+",
            fontSize = 30.sp
        )

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${colorNames[colors.indexOf(colorThree)]}")
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(animatedPaddingThree)
                    .fillMaxWidth(fraction = 0.4f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        toggledThree = !toggledThree
                        expandedColorThree = true
                    }
                    .clip(shape = RoundedCornerShape(6.dp))
                    .border(
                        BorderStroke(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    .background(colorThree)
            ) {
                DropdownMenu(
                    expanded = expandedColorThree,
                    onDismissRequest = {
                        expandedColorThree = false
                        toggledThree = false
                    },
                ) {
                    colors.forEach { color ->
                        DropdownMenuItem(
                            text = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(color)
                                )
                            },
                            onClick = {
                                viewModel.colorThree.value = color
                                expandedColorThree = false
                                toggledThree = false
                            }
                        )
                    }
                }
            }
        }

        Text(
            text = "=",
            fontSize = 30.sp
        )

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hex: #${mixedColor.value.toHexString
                    (format = HexFormat.Default).dropLast(8)}"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .fillMaxWidth(fraction = 0.4f)
                    .clip(shape = RoundedCornerShape(6.dp))
                    .border(
                        BorderStroke(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    .background(animatedColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MixingPreview() {
    MixColorsTheme {
        Mixing()
    }
}