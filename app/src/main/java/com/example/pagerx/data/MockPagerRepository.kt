package com.example.pagerx.data

import com.example.pagerx.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.math.max
import kotlin.random.Random

class MockPagerRepository : PagerRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val telemetryState = MutableStateFlow(
        DeviceTelemetry(31f, 48f, 62f, 820f, 430f, 2 * 86400L, System.currentTimeMillis(), ConnectionState.DEMO)
    )
    private val agentsState = MutableStateFlow(initialAgents())
    override val telemetry: Flow<DeviceTelemetry> = telemetryState.asStateFlow()
    override val agents: Flow<List<AiAgent>> = agentsState.asStateFlow()

    init { startSimulation() }
    private fun startSimulation() {
        scope.launch {
            while (isActive) {
                delay(1000L)
                telemetryState.update { old ->
                    old.copy(
                        cpuPercent = (old.cpuPercent + Random.nextFloat() * 12f - 6f).coerceIn(1f, 99f),
                        ramPercent = (old.ramPercent + Random.nextFloat() * 12f - 6f).coerceIn(1f, 99f),
                        storagePercent = max(old.storagePercent, 62f),
                        rxKbps = (old.rxKbps + Random.nextFloat() * 300f - 150f).coerceIn(0f, 5000f),
                        txKbps = (old.txKbps + Random.nextFloat() * 300f - 150f).coerceIn(0f, 5000f),
                        uptimeSeconds = old.uptimeSeconds + 1,
                        receivedAtMillis = System.currentTimeMillis()
                    )
                }
                agentsState.update { agents ->
                    agents.map { agent ->
                        val next = agent.progress + 0.03f
                        agent.copy(progress = if (next >= 1f) 0f else next)
                    }
                }
            }
        }
    }
    override suspend fun refreshTelemetry(): Result<DeviceTelemetry> = Result.success(telemetryState.value)
    override suspend fun runDiagnostic(diagnostic: SafeDiagnostic): Result<DiagnosticResult> {
        delay(350L)
        val output = when (diagnostic) {
            SafeDiagnostic.UPTIME -> "Uptime: active"
            SafeDiagnostic.INTERFACESTATUS -> "Interfaces: healthy"
            SafeDiagnostic.ROUTETABLE -> "Routes: read-only"
            SafeDiagnostic.SERVICESTATUS -> "Services: normal"
        }
        return Result.success(DiagnosticResult(diagnostic, output, System.currentTimeMillis(), true))
    }
    override suspend fun disconnect() {
        scope.coroutineContext[Job]?.cancel()
        telemetryState.update { it.copy(connectionState = ConnectionState.DISCONNECTED) }
    }
    private fun initialAgents() = listOf(
        AiAgent("observer", "Observer", AgentCategory.OBSERVER, true, 0.72f, "Collecting telemetry"),
        AiAgent("analyst", "Analyst", AgentCategory.ANALYST, true, 0.44f, "Evaluating trends"),
        AiAgent("monitor", "Monitor", AgentCategory.MONITORING, true, 0.81f, "Checking health signals"),
        AiAgent("defense", "Defense", AgentCategory.DEFENSE, true, 0.37f, "Reviewing safe alerts"),
        AiAgent("reporter", "Reporter", AgentCategory.REPORTING, true, 0.63f, "Preparing status summary")
    )
}
