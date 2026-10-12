package com.example.pagerx.domain

enum class ConnectionState { DEMO, DISCONNECTED, CONNECTING, LIVE, STALE, FAILED }
enum class AgentCategory { OBSERVER, ANALYST, MONITORING, DEFENSE, REPORTING }
enum class SafeDiagnostic { UPTIME, INTERFACESTATUS, ROUTETABLE, SERVICESTATUS }
data class DeviceTelemetry(
    val cpuPercent: Float, val ramPercent: Float, val storagePercent: Float,
    val rxKbps: Float, val txKbps: Float, val uptimeSeconds: Long,
    val receivedAtMillis: Long, val connectionState: ConnectionState
)
data class AiAgent(
    val id: String, val name: String, val category: AgentCategory,
    val enabled: Boolean, val progress: Float, val task: String
)
data class DiagnosticResult(
    val diagnostic: SafeDiagnostic, val output: String,
    val completedAtMillis: Long, val success: Boolean
)
