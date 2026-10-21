# Performance MCP

MCP-Server und Java-SDK für **Diagnose, Performance-Analyse und Debugging laufender JVMs**: Thread-Dumps mit
Deadlock-Erkennung, Heap-Histogramme und Heap-Dumps, Flight Recorder, async-profiler, VisualVM-Engines (Heap-Analyse,
CPU-Sampler) und ein JDI-Debugger. Die Tools stammen aus [devtools-mcp](https://github.com/OriginalFelix/devtools-mcp)
und sind hier eigenständig nutzbar – ohne Desktop-App, Benutzerverwaltung oder Berechtigungslogik.

Ziele der Tools: lokale JVMs, JVMs in Docker-/Podman-Containern (`container:<name>`) und entfernte JVMs über JMX
(`jmx:<alias>`).

| Modul | Inhalt |
|---|---|
| `performance-mcp-core` | SDK (Paket `systems.grebe.performance.mcp`): Tool-Klassen, Analyzer, `JavaEnvironment`, Einstellungs-Records, Fabrik `PerformanceTools`. Keine Abhängigkeit auf einen MCP-Server. |
| `performance-mcp-server` | Selbststartfähige Spring-Boot-App: MCP-Server über Streamable HTTP oder stdio, Konfiguration über `application.yml`/Umgebung (`performance.*`). Alle Tools aktiv, keine Berechtigungsprüfung, keine Oberfläche. |
| `container4j` | Git-Submodule ([Container4J](https://github.com/OriginalFelix/Container4J)), als Composite Build eingebunden. |

## Tools

Tool-Namen sind nach Bereich präfixiert.

| Bereich | Tools |
|---|---|
| **JVM-Diagnose** (jcmd) | `jvm_processes`, `jvm_info`, `jvm_threads` (inkl. Deadlock-Erkennung), `jvm_heap`, `jvm_native_memory` · invasiv: `jvm_heap_dump`, `jvm_gc_run`, `jvm_jcmd` (Allowlist) |
| **Flight Recorder** | `jfr_record`, `jfr_start`, `jfr_status`, `jfr_dump`, `jfr_stop`, `jfr_analyze` (cpu/allocation/gc/locks/io/exceptions/threads), `jfr_flamegraph` |
| **async-profiler** 4.5 | `asprof_profile`, `asprof_start`, `asprof_stop`, `asprof_status` – Linux/macOS nativ, unter Windows für JVMs in Docker/Podman-Containern |
| **VisualVM** | `visualvm_heap_analyze` (Heap-Engine: Histogramm, Retained Size, Pfad zur GC-Wurzel), `visualvm_sample_cpu` (JMX-Sampler, `.nps`-Snapshot), `visualvm_open`, `visualvm_open_file` (externe VisualVM-GUI) |
| **Debugger** (JDI) | `debug_attach`, `debug_sessions`, `debug_detach`, `debug_set_breakpoint`, `debug_clear_breakpoint`, `debug_wait_for_break`, `debug_threads`, `debug_stack`, `debug_variables`, `debug_step`, `debug_resume` |

Zusammen 34 Tools. Die Java-Grundeinstellungen (`performance.java.*`) haben keine eigenen Tools, sie liefern JDK,
Ablageordner, Prozessfilter und JMX-Ziele für alle Bereiche. Die Instructions der Bereiche schickt der Server beim
`initialize` als MCP-`instructions` mit.

## Bauen und starten

Voraussetzung: JDK 25 und Git (für das Submodule).

```bash
git clone --recurse-submodules https://github.com/OriginalFelix/performance-mcp.git
cd performance-mcp
./gradlew build                                   # baut und testet alles
./gradlew :performance-mcp-server:bootJar         # build/libs/performance-mcp-0.1.0-SNAPSHOT.jar
```

Ohne `--recurse-submodules` holt Gradle `container4j` beim ersten Lauf selbst.

### Streamable HTTP (Standard)

```bash
java -jar performance-mcp-server/build/libs/performance-mcp-0.1.0-SNAPSHOT.jar
# oder: ./gradlew :performance-mcp-server:bootRun
```

Endpunkt: `http://127.0.0.1:8765/mcp`. Der Server lauscht nur auf `127.0.0.1` und hat **keine Anmeldung** – wer ihn mit
`SERVER_ADDRESS=0.0.0.0` öffnet, gibt Heap-Dumps und Debugger-Zugriff auf alle erreichbaren JVMs frei. Port: `SERVER_PORT`.

### stdio

Der Client startet den Server als Kindprozess; stdout gehört dem Protokoll, Logs gehen nach stderr:

```bash
java -jar performance-mcp-server/build/libs/performance-mcp-0.1.0-SNAPSHOT.jar --spring.profiles.active=stdio
```

## Client-Einbindung

Claude Code, `.mcp.json` (Projekt) bzw. `~/.claude.json`:

```json
{
  "mcpServers": {
    "performance": {
      "type": "http",
      "url": "http://127.0.0.1:8765/mcp"
    }
  }
}
```

oder per stdio:

```json
{
  "mcpServers": {
    "performance": {
      "command": "java",
      "args": ["-jar", "/pfad/zu/performance-mcp-0.1.0-SNAPSHOT.jar", "--spring.profiles.active=stdio"],
      "env": { "PERFORMANCE_JAVA_JMX_TARGETS": "test=srv01:9010" }
    }
  }
}
```

Kurz: `claude mcp add --transport http performance http://127.0.0.1:8765/mcp`.

## Konfiguration

`application.yml` neben dem Jar, Umgebungsvariablen (`PERFORMANCE_JAVA_MAX_ARTIFACTS=100`) oder Kommandozeile
(`--performance.jfr.max-seconds=120`). Alle Werte sind optional.

| Eigenschaft | Standard | Bedeutung |
|---|---|---|
| `performance.home` | `~/.performance-mcp` | Basisverzeichnis: `diagnostics/` (Artefakte), `tools/` (Downloads) |
| `performance.java.jdk-home` | JDK des Servers | JDK für jcmd/Attach |
| `performance.java.artifact-dir` | `<home>/diagnostics` | Ablage für JFR-Aufzeichnungen, Heap-Dumps, Flame Graphs, Snapshots |
| `performance.java.max-artifacts` | `50` | Ältere Dateien werden automatisch gelöscht |
| `performance.java.include-processes` | alle | Regex auf Hauptklasse/Kommandozeile der erlaubten JVMs |
| `performance.java.exclude-processes` | IntelliJ, Gradle-Daemon, JetBrains | Regex für auszuschließende JVMs |
| `performance.java.jmx-targets` | – | Liste `alias=host:port`, ansprechbar als `jmx:<alias>` |
| `performance.java.jmx-user` / `jmx-password` | – | JMX-Zugangsdaten |
| `performance.java.debug-hosts` | `localhost\|127\.0\.0\.1` | Regex für Hosts, an deren JDWP-Port sich der Debugger hängen darf |
| `performance.java.allow-invasive` | `true` | Heap-Dumps, GC erzwingen, freie jcmd-Befehle |
| `performance.java.max-output-lines` | `400` | Begrenzung der Tool-Ausgaben |
| `performance.java.exclude-self` | `true` | Die Server-JVM selbst nicht als Ziel anbieten |
| `performance.jvm.enabled` | `true` | Bereich `jvm_*` anbieten (ebenso `jfr`, `asprof`, `visualvm`, `debug`) |
| `performance.jvm.allowed-commands` | 22 Diagnosebefehle (`VM.version` …) | Erlaubte jcmd-Befehle für `jvm_jcmd` |
| `performance.jfr.settings` | `profile` | Standard-Profil (`profile` ≈ 2 % Overhead, `default` < 1 %) |
| `performance.jfr.max-seconds` | `300` | Max. Dauer für `jfr_record` |
| `performance.asprof.home` | – | Entpacktes async-profiler-Verzeichnis (mit `bin/asprof`); leer = automatischer Download |
| `performance.asprof.auto-download` | `true` | async-profiler 4.5 (SHA-256-geprüft) nach `<home>/tools` laden |
| `performance.asprof.max-seconds` | `300` | Max. Profildauer |
| `performance.visualvm.home` | – | VisualVM-Installation (nur für die GUI nötig) |
| `performance.visualvm.auto-download` | `true` | VisualVM 2.2.2 (SHA-256-geprüft) beim ersten Öffnen laden |
| `performance.visualvm.sample-interval-ms` | `20` | Abstand der Thread-Dumps beim CPU-Sampler |
| `performance.debug.max-wait-seconds` | `120` | Max. Wartezeit auf einen Breakpoint |
| `performance.debug.variable-depth` | `2` | Objekttiefe bei Variablen (0–5) |
| `performance.container.enabled` | `true` | `container:<name>`-Ziele (Docker/Podman) |
| `performance.container.runtime` | `auto` | `auto` (erste erreichbare), `docker` oder `podman` |
| `performance.container.allowed-containers` | `.*` | Regex auf Containernamen |
| `performance.container.docker-binary` / `docker-context` / `docker-host` | `docker` | docker-CLI, `--context`, `-H` |
| `performance.container.podman-binary` / `podman-connection` / `podman-url` | `podman` | podman-CLI, `--connection`, `--url` |

Sicherheit: Es gibt keine Rechteprüfung – der Zugriff ist durch die Konfiguration begrenzt (Prozessfilter,
`allow-invasive`, `allowed-commands`, `debug-hosts`, `allowed-containers`). Downloads sind per SHA-256 geprüft.
Der Debugger wertet keine Ausdrücke aus und verändert keine Werte.

## SDK nutzen

```kotlin
dependencies {
    implementation("systems.grebe:performance-mcp-core:0.1.0-SNAPSHOT")
}
```

(Das Projekt als Composite Build einbinden: `includeBuild("performance-mcp")`.)

```java
JavaEnvironment env = PerformanceTools.environment(
        JavaSettings.defaults().withHomeDir(Path.of("/var/lib/mein-host")), ContainerAccess.none());

// Spring-AI-ToolCallbacks je Bereich; Namen sind unpräfixiert ("processes"), prefixed() setzt "jvm_"
List<ToolCallback> jvm   = PerformanceTools.prefixed(PerformanceArea.JVM,   PerformanceTools.jvmTools(() -> env, JvmSettings.defaults()));
List<ToolCallback> jfr   = PerformanceTools.prefixed(PerformanceArea.JFR,   PerformanceTools.jfrTools(() -> env, JfrSettings.defaults()));
List<ToolCallback> asprof = PerformanceTools.prefixed(PerformanceArea.ASPROF, PerformanceTools.asprofTools(() -> env, AsprofSettings.defaults()));
List<ToolCallback> vvm   = PerformanceTools.prefixed(PerformanceArea.VISUALVM, PerformanceTools.visualVmTools(() -> env, VisualVmSettings.defaults()));

DebugSessions sessions = new DebugSessions();          // schließbar: close() trennt alle JDI-Verbindungen
List<ToolCallback> debug = PerformanceTools.prefixed(PerformanceArea.DEBUG, PerformanceTools.debugTools(() -> env, DebugSettings.defaults(), sessions));

String instructions = PerformanceArea.JVM.instructions();   // auch briefInstructions(), description(), displayName()
```

- `PerformanceTools.jvmReadTools` / `jvmInvasiveTools` liefern lesende und invasive `jvm_*`-Tools getrennt, `jvmTools` beide.
- Die Tools fragen das `Supplier<JavaEnvironment>` bei jedem Aufruf – ein Host mit änderbaren Einstellungen liefert einfach
  ein neues Environment (`JavaEnvironment` ist unveränderlich).
- Container-Ziele: `ContainerAccess` implementieren (Laufzeit + Freigabe) oder `new CliContainerAccess(ContainerSettings)`
  für Docker/Podman-CLI verwenden.
- `ToolBeans.hints(callback)` liefert die MCP-Hinweise (`readOnly` …) eines Callbacks, auch durch `prefixed()` hindurch.
- `PerformanceTools.asprofInstaller(..)` und `visualVmLauncher(..)` bieten Verbindungsprüfung und „Datei in VisualVM öffnen“.

## Entwicklung

Siehe `CLAUDE.md` (Arbeitsweise, graphify-Code-Graph). Tests: `./gradlew build`; sie starten echte Fixture-JVMs
(JMX, JDWP) und prüfen alle Tools; `AsyncProfilerContainerTest` läuft nur mit Docker/Podman und dem Image
`eclipse-temurin:25-jdk`.
