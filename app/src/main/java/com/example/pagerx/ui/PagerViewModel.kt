package com.example.pagerx.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagerx.domain.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PagerUiState(
    val endpoint: String = "172.16.52.1",
    val telemetry: DeviceTelemetry? = null,
    val agents: List<AiAgent> = emptyList(),
    val connectionState: ConnectionState = ConnectionState.CONNECTING,
    val diagnosticLines: List<String> = emptyList(),
    val auditLines: List<String> = emptyList(),
    val disconnectRequested: Boolean = false,
    val errorMessage: String? = null
)

class PagerViewModel(private val repository: PagerRepository) : ViewModel() {
    private val state = MutableStateFlow(PagerUiState())
    val state: StateFlow<PagerUiState> = state.asStateFlow()
    private var observationJob: Job? = null

    init { observeRepository() }
    private fun observeRepository() {
        observationJob?.cancel()
        observationJob = viewModelScope.launch {
            combine(repository.telemetry, repository.agents) { telemetry, agents ->
                telemetry to agents
            }.collect { (telemetry, agents) ->
                state.update { it.copy(telemetry = telemetry, agents = agents, connectionState = telemetry.connectionState) }
            }
        }
    }
    fun refresh() { viewModelScope.launch { repository.refreshTelemetry() } }
    fun runDiagnostic(diagnostic: SafeDiagnostic) {
        viewModelScope.launch {
            repository.runDiagnostic(diagnostic).onSuccess { result ->
                state.update { it.copy(diagnosticLines = (it.diagnosticLines + result.output).takeLast(20)) }
            }
        }
    }
    fun requestEmergencyDisconnect() { state.update { it.copy(disconnectRequested = true) } }
    fun cancelEmergencyDisconnect() { state.update { it.copy(disconnectRequested = false) } }
    fun confirmEmergencyDisconnect() {
        viewModelScope.launch {
            state.update { it.copy(disconnectRequested = false, connectionState = ConnectionState.DISCONNECTED) }
            observationJob?.cancel()
            repository.disconnect()
        }
    }
}
