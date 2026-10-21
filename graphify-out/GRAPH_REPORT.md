# Graph Report - performance-mcp-debug-hosts  (2026-10-21)

## Corpus Check
- 71 files · ~32,560 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 13 file(s) not represented in the graph (top: (none) 6, .jar 2, .properties 2)

## Summary
- 894 nodes · 2397 edges · 53 communities (15 shown, 38 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 312 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `16440ebe`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.springframework.ai.tool.annotation.Tool
- JavaSettings
- PerformanceArea
- ContainerRuntime
- CliContainerRuntime
- JfrAnalyzer
- JavaEnvironment
- org.springframework.ai.tool.ToolCallback
- StackProfile
- PerformanceMcpApplicationTest.java
- PerformanceProperties
- PerformanceConfiguration.java
- JvmTarget
- Override
- .sample
- arraylist
- ContainerAccess
- Override
- org.junit.jupiter.api.Test
- .run
- InContainer
- CliContainerAccess
- AsyncProfilerContainerTest.java
- ToolDownloads
- AsprofInstaller
- Performance MCP
- LocalJvms
- Artifact
- ProcessRunner
- ThreadDumpAnalyzer
- FixtureApp
- .toolNamesMatchDevtools
- .build
- PerformanceToolSet
- DockerRuntime
- PerformanceMcpApplication.java
- PodmanRuntime

## God Nodes (most connected - your core abstractions)
1. `JavaEnvironment` - 83 edges
2. `ContainerRuntime` - 45 edges
3. `CliContainerRuntime` - 36 edges
4. `ArtifactStore` - 34 edges
5. `DebugSession` - 33 edges
6. `JvmTarget` - 29 edges
7. `PerformanceArea` - 23 edges
8. `StackProfile` - 22 edges
9. `JfrAnalyzer` - 21 edges
10. `JavaSettings` - 20 edges

## Surprising Connections (you probably didn't know these)
- `SDK nutzen` --references--> `ContainerAccess`  [INFERRED]
  README.md → performance-mcp-core/src/main/java/systems/grebe/performance/mcp/container/ContainerAccess.java
- `Entwicklung` --references--> `AsyncProfilerContainerTest`  [INFERRED]
  README.md → performance-mcp-core/src/test/java/systems/grebe/performance/mcp/asprof/AsyncProfilerContainerTest.java
- `Laufzeiten` --references--> `ContainerRuntime`  [INFERRED]
  container4j/README.md → container4j/src/main/java/systems/grebe/container4j/ContainerRuntime.java
- `CliContainerAccess` --references--> `ContainerRuntime`  [EXTRACTED]
  performance-mcp-core/src/main/java/systems/grebe/performance/mcp/container/CliContainerAccess.java → container4j/src/main/java/systems/grebe/container4j/ContainerRuntime.java
- `PerformanceToolSet` --references--> `PerformanceArea`  [EXTRACTED]
  performance-mcp-server/src/main/java/systems/grebe/performance/mcp/server/PerformanceToolSet.java → performance-mcp-core/src/main/java/systems/grebe/performance/mcp/PerformanceArea.java

## Import Cycles
- None detected.

## Communities (53 total, 38 thin omitted)

### Community 0 - "org.springframework.ai.tool.annotation.Tool"
Cohesion: 0.06
Nodes (12): Breakpoint, DebugSession, Stop, DebugSessions, DebugTools, JfrTools, JvmInvasiveTools, JvmTools (+4 more)

### Community 3 - "PerformanceArea"
Cohesion: 0.12
Nodes (7): PerformanceArea, ASPROF, DEBUG, JAVA, JFR, JVM, VISUALVM

### Community 4 - "ContainerRuntime"
Cohesion: 0.07
Nodes (10): Bauen, Container4J, Laufzeiten, ContainerRuntime, ContainerStats, ContainerSummary, ExecResult, ImageInfo (+2 more)

### Community 6 - "JfrAnalyzer"
Cohesion: 0.10
Nodes (12): Aspect, ALLOCATION, CPU, EXCEPTIONS, GC, IO, LOCKS, SUMMARY (+4 more)

### Community 10 - "org.springframework.ai.tool.ToolCallback"
Cohesion: 0.06
Nodes (15): PerformanceTools, DebugSettings, JfrSettings, JvmSettings, VisualVmSettings, Hinted, Hints, Prefixed (+7 more)

### Community 11 - "StackProfile"
Cohesion: 0.07
Nodes (13): AsyncProfilerTools, InContainer, Local, Run, Frame, Kind, ALLOC, CPU (+5 more)

### Community 14 - "PerformanceProperties"
Cohesion: 0.19
Nodes (7): Asprof, Container, Debug, Jfr, Jvm, PerformanceProperties, Visualvm

### Community 16 - "JvmTarget"
Cohesion: 0.18
Nodes (3): JvmTarget, ShellHints, Text

### Community 18 - ".sample"
Cohesion: 0.26
Nodes (3): IdleFrames, JmxCpuSampler, Result

### Community 20 - "ContainerAccess"
Cohesion: 0.22
Nodes (3): ContainerAccess, Container, Containers

### Community 24 - ".run"
Cohesion: 0.27
Nodes (3): Result, AsyncProfilerContainerTest, Entwicklung

### Community 26 - "CliContainerAccess"
Cohesion: 0.23
Nodes (3): Availability, CliContainerAccess, Probe

### Community 31 - "Performance MCP"
Cohesion: 0.22
Nodes (7): Bauen und starten, Client-Einbindung, Konfiguration, Performance MCP, stdio, Streamable HTTP (Standard), Tools

### Community 33 - "Artifact"
Cohesion: 0.29
Nodes (5): Artifact, ASYNC_PROFILER_LINUX_ARM64, ASYNC_PROFILER_LINUX_X64, ASYNC_PROFILER_MACOS, VISUALVM

### Community 37 - ".toolNamesMatchDevtools"
Cohesion: 0.33
Nodes (5): Arbeitsweise in diesem Repository, Architektur-Regeln, Code-Graph mit graphify, Graph bei jedem Commit aktualisieren, graphify immer verwenden

## Knowledge Gaps
- **22 isolated node(s):** `JAVA`, `JVM`, `JFR`, `ASPROF`, `VISUALVM` (+17 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 206 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **38 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `JavaEnvironment` connect `JavaEnvironment` to `org.springframework.ai.tool.annotation.Tool`, `JavaSettings`, `org.springframework.ai.tool.ToolCallback`, `StackProfile`, `HeapAnalyzer.java`, `PerformanceMcpApplicationTest.java`, `PerformanceProperties`, `PerformanceConfiguration.java`, `JvmTarget`, `Override`, `arraylist`, `ContainerAccess`, `Override`, `.run`, `InContainer`, `path`, `AsyncProfilerContainerTest.java`, `ToolDownloads`, `AsprofInstaller`, `LocalJvms`?**
  _High betweenness centrality (0.216) - this node is a cross-community bridge._
- **What connects `JAVA`, `JVM`, `JFR` to the rest of the system?**
  _22 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.springframework.ai.tool.annotation.Tool` be split into smaller, more focused modules?**
  _Cohesion score 0.055860805860805864 - nodes in this community are weakly interconnected._
- **Why does `ContainerRuntime` connect `ContainerRuntime` to `CliContainerRuntime`, `arraylist`, `ContainerAccess`, `CliContainerAccess`, `path`, `AsyncProfilerContainerTest.java`?**
  _High betweenness centrality (0.084) - this node is a cross-community bridge._
- **Should `JavaSettings` be split into smaller, more focused modules?**
  _Cohesion score 0.11375661375661375 - nodes in this community are weakly interconnected._
- **Why does `ArtifactStore` connect `Override` to `org.springframework.ai.tool.annotation.Tool`, `JfrAnalyzer`, `JavaEnvironment`, `JmxCpuSampler.java`, `StackProfile`, `HeapAnalyzer.java`, `JvmTarget`, `Override`, `.sample`, `JfrAnalyzer.java`, `InContainer`?**
  _High betweenness centrality (0.069) - this node is a cross-community bridge._
- **Should `PerformanceArea` be split into smaller, more focused modules?**
  _Cohesion score 0.11764705882352941 - nodes in this community are weakly interconnected._