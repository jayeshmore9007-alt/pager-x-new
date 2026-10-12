package com.example.pagerx.domain

import kotlinx.coroutines.flow.Flow

interface PagerRepository {
    val telemetry: Flow<DeviceTelemetry>
    val agents: Flow<List<AiAgent>>
    suspend fun refreshTelemetry(): Result<DeviceTelemetry>
    suspend fun runDiagnostic(diagnostic: SafeDiagnostic): Result<DiagnosticResult>
    suspend fun disconnect()
}
