# Graph Report - performance-mcp-initial  (2026-10-21)

## Corpus Check
- 71 files · ~32,544 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 13 file(s) not represented in the graph (top: (none) 6, .jar 2, .properties 2)

## Summary
- 893 nodes · 2393 edges · 55 communities (16 shown, 39 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 311 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- org.springframework.ai.tool.annotation.Tool
- HeapAnalyzer.java
- Fixture
- PerformanceArea
- ContainerRuntime
- CliContainerRuntime
- JfrAnalyzer
- JavaEnvironment
- JmxCpuSampler.java
- ToolBeans.java
- StackProfile
- list
- PerformanceProperties
- PerformanceConfiguration.java
- JvmTarget
- JavaSettings
- DebugSession
- arraylist
- ContainerAccess
- Override
- org.junit.jupiter.api.Test
- org.springframework.ai.tool.ToolCallback
- DebugTools
- InContainer
- CliContainerAccess
- AsyncProfilerContainerTest.java
- Breakpoint
- ArtifactStore
- LocalJvms
- DebugSessions
- ProcessRunner
- ThreadDumpAnalyzer
- FixtureApp
- .toolNamesMatchDevtools
- ToolHints
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

## Communities (55 total, 39 thin omitted)

### Community 0 - "org.springframework.ai.tool.annotation.Tool"
Cohesion: 0.06
Nodes (11): AsyncProfilerTools, InContainer, Local, Run, JfrTools, JvmInvasiveTools, JvmTools, VisualVmTools (+3 more)

### Community 1 - "HeapAnalyzer.java"
Cohesion: 0.05
Nodes (9): Artifact, ASYNC_PROFILER_LINUX_ARM64, ASYNC_PROFILER_LINUX_X64, ASYNC_PROFILER_MACOS, VISUALVM, ToolDownloads, HeapAnalyzer, Open (+1 more)

### Community 2 - "Fixture"
Cohesion: 0.07
Nodes (5): AsprofInstaller, Result, AsyncProfilerContainerTest, Fixture, Entwicklung

### Community 3 - "PerformanceArea"
Cohesion: 0.06
Nodes (16): PerformanceArea, ASPROF, DEBUG, JAVA, JFR, JVM, VISUALVM, Instructions (+8 more)

### Community 4 - "ContainerRuntime"
Cohesion: 0.07
Nodes (10): Bauen, Container4J, Laufzeiten, ContainerRuntime, ContainerStats, ContainerSummary, ExecResult, ImageInfo (+2 more)

### Community 6 - "JfrAnalyzer"
Cohesion: 0.15
Nodes (10): Aspect, ALLOCATION, CPU, EXCEPTIONS, GC, IO, LOCKS, SUMMARY (+2 more)

### Community 7 - "JavaEnvironment"
Cohesion: 0.11
Nodes (3): JavaEnvironment, JmxTarget, Remote

### Community 9 - "JmxCpuSampler.java"
Cohesion: 0.11
Nodes (3): IdleFrames, JmxCpuSampler, Result

### Community 10 - "ToolBeans.java"
Cohesion: 0.14
Nodes (5): Hinted, Hints, Prefixed, ToolBeans, Wrapper

### Community 11 - "StackProfile"
Cohesion: 0.15
Nodes (9): Frame, Kind, ALLOC, CPU, LOCK, WALL, Result, Stack (+1 more)

### Community 14 - "PerformanceProperties"
Cohesion: 0.13
Nodes (9): DebugSettings, JfrSettings, Asprof, Container, Debug, Jfr, Jvm, PerformanceProperties (+1 more)

### Community 16 - "JvmTarget"
Cohesion: 0.24
Nodes (3): JvmTarget, ShellHints, Text

### Community 20 - "ContainerAccess"
Cohesion: 0.20
Nodes (3): ContainerAccess, Container, Containers

### Community 23 - "org.springframework.ai.tool.ToolCallback"
Cohesion: 0.31
Nodes (4): PerformanceTools, AsprofSettings, JvmSettings, SDK nutzen

### Community 26 - "CliContainerAccess"
Cohesion: 0.23
Nodes (3): Availability, CliContainerAccess, Probe

### Community 37 - ".toolNamesMatchDevtools"
Cohesion: 0.29
Nodes (5): Arbeitsweise in diesem Repository, Architektur-Regeln, Code-Graph mit graphify, Graph bei jedem Commit aktualisieren, graphify immer verwenden

## Knowledge Gaps
- **22 isolated node(s):** `JAVA`, `JVM`, `JFR`, `ASPROF`, `VISUALVM` (+17 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 206 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **39 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `JavaEnvironment` connect `JavaEnvironment` to `org.springframework.ai.tool.annotation.Tool`, `HeapAnalyzer.java`, `Fixture`, `list`, `PerformanceProperties`, `PerformanceConfiguration.java`, `JvmTarget`, `JavaSettings`, `arraylist`, `ContainerAccess`, `Override`, `org.springframework.ai.tool.ToolCallback`, `DebugTools`, `InContainer`, `path`, `AsyncProfilerContainerTest.java`, `ArtifactStore`, `LocalJvms`, `DebugSessions`, `files`?**
  _High betweenness centrality (0.216) - this node is a cross-community bridge._
- **What connects `JAVA`, `JVM`, `JFR` to the rest of the system?**
  _22 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `org.springframework.ai.tool.annotation.Tool` be split into smaller, more focused modules?**
  _Cohesion score 0.06405856783344772 - nodes in this community are weakly interconnected._
- **Why does `ContainerRuntime` connect `ContainerRuntime` to `CliContainerRuntime`, `list`, `arraylist`, `ContainerAccess`, `CliContainerAccess`, `path`, `AsyncProfilerContainerTest.java`?**
  _High betweenness centrality (0.084) - this node is a cross-community bridge._
- **Should `HeapAnalyzer.java` be split into smaller, more focused modules?**
  _Cohesion score 0.05117845117845118 - nodes in this community are weakly interconnected._
- **Why does `ArtifactStore` connect `ArtifactStore` to `org.springframework.ai.tool.annotation.Tool`, `HeapAnalyzer.java`, `JfrAnalyzer`, `JavaEnvironment`, `JmxCpuSampler.java`, `StackProfile`, `JfrAnalyzer.java`, `JvmTarget`, `Override`, `InContainer`?**
  _High betweenness centrality (0.069) - this node is a cross-community bridge._
- **Should `Fixture` be split into smaller, more focused modules?**
  _Cohesion score 0.06968641114982578 - nodes in this community are weakly interconnected._