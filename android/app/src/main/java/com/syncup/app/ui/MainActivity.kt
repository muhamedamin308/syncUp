package com.syncup.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.syncup.app.presentation.navigation.SyncUpNavGraph
import com.syncup.app.presentation.sessions.SessionViewModel
import com.syncup.app.ui.theme.SyncUpTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Session ViewModel lives as activity scope - survives all screen changes
    private val sessionViewModel: SessionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SyncUpTheme {
                SyncUpNavGraph()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        sessionViewModel.goOffline()
    }

    override fun onStart() {
        super.onStart()
        sessionViewModel.goOnline()
    }
}
