plugins {
    `java-library`
    `maven-publish`
}

// SDK: Performance- und Debug-Tools für laufende JVMs (jvm_*, jfr_*, asprof_*, visualvm_*, debug_*) als Spring-AI-
// ToolCallbacks – ohne MCP-Server, ohne Berechtigungslogik. Der Server (performance-mcp-server) und andere Hosts
// (z.B. devtools-mcp) bauen darauf auf.
dependencies {
    // @Tool/@ToolParam und ToolCallback
    api("org.springframework.ai:spring-ai-model")

    // Container-Ziele (container:<name>): Docker/Podman über Container4J (Composite Build, siehe settings.gradle.kts)
    api("systems.grebe:container4j:0.1.0-SNAPSHOT")

    // Flame Graphs aus JFR (async-profiler-Konverter) und VisualVM-Engines (Heap-Analyse, CPU-Sampler)
    implementation("tools.profiler:jfr-converter:4.5")
    implementation("org.graalvm.visualvm.modules:org-graalvm-visualvm-lib-jfluid-heap:2.2")
    implementation("org.graalvm.visualvm.modules:org-graalvm-visualvm-lib-jfluid:2.2")
}

tasks.test {
    // Heap-Dumps der Fixture-JVM und VisualVM-Heap-Engine brauchen etwas Platz
    maxHeapSize = "1g"
}
