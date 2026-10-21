rootProject.name = "performance-mcp"

// Container4J: OCI-Container aus Java steuern (Docker, Podman) – eigenständiges Projekt (Git-Submodule
// github.com/OriginalFelix/Container4J), als Composite Build eingebunden; Abhängigkeiten auf
// systems.grebe:container4j werden durch den Quellcode ersetzt. Fehlt das Submodule (Klon ohne
// --recurse-submodules), holt Gradle es selbst.
if (!file("container4j/settings.gradle.kts").exists()) {
    val init = providers.exec {
        commandLine("git", "submodule", "update", "--init", "container4j")
        workingDir = settingsDir
        isIgnoreExitValue = true
    }
    if (init.result.get().exitValue != 0) {
        throw GradleException("Container4J fehlt und 'git submodule update --init container4j' ist fehlgeschlagen:\n"
                + init.standardError.asText.get().trim())
    }
}
includeBuild("container4j")

// performance-mcp-core: SDK mit den Tool-Klassen (JVM, JFR, async-profiler, VisualVM, Debugger) – als Bibliothek nutzbar
// performance-mcp-server: eigenständiger MCP-Server (Spring Boot, stdio und Streamable HTTP)
include("performance-mcp-core", "performance-mcp-server")
