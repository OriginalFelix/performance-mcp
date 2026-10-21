package systems.grebe.performance.mcp;

/**
 * Ein Bereich der Performance-/Debug-Tools. Die {@link #id()} ist zugleich das Präfix der Tool-Namen
 * ({@code jvm_processes}, {@code jfr_record} …). Texte (Anzeige, Beschreibung, Instructions) sind für Hosts gedacht, die
 * die Tools in eigene Module einpacken oder dem LLM als Server-Instructions mitgeben.
 */
public enum PerformanceArea {

    /** Java-Grundeinstellungen: stellt selbst keine Tools bereit, sondern die gemeinsame {@code JavaEnvironment}. */
    JAVA("java", "Java-Grundeinstellungen",
            "Gemeinsame Einstellungen für JVM-Diagnose, Flight Recorder, async-profiler, VisualVM und Debugger: "
                    + "welche JVMs erreichbar sind (lokal, Container, JMX) und wohin Aufzeichnungen und Dumps geschrieben werden.",
            null, null, 200, false),

    /** JVM-Diagnose über jcmd bzw. die DiagnosticCommand-MBean (lokal, Container, JMX). */
    JVM("jvm", "JVM-Diagnose (jcmd)",
            "Laufende JVMs finden und untersuchen: Version/Flags, Thread-Dumps mit Deadlock-Erkennung, Heap und "
                    + "Klassenhistogramm, Native Memory, Heap-Dumps. Ziele: lokal, im Container oder über JMX "
                    + "(siehe Java-Grundeinstellungen).",
            """
                    Für laufende JVMs diese Tools statt `jps`, `jcmd`, `jstack`, `jmap` oder `jinfo` in der Shell verwenden:
                    - `jvm_processes` zuerst: liefert die gültigen Ziele (PID, Name, `container:<name>`, `jmx:<alias>`).
                    - `jvm_info` (statt `jinfo`/`jcmd VM.flags`), `jvm_threads` (statt `jstack`, inkl. Deadlock-Erkennung), \
                    `jvm_heap` (statt `jmap -histo`), `jvm_native_memory` (statt `jcmd VM.native_memory`).
                    - Invasiv, nur wenn angeboten und nötig: `jvm_heap_dump` (statt `jmap -dump`), `jvm_gc_run`, `jvm_jcmd`.
                    PIDs ändern sich nach jedem Neustart der Ziel-JVM – vor Aufrufen mit PID erneut `jvm_processes` abfragen.""",
            "jvm_processes zuerst (PIDs ändern sich nach jedem Neustart), dann jvm_info, jvm_threads, "
                    + "jvm_heap.",
            210, true),

    /** Java Flight Recorder: Aufzeichnen (lokal, Container, JMX), Auswerten, Flame Graphs. */
    JFR("jfr", "Flight Recorder (JFR)",
            "Zeichnet mit dem Java Flight Recorder auf und wertet aus: CPU-Hotspots, Allokationen, GC-Pausen, "
                    + "Lock-Konkurrenz, Exceptions, langsame I/O – plus interaktive Flame Graphs. Funktioniert auf jeder "
                    + "Plattform, auch unter Windows.",
            """
                    Für Java-Flight-Recorder-Aufzeichnungen diese Tools statt `jcmd JFR.*` oder `jfr print` verwenden: \
                    `jfr_record` für eine feste Dauer, sonst `jfr_start` → `jfr_dump` → `jfr_stop`; auswerten mit `jfr_analyze` \
                    (cpu, allocation, gc, locks, io, exceptions, threads) und `jfr_flamegraph`. Erste Wahl für CPU-, Allokations- und \
                    Lock-Analysen auf jeder Plattform.""",
            "jfr_record für eine feste Dauer, auswerten mit jfr_analyze und jfr_flamegraph – erste Wahl "
                    + "für CPU-, Allokations- und Lock-Analysen.",
            220, true),

    /**
     * async-profiler 4.5: sampling-Profiler ohne Safepoint-Bias (CPU inkl. nativer/Kernel-Frames, Allokationen, Locks,
     * Wall-Clock). Läuft nativ unter Linux/macOS. Unter Windows werden JVMs in Linux-Containern profiliert.
     */
    ASPROF("asprof", "async-profiler",
            "Präziser Sampling-Profiler (CPU ohne Safepoint-Bias inkl. nativer Frames, Allokationen, Locks, Wall-Clock) "
                    + "mit Flame Graphs. Nativ auf Linux/macOS; unter Windows für JVMs in Linux-Containern (container:<name>). "
                    + "Lokale Windows-JVMs: Flight Recorder verwenden.",
            """
                    Statt `asprof`/`profiler.sh` in der Shell: `asprof_profile` für eine feste Dauer, sonst \
                    `asprof_start` → `asprof_stop`; `asprof_status` zeigt verfügbare Events. Genauer als JFR bei nativen Frames und ohne \
                    Safepoint-Bias; fehlt das Modul, `jfr_record` verwenden.""",
            null, 230, false),

    /** VisualVM: eingebettete Engines (Heap-Analyse, CPU-Sampler über JMX) sowie die VisualVM-Oberfläche. */
    VISUALVM("visualvm", "VisualVM",
            "Heap-Dump-Analyse (größte Objekte, Pfad zur GC-Wurzel) und CPU-Sampling über JMX mit den eingebetteten "
                    + "VisualVM-Engines – auch für entfernte JVMs. Zusätzlich öffnet die VisualVM-Oberfläche Prozesse, "
                    + "Heap-Dumps, JFR-Dateien und Snapshots, um Befunde selbst anzusehen.",
            """
                    - `visualvm_heap_analyze` für `.hprof`-Dateien (größte Objekte, Retained Size, Pfad zur GC-Wurzel) – statt \
                    Heap-Dumps selbst zu parsen oder `jhat`/Eclipse MAT per Shell zu starten.
                    - `visualvm_sample_cpu` für CPU-Sampling über JMX, auch bei entfernten JVMs.
                    - `visualvm_open`/`visualvm_open_file` nur, wenn der Nutzer die Oberfläche selbst sehen will; sie öffnen ein Fenster \
                    bei ihm und liefern keine Auswertung.""",
            null, 240, true),

    /** Java-Debugger (JDI) – verbindet sich mit einer per JDWP gestarteten JVM. Nur lesend, keine Ausdrucksauswertung. */
    DEBUG("debug", "Debugger (JDI)",
            "Hängt sich an eine JVM mit JDWP (-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005): "
                    + "Breakpoints setzen, auf Treffer warten, Stack und Variablen lesen, Schritte ausführen. "
                    + "Keine Ausdrucksauswertung und keine Wertänderung. Hält Threads an – nur für Entwicklungs-/Test-JVMs.",
            """
                    Für Breakpoint-Debugging einer JVM mit JDWP diese Tools statt `jdb` verwenden: `debug_attach` → \
                    `debug_set_breakpoint` → `debug_wait_for_break` → `debug_stack`/`debug_variables` → `debug_step`/`debug_resume`; zum \
                    Schluss `debug_detach`. Breakpoints halten Threads der Ziel-JVM an – vorher mit dem Nutzer klären, wenn er gerade \
                    selbst in der JVM arbeitet oder einen eigenen Debugger verbunden hat.""",
            "debug_attach → debug_set_breakpoint → debug_wait_for_break → debug_stack/debug_variables → "
                    + "debug_detach; hält Threads der Ziel-JVM an.",
            250, false);

    private final String id;
    private final String displayName;
    private final String description;
    private final String instructions;
    private final String briefInstructions;
    private final int order;
    private final boolean enabledByDefault;

    PerformanceArea(String id, String displayName, String description, String instructions, String briefInstructions,
                    int order, boolean enabledByDefault) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.instructions = instructions;
        this.briefInstructions = briefInstructions;
        this.order = order;
        this.enabledByDefault = enabledByDefault;
    }

    /** Kennung und Präfix der Tool-Namen, z.B. {@code jvm}. */
    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public String description() {
        return description;
    }

    /** Nutzungshinweise für das LLM (Markdown) oder {@code null}. */
    public String instructions() {
        return instructions;
    }

    /** Kurzfassung der Instructions oder {@code null} (dann: erster Satz der Beschreibung). */
    public String briefInstructions() {
        return briefInstructions;
    }

    /** Reihenfolge, wie sie das devtools-Modul hat (200 = Grundeinstellungen). */
    public int order() {
        return order;
    }

    /** Ob das devtools-Modul standardmäßig eingeschaltet ist ({@code ToolModule.enabledByDefault()}). */
    public boolean enabledByDefault() {
        return enabledByDefault;
    }

    /** {@code true} für Bereiche, die Tools anbieten (alle außer {@link #JAVA}). */
    public boolean hasTools() {
        return this != JAVA;
    }

    /** Der Bereich mit dieser Kennung oder {@code null}. */
    public static PerformanceArea byId(String id) {
        for (PerformanceArea a : values()) {
            if (a.id.equals(id)) {
                return a;
            }
        }
        return null;
    }
}
