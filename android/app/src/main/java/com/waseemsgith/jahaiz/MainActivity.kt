package com.waseemsgith.jahaiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.waseemsgith.jahaiz.core.ui.theme.JahaizBlack
import com.waseemsgith.jahaiz.core.ui.theme.JahaizTheme
import com.waseemsgith.jahaiz.ui.navigation.JahaizNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JahaizTheme {
                val nav = rememberNavController()
                Surface(color = JahaizBlack, modifier = Modifier.fillMaxSize()) {
                    JahaizNavGraph(nav = nav)
                }
            }
        }
    }
}
