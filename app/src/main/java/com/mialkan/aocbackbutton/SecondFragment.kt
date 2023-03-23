package com.mialkan.aocbackbutton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BackButtonScreen(findNavController())
            }
        }
    }
}

@Composable
fun BackButtonScreen(navController: NavController) {
    var showLoadingDuration by remember { mutableStateOf(1500) }
    var showLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = showLoading) {
        if (showLoading) {
            delay(showLoadingDuration.toLong())
            showLoading = false
        }
    }
    AOCBackButtonTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Back Button Focus Issue",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.semantics { heading() }
                    )
                }, navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Navigate Up"
                            )
                        }
                    })
            },
            bottomBar = {
                if (!showLoading) {
                    BottomAppBar(backgroundColor = Color.White) {
                        Button(onClick = { }, modifier = Modifier.weight(1f)) {
                            Text("Save")
                        }
                    }
                }
            }

        ) { paddingValues ->
            if (showLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colors.primary)
                }
            } else {
                Box(modifier = Modifier.padding(paddingValues)) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                            text = "How long did you sleep?",
                            style = MaterialTheme.typography.h6
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                            TextField(
                                value = "",
                                onValueChange = {},
                                modifier = Modifier.weight(1f),
                                label = { Text("Date?") }
                            )
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                            TextField(
                                value = "",
                                onValueChange = {},
                                modifier = Modifier.weight(1f),
                                label = { Text("When did you wake sleep?") }
                            )
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                            TextField(
                                value = "",
                                onValueChange = {},
                                modifier = Modifier.weight(1f),
                                label = { Text("When did you wake up?") }
                            )
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                            text = "How did you sleep?",
                            style = MaterialTheme.typography.h6
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                            AppSliderContainer()
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                        Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                            TextField(
                                value = "",
                                onValueChange = {},
                                modifier = Modifier.weight(1f),
                                minLines = 3,
                                maxLines = 3,
                                label = { Text("When did you wake up?") }
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AppSliderContainer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppSlider(
            value = 2f,
            valueRange = 0f..4f,
            stepSize = 1f,
            labelFormatter = listOf("Terrible", "Bad", "Average", "Good", "Excellent"),
            onValueChange = {
            }
        )
        Text(
            text = "Average",
            modifier = Modifier.padding(top = 12.dp)
                .clearAndSetSemantics {
                    testTag = "Average"
                },
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
fun AppSlider(
    value: Float,
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit = {},
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    stepSize: Float = 0f,
    labelFormatter: List<String>
) {
    val sliderState = remember { mutableStateOf(value) }

    // Compose does not currently support labels. issue tracker https://issuetracker.google.com/issues/236988201
    AndroidView(
        modifier = modifier,
        factory = { context ->
            SliderAccessible(context).apply {
                isEnabled = enabled
                addOnChangeListener { _, value, _ ->
                    onValueChange.invoke(value)
                    sliderState.value = value
                }
            }
        },
        update = {
            it.valueFrom = valueRange.start
            it.valueTo = valueRange.endInclusive
            it.value = sliderState.value
            it.stepSize = stepSize
            it.setLabelFormatter { value ->
                labelFormatter[value.toInt()]
            }
        }
    )
}

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun AOCBackButtonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}
