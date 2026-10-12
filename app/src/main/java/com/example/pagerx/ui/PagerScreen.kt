package com.example.pagerx.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pagerx.domain.*
import com.example.pagerx.ui.theme.PagerXTheme

@Composable
fun PagerScreen(viewModel: PagerViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PagerXTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item { Text("PAGER-X", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary) }
                item { Card { Column(Modifier.padding(16.dp)) { Text("TARGET ${state.endpoint}") } } }
                items(state.agents) { agent ->
                    Card { Column(Modifier.padding(14.dp)) { Text(agent.name, color = MaterialTheme.colorScheme.primary); Text(agent.task) } }
                }
                item {
                    Button(onClick = { viewModel.requestEmergencyDisconnect() }) { Text("Emergency Disconnect") }
                }
            }
        }
    }
}
