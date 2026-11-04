# Graph Report - performance-mcp  (2026-11-04)

## Corpus Check
- 72 files · ~32,734 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 898 nodes · 2341 edges · 58 communities (41 shown, 17 thin omitted)
- Extraction: 90% EXTRACTED · 10% INFERRED · 0% AMBIGUOUS · INFERRED: 232 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `29e4f617`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- org.springframework.ai.tool.annotation.Tool
- HeapAnalyzer.java
- Fixture
- org.springframework.ai.tool.ToolCallback
- ContainerRuntime
- CliContainerRuntime
- JfrAnalyzer
- JavaEnvironment
- DebugSession.java
- JvmTarget.java
- ToolBeans.java
- StackProfile
- arraylist
- DebugSession
- PerformanceTools.java
- PerformanceConfiguration.java
- AsyncProfilerTools.java
- DebugTools
- JfrTools
- CommandRunner.java
- ContainerAccess
- ArtifactStore
- org.junit.jupiter.api.Test
- JfrAnalyzer.java
- AsyncProfilerTools
- .artifacts
- CliContainerAccess
- list
- java.util.regex.Pattern
- VisualVmSettings
- .environment
- Performance MCP
- .images
- DebugSessions
- ProcessRunner
- ThreadDumpAnalyzer
- FixtureApp
- CLAUDE.md
- JavaSettings
- Breakpoint
- org.junit.jupiter.api.AfterAll
- PerformanceMcpApplication.java
- NamedResource
- ToolHints
- container4j/gradlew
- PodmanRuntime
- gradlew
- build.gradle.kts
- com.sun.jdi.ThreadReference
- Container4J

## God Nodes (most connected - your core abstractions)
1. `JavaEnvironment` - 83 edges
2. `ContainerRuntime` - 44 edges
3. `CliContainerRuntime` - 36 edges
4. `ArtifactStore` - 34 edges
5. `DebugSession` - 33 edges
6. `JvmTarget` - 29 edges
7. `StackProfile` - 22 edges
8. `AsyncProfilerTools` - 21 edges
9. `JfrAnalyzer` - 21 edges
10. `DebugTools` - 20 edges

## Surprising Connections (you probably didn't know these)
- `CliContainerAccess` --references--> `ContainerRuntime`  [EXTRACTED]
  performance-mcp-core/src/main/java/systems/grebe/performance/mcp/container/CliContainerAccess.java → container4j/src/main/java/systems/grebe/container4j/ContainerRuntime.java
- `PerformanceToolSet` --references--> `PerformanceArea`  [EXTRACTED]
  performance-mcp-server/src/main/java/systems/grebe/performance/mcp/server/PerformanceToolSet.java → performance-mcp-core/src/main/java/systems/grebe/performance/mcp/PerformanceArea.java
- `Probe` --references--> `Availability`  [EXTRACTED]
  performance-mcp-core/src/main/java/systems/grebe/performance/mcp/container/CliContainerAccess.java → container4j/src/main/java/systems/grebe/container4j/ContainerRuntime.java
- `CliContainerRuntime` --implements--> `ContainerRuntime`  [EXTRACTED]
  container4j/src/main/java/systems/grebe/container4j/cli/CliContainerRuntime.java → container4j/src/main/java/systems/grebe/container4j/ContainerRuntime.java
- `DockerRuntime` --inherits--> `CliContainerRuntime`  [EXTRACTED]
  container4j/src/main/java/systems/grebe/container4j/cli/DockerRuntime.java → container4j/src/main/java/systems/grebe/container4j/cli/CliContainerRuntime.java

## Import Cycles
- None detected.

## Communities (58 total, 17 thin omitted)

### Community 0 - "org.springframework.ai.tool.annotation.Tool"
Cohesion: 0.17
Nodes (4): org.springframework.ai.tool.annotation.Tool, JvmTarget, JvmInvasiveTools, JvmToolsTest

### Community 1 - "HeapAnalyzer.java"
Cohesion: 0.05
Nodes (34): arrayitemvalue, basicfileattributes, digestinputstream, fieldvalue, function, gcroot, heapfactory, heapsummary (+26 more)

### Community 2 - "Fixture"
Cohesion: 0.17
Nodes (3): VisualVmTools, Fixture, VisualVmToolsTest

### Community 4 - "ContainerRuntime"
Cohesion: 0.10
Nodes (4): ContainerRuntime, ContainerStats, ExecResult, RunSpec

### Community 6 - "JfrAnalyzer"
Cohesion: 0.15
Nodes (10): Aspect, ALLOCATION, CPU, EXCEPTIONS, GC, IO, LOCKS, SUMMARY (+2 more)

### Community 8 - "DebugSession.java"
Cohesion: 0.07
Nodes (28): absentinformationexception, arrayreference, atomicinteger, attachingconnector, blockingqueue, bootstrap, breakpointevent, classtype (+20 more)

### Community 9 - "JvmTarget.java"
Cohesion: 0.09
Nodes (22): arrays, collectors, com.sun.tools.attach.VirtualMachine, dataoutputstream, flatprofilecontainer, flightrecordermxbean, hotspotdiagnosticmxbean, java.lang.management.ThreadInfo (+14 more)

### Community 10 - "ToolBeans.java"
Cohesion: 0.15
Nodes (12): method, org.springframework.ai.chat.model.ToolContext, org.springframework.ai.tool.definition.ToolDefinition, org.springframework.ai.tool.metadata.ToolMetadata, Hinted, Hints, Override, Prefixed (+4 more)

### Community 11 - "StackProfile"
Cohesion: 0.15
Nodes (11): Frame, Kind, ALLOC, CPU, LOCK, METHOD, WALL, parse() (+3 more)

### Community 12 - "arraylist"
Cohesion: 0.15
Nodes (10): arguments, arraylist, bufferedreader, completablefuture, hashmap, hashset, inputstreamreader, main (+2 more)

### Community 13 - "DebugSession"
Cohesion: 0.18
Nodes (5): com.sun.jdi.event.ClassPrepareEvent, com.sun.jdi.Location, com.sun.jdi.VirtualMachine, DebugSession, Stop

### Community 14 - "PerformanceTools.java"
Cohesion: 0.10
Nodes (12): defaultvalue, org.springframework.boot.context.properties.ConfigurationProperties, DebugSettings, JfrSettings, JvmSettings, Asprof, Container, Debug (+4 more)

### Community 15 - "PerformanceConfiguration.java"
Cohesion: 0.20
Nodes (10): mcpschema, mcptoolutils, org.springframework.ai.mcp.customizer.McpSyncServerCustomizer, org.springframework.boot.context.properties.EnableConfigurationProperties, org.springframework.context.annotation.Bean, org.springframework.context.annotation.Configuration, org.springframework.context.annotation.Primary, SyncToolSpecification (+2 more)

### Community 16 - "AsyncProfilerTools.java"
Cohesion: 0.24
Nodes (4): ShellHints, Text, supplier, toolparam

### Community 19 - "CommandRunner.java"
Cohesion: 0.23
Nodes (10): bytearrayoutputstream, duration, inputstream, ioexception, java.nio.charset.Charset, locale, CommandRunner, standardcharsets (+2 more)

### Community 20 - "ContainerAccess"
Cohesion: 0.07
Nodes (8): AsprofInstaller, ContainerAccess, Container, Containers, Result, Jvm, LocalJvms, Result

### Community 21 - "ArtifactStore"
Cohesion: 0.06
Nodes (10): javax.management.MBeanServerConnection, org.graalvm.visualvm.lib.jfluid.results.cpu.CPUResultsSnapshot, ArtifactStore, JmxTarget, InContainer, Override, Local, Remote (+2 more)

### Community 22 - "org.junit.jupiter.api.Test"
Cohesion: 0.05
Nodes (21): autowired, DockerRuntime, CliContainerRuntimeTest, mcpserverfeatures, org.junit.jupiter.api.BeforeEach, org.junit.jupiter.api.condition.DisabledOnOs, org.junit.jupiter.api.Test, org.springframework.boot.test.context.SpringBootTest (+13 more)

### Community 23 - "JfrAnalyzer.java"
Cohesion: 0.12
Nodes (15): comparator, copyonwritearraylist, datetimeformatter, instant, jdk.jfr.consumer.RecordedEvent, jdk.jfr.consumer.RecordedStackTrace, localdatetime, predicate (+7 more)

### Community 24 - "AsyncProfilerTools"
Cohesion: 0.21
Nodes (3): AsyncProfilerTools, Run, AsyncProfilerContainerTest

### Community 25 - ".artifacts"
Cohesion: 0.26
Nodes (4): InContainer, Override, Result, Local

### Community 26 - "CliContainerAccess"
Cohesion: 0.23
Nodes (4): Availability, CliContainerAccess, Override, Probe

### Community 27 - "list"
Cohesion: 0.14
Nodes (17): assertthat, assertthatthrownby, assumptions, concurrenthashmap, datainputstream, executors, files, future (+9 more)

### Community 28 - "java.util.regex.Pattern"
Cohesion: 0.38
Nodes (5): java.util.regex.Pattern, linkedhashmap, map, patternsyntaxexception, ContainerSettings

### Community 31 - "Performance MCP"
Cohesion: 0.20
Nodes (9): Bauen und starten, Client-Einbindung, Entwicklung, Konfiguration, Performance MCP, SDK nutzen, stdio, Streamable HTTP (Standard) (+1 more)

### Community 33 - "DebugSessions"
Cohesion: 0.28
Nodes (3): Override, DebugSessions, Override

### Community 37 - "CLAUDE.md"
Cohesion: 0.33
Nodes (5): Arbeitsweise in diesem Repository, Architektur-Regeln, Code-Graph mit graphify, Graph bei jedem Commit aktualisieren, graphify immer verwenden

### Community 39 - "Breakpoint"
Cohesion: 0.33
Nodes (4): com.sun.jdi.ReferenceType, com.sun.jdi.request.BreakpointRequest, com.sun.jdi.request.ClassPrepareRequest, Breakpoint

### Community 41 - "PerformanceMcpApplication.java"
Cohesion: 0.50
Nodes (3): org.springframework.boot.autoconfigure.SpringBootApplication, PerformanceMcpApplication, springapplication

### Community 43 - "ToolHints"
Cohesion: 0.47
Nodes (5): elementtype, java.lang.annotation.Retention, java.lang.annotation.Target, ToolHints, retentionpolicy

### Community 44 - "container4j/gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 46 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 57 - "Container4J"
Cohesion: 0.50
Nodes (3): Bauen, Container4J, Laufzeiten

## Knowledge Gaps
- **42 isolated node(s):** `JAVA`, `JVM`, `JFR`, `ASPROF`, `VISUALVM` (+37 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **17 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `JavaEnvironment` connect `JavaEnvironment` to `org.springframework.ai.tool.annotation.Tool`, `HeapAnalyzer.java`, `Fixture`, `org.springframework.ai.tool.ToolCallback`, `arraylist`, `PerformanceTools.java`, `PerformanceConfiguration.java`, `AsyncProfilerTools.java`, `DebugTools`, `JfrTools`, `CommandRunner.java`, `ContainerAccess`, `ArtifactStore`, `AsyncProfilerTools`, `.artifacts`, `list`, `java.util.regex.Pattern`, `VisualVmSettings`, `.environment`, `DebugSessions`?**
  _High betweenness centrality (0.198) - this node is a cross-community bridge._
- **Why does `ContainerRuntime` connect `ContainerRuntime` to `.images`, `CliContainerRuntime`, `NamedResource`, `CommandRunner.java`, `ContainerAccess`, `CliContainerAccess`, `list`, `java.util.regex.Pattern`?**
  _High betweenness centrality (0.082) - this node is a cross-community bridge._
- **Why does `ArtifactStore` connect `ArtifactStore` to `org.springframework.ai.tool.annotation.Tool`, `HeapAnalyzer.java`, `JfrAnalyzer`, `JavaEnvironment`, `JvmTarget.java`, `StackProfile`, `AsyncProfilerTools.java`, `JfrTools`, `JfrAnalyzer.java`, `.artifacts`?**
  _High betweenness centrality (0.068) - this node is a cross-community bridge._
- **What connects `JAVA`, `JVM`, `JFR` to the rest of the system?**
  _42 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `HeapAnalyzer.java` be split into smaller, more focused modules?**
  _Cohesion score 0.052525252525252523 - nodes in this community are weakly interconnected._
- **Should `ContainerRuntime` be split into smaller, more focused modules?**
  _Cohesion score 0.10276679841897234 - nodes in this community are weakly interconnected._
- **Should `JfrAnalyzer` be split into smaller, more focused modules?**
  _Cohesion score 0.14942528735632185 - nodes in this community are weakly interconnected._