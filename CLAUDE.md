# Arbeitsweise in diesem Repository

Gradle-Multiprojekt (Java 25, Kotlin DSL, Spring Boot 4.1.1, Spring AI 2.0.1) mit zwei Modulen:
`performance-mcp-core` (SDK, Tools) und `performance-mcp-server` (MCP-Server). Container4J ist als Git-Submodule
(`container4j`) per `includeBuild` eingebunden – nach dem Klonen `git submodule update --init`.

Bauen und testen: `./gradlew build` (JDK 25; Tests starten echte Fixture-JVMs, die Container-Tests werden ohne
Docker/Podman übersprungen).

# Code-Graph mit graphify

Das Repository hat einen Wissensgraphen aus dem Tool `graphifyy` (CLI-Befehl `graphify`, installiert via
`uv tool install graphifyy` nach `~/.local/bin`).

- Ablage: `graphify-out/` (graph.json, GRAPH_REPORT.md, graph.html), committet; nur `graphify-out/cache/` ist per
  `.gitignore` ausgeschlossen. Alle Pfade sind relativ zum Repo-Root.
- graphify läuft **immer ohne Tokens/LLM**: nur `graphify update` (reine AST-Extraktion).
  Nicht verwenden: `graphify extract`, `label`, den `/graphify`-Skill oder `cluster-only` ohne `--no-label` – diese
  rufen ein LLM auf.

## graphify immer verwenden

Bei jeder Code-Suche, Architektur- oder Auswirkungsfrage **zuerst graphify** befragen (aus dem Repo-Root bzw.
Worktree heraus), erst danach gezielt Dateien lesen/greppen:
- `graphify query "<Frage>"` – relevante Knoten/Kanten zu einer Frage
- `graphify explain "<Klasse/Symbol>"` – Nachbarn und Verbindungen eines Knotens
- `graphify path "<A>" "<B>"` – kürzester Pfad zwischen zwei Knoten
- `graphify affected "<Symbol>"` – was von einer Änderung betroffen ist (vor Änderungen prüfen)
- `graphify god-nodes` – zentrale Architektur-Knoten
- Überblick: `graphify-out/GRAPH_REPORT.md`

Der Graph zeigt den Stand des letzten `update` – Ergebnisse an den tatsächlichen Dateien gegenprüfen.

## Graph bei jedem Commit aktualisieren

Vor **jedem** Commit:
1. Im Root des Worktrees `graphify update .` ausführen (immer aus dem Root, damit die Pfade relativ bleiben).
   Verweigert graphify das Überschreiben (weniger Knoten, z.B. nach gelöschtem Code): `graphify update . --force`.
2. Einen ggf. angelegten Backup-Ordner `graphify-out/<datum>/` löschen, nicht committen.
3. Das aktualisierte `graphify-out/` im selben Commit mit der eigentlichen Änderung committen.

# Architektur-Regeln

- `performance-mcp-core` hat **keine** Abhängigkeit auf devtools-mcp oder dessen plugin-api und enthält **keine**
  Berechtigungslogik; Hosts (devtools-mcp, der eigene Server) entscheiden, welche Tools sie anbieten.
- Tool-Namen, Beschreibungen, Parameter und Hints der `@Tool`-Methoden sind Vertrag (devtools-mcp bietet dieselben Tools an):
  Änderungen nur bewusst; `PerformanceToolsTest.toolNamesMatchDevtools` prüft die Namen gegen `expected-tools.txt`.
- Einstellungen sind Records unter `systems.grebe.performance.mcp.settings`; Basisverzeichnis über `JavaSettings.homeDir`
  (kein globaler Zustand wie ein festes `~/.devtools-mcp`).
