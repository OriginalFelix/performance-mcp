package systems.grebe.performance.mcp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.ai.tool.ToolCallback;
import systems.grebe.performance.mcp.asprof.AsprofInstaller;
import systems.grebe.performance.mcp.asprof.AsyncProfilerTools;
import systems.grebe.performance.mcp.container.ContainerAccess;
import systems.grebe.performance.mcp.debug.DebugSessions;
import systems.grebe.performance.mcp.debug.DebugTools;
import systems.grebe.performance.mcp.java.JavaEnvironment;
import systems.grebe.performance.mcp.jfr.JfrTools;
import systems.grebe.performance.mcp.jvm.JvmInvasiveTools;
import systems.grebe.performance.mcp.jvm.JvmTools;
import systems.grebe.performance.mcp.settings.AsprofSettings;
import systems.grebe.performance.mcp.settings.DebugSettings;
import systems.grebe.performance.mcp.settings.JavaSettings;
import systems.grebe.performance.mcp.settings.JfrSettings;
import systems.grebe.performance.mcp.settings.JvmSettings;
import systems.grebe.performance.mcp.settings.VisualVmSettings;
import systems.grebe.performance.mcp.tool.ToolBeans;
import systems.grebe.performance.mcp.visualvm.VisualVmLauncher;
import systems.grebe.performance.mcp.visualvm.VisualVmTools;

/**
 * Einstiegspunkt des SDK: erzeugt die Tool-Callbacks je {@link PerformanceArea}. Tool-Namen sind <em>nicht</em>
 * präfixiert ({@code processes}, {@code record} …); das Präfix {@code <area.id()>_} setzt der Host – etwa mit
 * {@link #prefixed(PerformanceArea, List)} oder (devtools) über seine Modul-Verwaltung.
 *
 * <p>Alle Methoden erwarten ein {@code Supplier<JavaEnvironment>}: die Tools fragen bei jedem Aufruf neu, Änderungen
 * der Java-Einstellungen wirken also sofort, wenn der Host einen neuen Wert liefert. Für feste Einstellungen genügt
 * {@link #environment(JavaSettings, ContainerAccess)} und {@code () -> env}.
 *
 * <pre>{@code
 * JavaEnvironment env = PerformanceTools.environment(JavaSettings.defaults(), ContainerAccess.none());
 * List<ToolCallback> jvm = PerformanceTools.prefixed(PerformanceArea.JVM,
 *         PerformanceTools.jvmTools(() -> env, JvmSettings.defaults()));
 * }</pre>
 */
public final class PerformanceTools {

    /** Schlüssel, unter dem Hosts die {@link DebugSessions} in ihrem Lebenszyklus-Zustand ablegen (z.B. devtools-Scope). */
    public static final String DEBUG_SESSIONS_KEY = "debug.sessions";

    private PerformanceTools() {
    }

    // ------------------------------------------------------------------ java (Basis)

    /** Java-Grundeinstellungen auswerten: Prozesse, Container, JMX, Artefaktablage. */
    public static JavaEnvironment environment(JavaSettings settings, ContainerAccess containers) {
        return new JavaEnvironment(settings, containers);
    }

    // ------------------------------------------------------------------ jvm

    /** {@code jvm_*} lesend: processes, info, threads, heap, native_memory. */
    public static List<ToolCallback> jvmReadTools(Supplier<JavaEnvironment> env) {
        return ToolBeans.callbacks(new JvmTools(env));
    }

    /** {@code jvm_*} invasiv: heap_dump, gc_run, jcmd – prüft bei jedem Aufruf {@code JavaSettings.allowInvasive}. */
    public static List<ToolCallback> jvmInvasiveTools(Supplier<JavaEnvironment> env, JvmSettings settings) {
        return ToolBeans.callbacks(new JvmInvasiveTools(env, settings.allowedCommands()));
    }

    /** Beide Gruppen, lesende zuerst (wie das devtools-Modul {@code jvm}). */
    public static List<ToolCallback> jvmTools(Supplier<JavaEnvironment> env, JvmSettings settings) {
        List<ToolCallback> tools = new ArrayList<>(jvmReadTools(env));
        tools.addAll(jvmInvasiveTools(env, settings));
        return tools;
    }

    // ------------------------------------------------------------------ jfr

    public static List<ToolCallback> jfrTools(Supplier<JavaEnvironment> env, JfrSettings settings) {
        return ToolBeans.callbacks(new JfrTools(env, settings.settings(), settings.maxSeconds()));
    }

    // ------------------------------------------------------------------ asprof

    public static List<ToolCallback> asprofTools(Supplier<JavaEnvironment> env, AsprofSettings settings) {
        return ToolBeans.callbacks(new AsyncProfilerTools(env, asprofInstaller(env, settings), settings.maxSeconds()));
    }

    /** Installer (lokal/Container, Download) – auch für Verbindungsprüfungen des Hosts. */
    public static AsprofInstaller asprofInstaller(Supplier<JavaEnvironment> env, AsprofSettings settings) {
        return new AsprofInstaller(settings, () -> env.get().downloads());
    }

    // ------------------------------------------------------------------ visualvm

    public static List<ToolCallback> visualVmTools(Supplier<JavaEnvironment> env, VisualVmSettings settings) {
        return ToolBeans.callbacks(new VisualVmTools(env, settings));
    }

    /** Startet bzw. findet die VisualVM-Oberfläche (für „Datei in VisualVM öffnen“ und Verbindungsprüfungen). */
    public static VisualVmLauncher visualVmLauncher(Supplier<JavaEnvironment> env, VisualVmSettings settings) {
        JavaEnvironment e = env.get();
        return new VisualVmLauncher(settings, e.jdkHome(), () -> env.get().downloads());
    }

    // ------------------------------------------------------------------ debug

    /**
     * {@code debug_*}. Die offenen Sitzungen liegen in {@code sessions}; der Host hält die Registry in seinem
     * Lebenszyklus (devtools: {@code scope.state(DEBUG_SESSIONS_KEY, DebugSessions::new)}) und schließt sie mit
     * {@link DebugSessions#close()} – dabei werden alle JDI-Verbindungen getrennt.
     */
    public static List<ToolCallback> debugTools(Supplier<JavaEnvironment> env, DebugSettings settings,
                                                DebugSessions sessions) {
        return ToolBeans.callbacks(new DebugTools(env, sessions, settings.maxWaitSeconds(), settings.variableDepth()));
    }

    // ------------------------------------------------------------------ Hilfen

    /** Setzt das Namenspräfix des Bereichs ({@code jvm_} …) auf alle Callbacks; Hinweise bleiben erhalten. */
    public static List<ToolCallback> prefixed(PerformanceArea area, List<ToolCallback> callbacks) {
        return ToolBeans.prefixed(area.id(), callbacks);
    }
}
