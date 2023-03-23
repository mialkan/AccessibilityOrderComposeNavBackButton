package com.mialkan.aocbackbutton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FirstScreen(findNavController())
            }
        }
    }
}

@Composable
fun FirstScreen(navController: NavController) {
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
                })
            }

        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Box(modifier = Modifier.padding(20.dp)) {
                    Button(
                        onClick = { navController.navigate(R.id.action_FirstFragment_to_SecondFragment) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Next Screen")
                    }
                }
            }
        }
    }
}
